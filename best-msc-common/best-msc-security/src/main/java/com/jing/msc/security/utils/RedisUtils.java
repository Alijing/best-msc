package com.jing.msc.security.utils;

import com.jing.common.core.util.JsonUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : jing
 * @since : 2024/7/3 11:27
 */
@Service("redisUtils")
public class RedisUtils {

    /**
     * 命令执行成功返回字符串OK
     */
    private static final String SUCCESS = "OK";

    /**
     * zset的区间负无穷标识符
     */
    public static final String ZSET_MINUS_INFINITY = "-inf";

    /**
     * zset的区间正无穷标识符
     */
    public static final String ZSET_POSITIVE_INFINITY = "+inf";

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate redisTemplate;

    public RedisUtils(StringRedisTemplate stringRedisTemplate, RedisTemplate redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 执行redis缓存数据操作
     *
     * @param callback 实际操作对象
     * @return
     */
    public <T> T execute(StringRedisCallback<T> callback) {
        RedisCallback<T> _callback = connection -> callback.doInRedis((StringRedisConnection) connection);
        return stringRedisTemplate.execute(_callback);
    }

    public interface StringRedisCallback<T> {

        /**
         * Gets called by {@link RedisTemplate} with an active Redis connection. Does not need to care about activating or
         * closing the connection or handling exceptions.
         *
         * @param connection active Redis connection
         * @return a result object or {@code null} if none
         * @throws DataAccessException
         */
        @Nullable
        T doInRedis(StringRedisConnection connection) throws DataAccessException;
    }

    /**
     * 执行lua脚本
     *
     * @param tClass    返回结果class，如果没有返回值，则传null
     * @param luaScript lua脚本
     * @param keys      lua脚本接收的key，脚本内变量为 KEYS[i]，其中i从1开始
     * @param args      lua脚本接收的参数，脚本内变量为 ARGV[i]，其中i从1开始
     * @param <T>       返回结果类型
     * @return 执行结果，如果tClass为null或者Void.class，则返回null
     */
    public <T> T execute(Class<T> tClass, String luaScript, List<String> keys, Object... args) {
        if (Objects.isNull(tClass)) {
            tClass = (Class<T>) Void.class;
        }
        // 创建并设置Lua脚本
        DefaultRedisScript<T> script = new DefaultRedisScript<>();
        script.setScriptText("redis.replicate_commands()" + luaScript);//加redis.replicate_commands()为了兼容redis 3.2.100版本不报错
        script.setResultType(tClass);
        return (T) redisTemplate.execute(script, keys, args);
    }

    /**
     * 有一个业务场景为，一个对象需要从2个维度（命名为a、b维度）去缓存，由于对象转成json串存储到redis较大，2个维度同时存储会占用大量内存空间，
     * 因此a维度选择使用一个普通key来存储json串（a维度的 key 使用 固定前缀 + a维度标识符），另外b维度使用hash的field存储b维度标识符，value存储普通key中的 a维度标识符，
     * 查询时通过hash的field找到value值，通过 a维度固定前缀 + value值 即可获得 a维度的完整key，使用完整key再查a维度即可获取对象。<br>
     * 通常a维度的普通key具有过期机制，过期后可能和b维度的数据不同步，为了保持b维度的数据 和 a维度一致，所以需要定期清理b维度中 a维度已经过期的数据。<br>
     * 为了避免lua脚本执行时间过长，导致redis服务阻塞，清理将分批进行，外部需使用循环调用本方法进行清理，此方法封装了hscan命令，
     * 每次调用本方法后，会返回一个游标，如果游标不为0，则继续调用本方法，直到游标为0。
     *
     * @param hashKey   需要清理的b维度的hashKey
     * @param keyPrefix a维度的key前缀
     * @param batchSize hscan命令每批次扫描的数量
     * @param cursor    游标，起始为0（小于0将从0开始），循环遍历时传入上一次调用返回的游标值
     * @return 新游标，当返回的游标为0，则表示清理完成
     */
    public Long clearHashByScanValue(String hashKey, String keyPrefix, int batchSize, long cursor) {
        String luaScript =
                "local hashKey = KEYS[1]         -- Hash的Key\n" +
                        "local keyPrefix = KEYS[2]       -- Key的前缀\n" +
                        "local batchSize = tonumber(ARGV[1]) -- 分批处理的field数量\n" +
                        "local cursor = tonumber(ARGV[2]) or 0 -- 初始化游标\n" +
                        "local scanResult = redis.call('HSCAN', hashKey, cursor, 'COUNT', batchSize)\n" +
                        "local fields = scanResult[2]\n" +
                        "local newCursor = tonumber(scanResult[1])\n" +
                        "if #fields > 0 then\n" +
                        "  local deleteFields = {}\n" +
                        "  for i = 1, #fields, 2 do\n" +
                        "    local field = fields[i]\n" +
                        "    local value = fields[i+1]\n" +
                        "    local fullKey = keyPrefix .. value\n" +
                        "    -- 检查Key是否存在\n" +
                        "    if redis.call('EXISTS', fullKey) == 0 then\n" +
                        "      table.insert(deleteFields, field);\n" +
                        "    end\n" +
                        "  end\n" +
                        "  if #deleteFields > 0 then\n" +
                        "    redis.call('HDEL', hashKey, unpack(deleteFields))\n" +
                        "  end\n" +
                        "end\n" +
                        "-- 返回新的游标值\n" +
                        "return newCursor";

        if (cursor < 0) {
            cursor = 0;
        }
        return execute(Long.class, luaScript, Arrays.asList(hashKey, keyPrefix), batchSize, cursor);
    }

    /**
     * 判断命令执行结果是否为OK
     *
     * @param result 命令执行结果
     * @return
     */
    private boolean isSuccess(String result) {
        return SUCCESS.equals(result);
    }

    /**
     * 判断命令执行结果是否为大于0
     *
     * @param result 命令执行结果
     * @return
     */
    private boolean isSuccess(Long result) {
        return result != null && result > 0;
    }

    /**
     * 对象数组转换字符串数组，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     *
     * @param item
     * @return
     */
    private String[] convertArray(Object[] item) {
        String[] items = null;
        if (item.length > 0) {
            items = new String[item.length];
            for (int i = 0; i < item.length; i++) {
                items[i] = PlatformUtils.toString(item[i]);
            }
        }

        return items;
    }

    /**
     * 对象List转换字符串数组，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     *
     * @param item
     * @return
     */
    private String[] convertArray(List<?> item) {
        String[] items = null;
        if (item != null && item.size() > 0) {
            items = new String[item.size()];
            for (int i = 0; i < item.size(); i++) {
                items[i] = PlatformUtils.toString(item.get(i));
            }
        }

        return items;
    }

    /**
     * map转换，注意：map参数指定的键和值如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     *
     * @param map
     * @return
     */
    private Map<String, String> convertMap(Map<?, ?> map) {
        Map<String, String> _map = new HashMap<>();
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();
            String key = PlatformUtils.toString(entry.getKey());
            String value = PlatformUtils.toString(entry.getValue());
            _map.put(key, value);
        }

        return _map;
    }


    // ===============================Key命令开始=================================

    /**
     * 返回指定的keys集合
     *
     * @param keys 键
     * @return
     */
    public Set<String> keys(String keys) {
        return stringRedisTemplate.keys(keys);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key     键
     * @param seconds 时间(秒)
     * @return
     */
    public boolean expire(String key, long seconds) {
        return stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取失效时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key          键
     * @param milliseconds 时间(毫秒)
     * @return
     */
    public boolean pExpire(String key, long milliseconds) {
        return stringRedisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * 根据key 获取失效时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getPExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    /**
     * 为一个存在的key设置过期时间，格式是uinx时间戳并精确到毫秒，例：<br>
     * 先执行pExpireAt name 1633190400000，不支持再执行 expireat name 1633190400 -- 把name键设置为2021-10-2 00:00:00到期(精确秒)
     *
     * @param key  键
     * @param date uinx时间戳并精确到毫秒
     * @return
     */
    public boolean expireAt(String key, Date date) {
        return stringRedisTemplate.expireAt(key, date);
    }

    /**
     * 清除当前有定时时间的键值，设置永不过期（和普通键值一样了），关闭后并不会删除已有的键值
     *
     * @param key 键
     * @return
     */
    public boolean persist(String key) {
        return stringRedisTemplate.persist(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 返回当前指定的key的类型
     *
     * @param key 键
     * @return 可返回的类型是: string,list,set,zset,hash和stream
     */
    public DataType getKeyType(String key) {
        return stringRedisTemplate.type(key);
    }

    /**
     * 修改key名称，存在原来则覆盖；如果修改key1为key2，key2存在，则key1覆盖key2的值
     *
     * @param oldKey 键
     * @param newKey 新键
     * @return
     */
    public void renameKey(String oldKey, String newKey) {
        stringRedisTemplate.rename(oldKey, newKey);
    }

    /**
     * 修改key名称存在则抛错；如果修改key1为key2，key2存在，则key1修改不成功
     *
     * @param oldKey 键
     * @param newKey 新键
     * @return 修改是否成功
     */
    public boolean renameIfAbsent(String oldKey, String newKey) {
        return stringRedisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 拷贝当前某一个key的值，存放到新的key中（可以跨库拷贝），例：<br>
     * copy name1 name2  -- 把 name1 的值 拷贝到 name2 里<br>
     * copy name1 name2 db 5 -- 把 name1 的值拷贝到第6号数据库name2里<br>
     * copy name1 name2 replace -- 把 name1 的值拷贝到name2里，存在则强行覆盖
     *
     * @param sourceKey 键
     * @param targetKey 新键
     * @param replace   新键如果存在，是否覆盖
     * @return
     */
    public void copyKey(String sourceKey, String targetKey, boolean replace) {
        stringRedisTemplate.executePipelined((RedisConnection connection) -> {
            // 获取源键的序列化值
            byte[] serializedSourceValue = connection.dump(sourceKey.getBytes(StandardCharsets.UTF_8));

            // 复制序列化值到目标键，设置过期时间为源键的剩余生存时间
            Long ttl = connection.ttl(sourceKey.getBytes(StandardCharsets.UTF_8));
            connection.restore(targetKey.getBytes(StandardCharsets.UTF_8), ttl, serializedSourceValue, replace);

            return null;
        });
    }

    /**
     * 把指定的键值移动到选定的数据库db当中。如果key在目标数据库中已存在，或者key在源数据库中不存，则key不会被移动。
     *
     * @param key   键
     * @param newDb 新库
     * @return 移动是否成功
     */
    public boolean moveKey(String key, Integer newDb) {
        return stringRedisTemplate.move(key, newDb);
    }

    /**
     * 修改指定key的最后访问时间。忽略不存在的 key。
     *
     * @param keys 可以传一个值 或多个
     */
    public void touch(String... keys) {
        byte[][] touchKeys = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            touchKeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }
        stringRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.touch(touchKeys);
            return null;
        });
    }

    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    public void delete(String... keys) {
        stringRedisTemplate.delete(Arrays.asList(keys));
    }

    /**
     * 删除缓存<br>
     * del和unlink区别：<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;del：它是线程阻塞的，当执行del命令是，del在没执行完时，其它后续的命令是无法进入的（要安全就使用del）<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;unlink:它不是线程阻塞的，当执行unlink命令时，它会将要删除的键移交给另外线程，然后将当前要删除的键与数据库空间断开连接<br>
     * 后续则由其它线程异步删除这些键（要效率快就使用unlink）
     *
     * @param keys 可以传一个值 或多个
     */
    public void unlink(String... keys) {
        stringRedisTemplate.unlink(Arrays.asList(keys));
    }

    // ===============================Key命令结束=================================

    // ===============================String命令开始==============================

    /**
     * 普通缓存获取，返回字符串数据，要返回特定类型请使用{@link #get(String, Type)}
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存获取，返回单对象（不包含List等）
     *
     * @param key         键
     * @param resultClass 值对象Class
     * @param <T>         值类型
     * @return 值
     */
    public <T> T get(String key, Class<T> resultClass) {
        return this.get(key, (Type) resultClass);
    }

    /**
     * 普通缓存获取，返回单对象（不包含List等）
     *
     * @param key         键
     * @param resultClass 值对象Class
     * @param <T>         值类型
     * @return 值
     */
    public <T> T get(String key, Type resultClass) {
        return PlatformUtils.getValueByClass(this.get(key), resultClass);
    }

    /**
     * 普通缓存获取，存数的数据为List的json格式，返回List
     *
     * @param key          键
     * @param elementClass List元素对象Class
     * @param <T>          List元素类型
     * @return 值
     */
    public <T> List<T> getList(String key, Class<T> elementClass) {
        return this.getList(key, (Type) elementClass);
    }

    /**
     * 普通缓存获取，存数的数据为List的json格式，返回List
     *
     * @param key          键
     * @param elementClass List元素对象Class
     * @param <T>          List元素类型
     * @return 值
     */
    public <T> List<T> getList(String key, Type elementClass) {
        return JsonUtils.toList(this.get(key), elementClass);
    }

    /**
     * 批量缓存获取，返回字符串集合，要返回特定类型请使用{@link #multiGet(String, Type)}
     *
     * @param keys 键
     * @return 值
     */
    public List<String> multiGet(String... keys) {
        return stringRedisTemplate.opsForValue().multiGet(Arrays.asList(keys));
    }

    /**
     * 批量缓存获取，返回elementClass集合
     *
     * @param key          键
     * @param elementClass 值对象Class
     * @param <T>          值类型
     * @return 值
     */
    public <T> List<T> multiGet(String key, Class<T> elementClass) {
        return this.multiGet(key, (Type) elementClass);
    }

    /**
     * 普通缓存获取，返回elementClass集合
     *
     * @param key          键
     * @param elementClass 值对象Class
     * @param <T>          值类型
     * @return 值
     */
    public <T> List<T> multiGet(String key, Type elementClass) {
        List<String> list = this.multiGet(key);
        return list.stream().map(el -> (T) PlatformUtils.getValueByClass(el, elementClass))
                .collect(Collectors.toList());
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        return execute(connection -> connection.set(key, PlatformUtils.toString(value)));
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        if (time > 0) {
            return execute(connection -> connection.setEx(key, time, PlatformUtils.toString(value)));
        } else {
            return this.set(key, value);
        }
    }

    /**
     * 普通缓存放入, 不存在放入，存在返回
     *
     * @param key   键
     * @param value 值，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return true成功 false失败
     */
    public boolean setIfAbsent(String key, Object value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, PlatformUtils.toString(value));
    }

    /**
     * 普通缓存放入并设置时间,不存在放入，存在返回
     *
     * @param key   键
     * @param value 值，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setIfAbsent(String key, Object value, long time) {
        return execute(connection -> connection.set(key, PlatformUtils.toString(value),
                Expiration.seconds(time), RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incrBy(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decrBy(String key, long delta) {
        if (delta <= 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().decrement(key, delta);
    }

    // ===============================String命令结束==============================

    // ===============================Hash命令开始================================

    /**
     * HashGet，返回字符串数据，要返回特定类型请使用{@link #hashGet(String, String, Type)}
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return 值
     */
    public String hashGet(String key, String hashKey) {
        return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * HashGet
     *
     * @param key         键 不能为null
     * @param hashKey     项 不能为null
     * @param resultClass 值对象Class
     * @param <T>         值类型
     * @return 值
     */
    public <T> T hashGet(String key, String hashKey, Class<T> resultClass) {
        return this.hashGet(key, hashKey, (Type) resultClass);
    }

    /**
     * HashGet
     *
     * @param key         键 不能为null
     * @param hashKey     项 不能为null
     * @param resultClass 值对象Class
     * @param <T>         值类型
     * @return 值
     */
    public <T> T hashGet(String key, String hashKey, Type resultClass) {
        return PlatformUtils.getValueByClass(this.hashGet(key, hashKey), resultClass);
    }

    /**
     * 获取hashKey对应的所有键值，返回Map的键、值都为字符串数据，要返回特定类型请使用{@link #hashGetAll(String, Class, Class)}
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, String> hashGetAll(String key) {
        return this.hashGetAll(key, String.class, String.class);
    }

    /**
     * 获取hashKey对应的所有键值，要求Map中所有key的数据类型一致，所有value的数据类型一致
     *
     * @param key        键
     * @param fieldClass 返回结果Map的key的Class
     * @param valueClass 返回结果Map的value的Class
     * @param <K>        返回结果Map的key的Class类型
     * @param <V>        返回结果Map的value的Class类型
     * @return 对应的多个键值
     */
    public <K, V> Map<K, V> hashGetAll(String key, Class<K> fieldClass, Class<V> valueClass) {
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        if (String.class.isAssignableFrom(fieldClass) && String.class.isAssignableFrom(valueClass)) {
            return (Map<K, V>) map;
        }
        if (map != null) {
            return map.entrySet().stream().collect(Collectors.toMap(
                    entry -> PlatformUtils.getValueByClass((String) entry.getKey(), fieldClass),
                    entry -> PlatformUtils.getValueByClass((String) entry.getValue(), valueClass)
            ));
        }
        return null;
    }

    /**
     * 通过hscan命令（避免hash数量过大对redis线程的长时间占用的问题），获取hashKey对应的所有键值，返回Map的键、值都为字符串数据，
     * 要返回特定类型请使用{@link #hashGetAll(String, Class, Class, long)}
     *
     * @param key       键
     * @param batchSize 每批次获取数量
     * @return 对应的多个键值
     */
    public Map<String, String> hashGetAll(String key, long batchSize) {
        return this.hashGetAll(key, String.class, String.class, batchSize);
    }

    /**
     * 通过hscan命令（避免hash数量过大对redis线程的长时间占用的问题），获取hashKey对应的所有键值，要求Map中所有key的数据类型一致，所有value的数据类型一致
     *
     * @param key        键
     * @param fieldClass 返回结果Map的key的Class
     * @param valueClass 返回结果Map的value的Class
     * @param <K>        返回结果Map的key的Class类型
     * @param <V>        返回结果Map的value的Class类型
     * @param batchSize  每批次获取数量
     * @return 对应的多个键值
     */
    public <K, V> Map<K, V> hashGetAll(String key, Class<K> fieldClass, Class<V> valueClass, long batchSize) {
        Map<K, V> result = new HashMap<>();
        ScanOptions options = ScanOptions.scanOptions()
                .match("*") // 根据需要调整匹配模式
                .count(batchSize) // 设置每次迭代返回的元素数量
                .build();

        Cursor<Map.Entry<Object, Object>> cursor = stringRedisTemplate.opsForHash().scan(key, options);

        try {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                result.put(PlatformUtils.getValueByClass((String) entry.getKey(), fieldClass),
                        PlatformUtils.getValueByClass((String) entry.getValue(), valueClass));
            }
        } finally {
            cursor.close();
        }

        return result;
    }

    /**
     * 通过hscan命令扫描指定key的hash，通过hash的value比较，获取hash对应的field
     *
     * @param key        hash的键
     * @param fieldClass hash的field的Class
     * @param value      待查询的hash的value，string等值匹配
     * @param batchSize  每批次获取数量
     * @param <K>        field的类型
     * @return field值
     */
    public <K> K hashScanFieldByValue(String key, Class<K> fieldClass, String value, long batchSize) {
        ScanOptions options = ScanOptions.scanOptions()
                .match("*") // 根据需要调整匹配模式
                .count(batchSize) // 设置每次迭代返回的元素数量
                .build();

        Cursor<Map.Entry<Object, Object>> cursor = stringRedisTemplate.opsForHash().scan(key, options);

        try {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                String _value = (String) entry.getValue();
                if (_value.equals(value)) {
                    return PlatformUtils.getValueByClass((String) entry.getKey(), fieldClass);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * 通过hscan命令扫描指定key的hash，通过hash的field比较，获取hash对应的value
     *
     * @param key        hash的键
     * @param valueClass hash的value的Class
     * @param field      待查询的hash的field，string等值匹配
     * @param batchSize  每批次获取数量
     * @param <K>        value的类型
     * @return value值
     */
    public <K> K hashScanValueByField(String key, Class<K> valueClass, String field, long batchSize) {
        ScanOptions options = ScanOptions.scanOptions()
                .match("*") // 根据需要调整匹配模式
                .count(batchSize) // 设置每次迭代返回的元素数量
                .build();

        Cursor<Map.Entry<Object, Object>> cursor = stringRedisTemplate.opsForHash().scan(key, options);

        try {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                String _field = (String) entry.getKey();
                if (_field.equals(field)) {
                    return PlatformUtils.getValueByClass((String) entry.getValue(), valueClass);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * 获取hashKey对应的hashKeys键值，返回List的元素都为字符串数据，要返回特定类型请使用{@link #hashMultiGet(String, Type, String...)}
     *
     * @param key      键
     * @param hashKeys hash键数组
     * @return 对应的多个键值
     */
    public List<String> hashMultiGet(String key, String... hashKeys) {
        return this.hashMultiGet(key, String.class, hashKeys);
    }

    /**
     * 获取hashKey对应的hashKeys键值，要求List中所有元素的数据类型一致
     *
     * @param key          键
     * @param elementClass 返回结果List的元素的Class
     * @param hashKeys     返回结果Map的value的Class类型
     * @param <T>          返回结果List的元素的Class类型
     * @return 对应的多个键值
     */
    public <T> List<T> hashMultiGet(String key, Class<T> elementClass, String... hashKeys) {
        return this.hashMultiGet(key, (Type) elementClass, hashKeys);
    }

    /**
     * 获取hashKey对应的items键值，要求List中所有元素的数据类型一致
     *
     * @param key          键
     * @param elementClass 返回结果List的元素的Class
     * @param hashKeys     返回结果Map的value的Class类型
     * @param <T>          返回结果List的元素的Class类型
     * @return 对应的多个键值
     */
    public <T> List<T> hashMultiGet(String key, Type elementClass, String... hashKeys) {
        List<Object> list = stringRedisTemplate.opsForHash().multiGet(key, Arrays.asList(hashKeys));
        if (list == null) {
            return null;
        }
        if (String.class == elementClass) {
            return (List<T>) list;
        }
        return list.stream().map(el -> (T) PlatformUtils.getValueByClass((String) el, elementClass))
                .collect(Collectors.toList());
    }

    /**
     * HashSet，注意：map参数指定的键和值如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     *
     * @param key 键
     * @param map 对应多个键值
     * @return
     */
    public void hashMultiSet(String key, Map<?, ?> map) {
        Map<String, String> _map = this.convertMap(map);
        stringRedisTemplate.opsForHash().putAll(key, _map);
    }

    /**
     * HashSet 并设置时间，注意：map参数指定的键和值如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return
     */
    public void hashMultiSet(String key, Map<?, ?> map, long time) {
        Map<String, String> _map = this.convertMap(map);
        execute(connection -> {
            connection.hMSet(key, _map);
            return connection.expire(key, time);
        });
    }


    /**
     * 向一张hash表中放入数据，如果不存在将创建
     *
     * @param key       键
     * @param hashKey   项
     * @param hashValue 值，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return
     */
    public void hashSet(String key, String hashKey, Object hashValue) {
        stringRedisTemplate.opsForHash().put(key, hashKey, PlatformUtils.toString(hashValue));
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建，并且对key设置过期时间
     *
     * @param key       键
     * @param hashKey   项
     * @param hashValue 值，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @param time      时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return
     */
    public boolean hashSet(String key, String hashKey, Object hashValue, long time) {
        return execute(connection -> {
            if (connection.hSet(key, hashKey, PlatformUtils.toString(hashValue))) {
                return connection.expire(key, time);
            }
            return false;
        });
    }

    /**
     * 删除hash表中的值
     *
     * @param key      键 不能为null
     * @param hashKeys 项 可以使多个，不能为null
     */
    public void hashDelete(String key, Object... hashKeys) {
        stringRedisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键，不能为null
     * @param hashKey 项，不能为null
     * @return true 存在，false不存在
     */
    public boolean hasHashKey(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * hash递增，如果不存在，就会创建一个 并 把新增后的值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param by      要增加几(大于0)
     * @return
     */
    public long hashIncrBy(String key, String hashKey, long by) {
        if (by <= 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForHash().increment(key, hashKey, by);
    }

    /**
     * hash递增，如果不存在，就会创建一个 并 把新增后的值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param by      要增加几(大于0)
     * @return
     */
    public double hashIncrBy(String key, String hashKey, double by) {
        if (by <= 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForHash().increment(key, hashKey, by);
    }

    /**
     * hash递减
     *
     * @param key     键
     * @param hashKey 项
     * @param by      要减少记(大于0，实际减去该值)
     * @return
     */
    public long hashDecrBy(String key, String hashKey, long by) {
        if (by <= 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForHash().increment(key, hashKey, -by);
    }

    /**
     * hash递减
     *
     * @param key     键
     * @param hashKey 项
     * @param by      要减少记(大于0，实际减去该值)
     * @return
     */
    public double hashDecrBy(String key, String hashKey, double by) {
        if (by <= 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForHash().increment(key, hashKey, -by);
    }

    /**
     * 获取hash长度
     *
     * @param key 键
     * @return
     */
    public Long hashSize(String key) {
        return stringRedisTemplate.opsForHash().size(key);
    }

    // ===============================Hash命令结束================================

    // ===============================Set命令开始=================================

    /**
     * 根据key获取Set中的所有值，返回字符串Set，要返回特定类型请使用{@link #setsMembers(String, Type)}
     *
     * @param key 键
     * @return
     */
    public Set<String> setsMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key          键
     * @param elementClass Set元素值对象Class
     * @param <T>          Set元素值对象类型
     * @return
     */
    public <T> Set<T> setsMembers(String key, Class<T> elementClass) {
        return this.setsMembers(key, (Type) elementClass);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key          键
     * @param elementClass Set元素值对象Class
     * @param <T>          Set元素值对象类型
     * @return
     */
    public <T> Set<T> setsMembers(String key, Type elementClass) {
        Set<String> members = this.setsMembers(key);
        return members.stream()
                .map(el -> (T) PlatformUtils.getValueByClass(el, elementClass))
                .collect(Collectors.toSet());
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean setsContains(String key, Object value) {
        return stringRedisTemplate.opsForSet().isMember(key, PlatformUtils.toString(value));
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值，可以是多个，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 成功个数
     */
    public long setsAdd(String key, Object... values) {
        String[] finalItems = this.convertArray(values);
        return stringRedisTemplate.opsForSet().add(key, finalItems);
    }

    /**
     * 将set数据放入缓存，并设置过期时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值，可以是多个，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 成功个数
     */
    public long setsAdd(String key, long time, Object... values) {
        String[] finalItems = this.convertArray(values);

        return execute(connection -> {
            Long count = connection.sAdd(key, finalItems);
            if (count > 0 && time > 0) {
                connection.expire(key, time);
            }
            return count;
        });
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long setsSize(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值，可以是多个，如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 移除的个数
     */
    public long setsRemove(String key, Object... values) {
        String[] finalItems = this.convertArray(values);
        return stringRedisTemplate.opsForSet().remove(key, finalItems);
    }

    // ===============================Set命令结束=================================

    // ===============================Zset命令开始================================

    /**
     * 向有序集合中添加成员
     *
     * @param key   键
     * @param score 分数
     * @param value 成员
     * @return
     */
    public boolean zSetAdd(String key, double score, String value) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 向有序集合中的成员分数添加增量
     *
     * @param key       键
     * @param increment 分数增量
     * @param value     成员
     * @return 返回添加增量后的分数
     */
    public double zSetIncrBy(String key, double increment, String value) {
        return stringRedisTemplate.opsForZSet().incrementScore(key, value, increment);
    }

    /**
     * 获取有序集合中的成员分数在min和max之间的数量
     *
     * @param key 键
     * @param min 分数下限
     * @param max 分数上限
     * @return 返回分数在min和max之间的数量
     */
    public long zSetCount(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取有序集合中的成员按分数从小到大排序后下标在start和end之间的成员
     *
     * @param key   键
     * @param start 下标起始位置
     * @param end   下标结束位置，0 到 -1 代表所有值
     * @return 返回指定区间内的有序集成员的列表
     */
    public Set<String> zSetRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
     *
     * @param key 键
     * @param min 分数下限
     * @param max 分数上限
     * @return
     */
    public Set<String> zSetRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
     *
     * @param key    键
     * @param min    分数下限
     * @param max    分数上限
     * @param offset 分页起始位置
     * @param count  分页的单页条数
     * @return
     */
    public Set<String> zSetRangeByScore(String key, double min, double max, int offset, int count) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 获取有序集合中的成员按分数从大到小排序后下标在start和end之间的成员
     *
     * @param key   键
     * @param start 下标起始位置
     * @param end   下标结束位置，0 到 -1 代表所有值
     * @return
     */
    public Set<String> zSetReverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
     *
     * @param key 键
     * @param min 分数下限
     * @param max 分数上限
     * @return
     */
    public Set<String> zSetReverseRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
     *
     * @param key    键
     * @param min    分数下限
     * @param max    分数上限
     * @param offset 分页起始位置
     * @param count  分页的单页条数
     * @return
     */
    public Set<String> zSetReverseRangeByScore(String key, double min, double max, int offset, int count) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回有序集 key 中成员 value 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
     *
     * @param key   键
     * @param value 成员
     * @return
     */
    public long zSetRank(String key, String value) {
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回有序集 key 中成员 value 的排名。其中有序集成员按 score 值递减(从大到小)排序。
     * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
     *
     * @param key   键
     * @param value 成员
     * @return
     */
    public long zSetReverseRank(String key, String value) {
        return stringRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 从有序集合中移除多个成员
     *
     * @param key     键
     * @param members 成员
     * @return
     */
    public boolean zSetRemove(String key, String... members) {
        long result = stringRedisTemplate.opsForZSet().remove(key, members);
        return this.isSuccess(result);
    }

    // ===============================Zset命令结束================================

    // ===============================List命令开始================================

    /**
     * 获取list缓存的指定范围内容，返回字符串List，要返回特定类型请使用{@link #listRange(String, long, long, Type)}
     *
     * @param key   键
     * @param start 开始
     * @param end   结束，0 到 -1代表所有值
     * @return
     */
    public List<String> listRange(String key, long start, long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的指定范围内容
     *
     * @param key          键
     * @param start        开始
     * @param end          结束 0 到 -1代表所有值
     * @param elementClass List元素值对象Class
     * @param <T>          List元素值对象类型
     * @return
     */
    public <T> List<T> listRange(String key, long start, long end, Class<T> elementClass) {
        return this.listRange(key, start, end, (Type) elementClass);
    }

    /**
     * 获取list缓存的指定范围内容
     *
     * @param key          键
     * @param start        开始
     * @param end          结束 0 到 -1代表所有值
     * @param elementClass List元素值对象Class
     * @param <T>          List元素值对象类型
     * @return
     */
    public <T> List<T> listRange(String key, long start, long end, Type elementClass) {
        return this.listRange(key, start, end).stream()
                .map(el -> (T) PlatformUtils.getValueByClass(el, elementClass))
                .collect(Collectors.toList());
    }

    /**
     * 获取list缓存的所有内容，返回字符串List，要返回特定类型请使用{@link #listGetAll(String, Type)}
     *
     * @param key 键
     * @return
     */
    public List<String> listGetAll(String key) {
        return execute(connection -> {
            long length = connection.lLen(key);
            return connection.lRange(key, 0l, length);
        });
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key          键
     * @param elementClass List元素值对象Class
     * @param <T>          List元素值对象类型
     * @return
     */
    public <T> List<T> listGetAll(String key, Class<T> elementClass) {
        return this.listGetAll(key, (Type) elementClass);
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key          键
     * @param elementClass List元素值对象Class
     * @param <T>          List元素值对象类型
     * @return
     */
    public <T> List<T> listGetAll(String key, Type elementClass) {
        return this.listGetAll(key).stream()
                .map(el -> (T) PlatformUtils.getValueByClass(el, elementClass))
                .collect(Collectors.toList());
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long listSize(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引 获取list中的值，返回字符串，要返回特定类型请使用{@link #listIndexOf(String, long, Type)}
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public String listIndexOf(String key, long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key         键
     * @param index       索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @param resultClass 值对象Class
     * @param <T>         值对象类型
     * @return
     */
    public <T> T listIndexOf(String key, long index, Class<T> resultClass) {
        return this.listIndexOf(key, index, (Type) resultClass);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key         键
     * @param index       索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @param resultClass 值对象Class
     * @param <T>         值对象类型
     * @return
     */
    public <T> T listIndexOf(String key, long index, Type resultClass) {
        String value = this.listIndexOf(key, index);
        return PlatformUtils.getValueByClass(value, resultClass);
    }

    /**
     * 将list放入缓存的头部
     *
     * @param key   键
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 返回插入后的list长度
     */
    public long listLeftPush(String key, Object value) {
        return stringRedisTemplate.opsForList().leftPush(key, PlatformUtils.toString(value));
    }

    /**
     * 把element元素插入到指定集合key里，但是还要以pivot内部的一个元素为基准，插到这个元素的左边<br>
     * 注：当集合key不存在时，这个list会被看作是空list，什么都不执行<br>
     * 注：当集合key存在，值不是列表类型时，返回错误<br>
     * 注：当给定的参考元素pivot不存在是则返回-1，因为程序不知道往哪插入<br>
     * 例：linsert listString before Romanti niubi -- 把niubi插入到listString集合里，插入参考Romanti元素的前面
     *
     * @param key   键
     * @param pivot 集合里的参考元素
     * @param value 待插入的元素，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 返回插入后list的长度，如果未找到pivot元素，则返回-1
     */
    public long listLeftPush(String key, Object pivot, Object value) {
        return stringRedisTemplate.opsForList().leftPush(key, PlatformUtils.toString(pivot), PlatformUtils.toString(value));
    }

    /**
     * 将list放入缓存的头部
     *
     * @param key   键
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @param time  时间(秒)
     * @return 返回插入后的list长度
     */
    public long listLeftPush(String key, Object value, long time) {
        return execute(connection -> {
            Long length = connection.lPush(key, PlatformUtils.toString(value));
            if (length > 0 && time > 0) {
                connection.expire(key, time);
            }
            return length;
        });
    }

    /**
     * 将list放入缓存的头部
     *
     * @param key    键
     * @param values 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 返回插入后的list长度
     */
    public long listLeftPush(String key, List<Object> values) {
        String[] finalItems = this.convertArray(values);
        return execute(connection -> connection.lPush(key, finalItems));
    }

    /**
     * 将list放入缓存的头部
     *
     * @param key    键
     * @param values 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @param time   时间(秒)
     * @return 返回插入后的list长度
     */
    public long listLeftPush(String key, List<?> values, long time) {
        String[] finalItems = this.convertArray(values);
        return execute(connection -> {
            Long length = connection.lPush(key, finalItems);
            if (length > 0 && time > 0) {
                connection.expire(key, time);
            }
            return length;
        });
    }

    /**
     * 从list集合左边（头部）弹出（获取并删除）1个元素
     *
     * @param key 键
     * @return
     */
    public String listLeftPop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从list集合左边（头部）弹出（获取并删除）1个元素
     *
     * @param key 键
     * @return
     */
    public <T> T listLeftPop(String key, Class<T> elementClass) {
        return this.listLeftPop(key, (Type) elementClass);
    }

    /**
     * 从list集合左边（头部）弹出（获取并删除）1个元素
     *
     * @param key 键
     * @return
     */
    public <T> T listLeftPop(String key, Type elementClass) {
        return PlatformUtils.getValueByClass(this.listLeftPop(key), elementClass);
    }

    /**
     * 将list放入缓存的尾部
     *
     * @param key   键
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 返回插入后的list长度
     */
    public long listRightPush(String key, Object value) {
        return stringRedisTemplate.opsForList().rightPush(key, PlatformUtils.toString(value));
    }

    /**
     * 把element元素插入到指定集合key里，但是还要以pivot内部的一个元素为基准，插到这个元素的右边<br>
     * 注：当集合key不存在时，这个list会被看作是空list，什么都不执行<br>
     * 注：当集合key存在，值不是列表类型时，返回错误<br>
     * 注：当给定的参考元素pivot不存在是则返回-1，因为程序不知道往哪插入<br>
     * 例：linsert listString after Romanti niubi -- 把niubi插入到listString集合里，插入参考Romanti元素的后面
     *
     * @param key   键
     * @param pivot 集合里的参考元素
     * @param value 待插入的元素，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 返回插入后list的长度，如果未找到pivot元素，则返回-1
     */
    public long listRightPush(String key, Object pivot, Object value) {
        return stringRedisTemplate.opsForList().rightPush(key, PlatformUtils.toString(pivot), PlatformUtils.toString(value));
    }

    /**
     * 将list放入缓存的尾部
     *
     * @param key   键
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @param time  时间(秒)
     * @return 返回插入后的list长度
     */
    public long listRightPush(String key, Object value, long time) {
        return execute(connection -> {
            Long length = connection.rPush(key, PlatformUtils.toString(value));
            if (length > 0 && time > 0) {
                connection.expire(key, time);
            }
            return length;
        });
    }

    /**
     * 将list放入缓存的尾部
     *
     * @param key   键
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 返回插入后的list长度
     */
    public long listRightPush(String key, List<Object> value) {
        String[] finalItems = this.convertArray(value);
        return execute(connection -> connection.rPush(key, finalItems));
    }

    /**
     * 将list放入缓存的尾部
     *
     * @param key   键
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @param time  时间(秒)
     * @return 返回插入后的list长度
     */
    public long listRightPush(String key, List<?> value, long time) {
        String[] finalItems = this.convertArray(value);
        return execute(connection -> {
            Long length = connection.rPush(key, finalItems);
            if (length > 0 && time > 0) {
                connection.expire(key, time);
            }
            return length;
        });
    }

    /**
     * 从list集合左边（尾部）弹出（获取并删除）1个元素
     *
     * @param key 键
     * @return
     */
    public String listRightPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从list集合左边（尾部）弹出（获取并删除）1个元素
     *
     * @param key 键
     * @return
     */
    public <T> T listRightPop(String key, Class<T> elementClass) {
        return this.listRightPop(key, (Type) elementClass);
    }

    /**
     * 从list集合左边（尾部）弹出（获取并删除）1个元素
     *
     * @param key 键
     * @return
     */
    public <T> T listRightPop(String key, Type elementClass) {
        return PlatformUtils.getValueByClass(this.listRightPop(key), elementClass);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return
     */
    public void listUpdateByIndex(String key, long index, Object value) {
        stringRedisTemplate.opsForList().set(key, index, PlatformUtils.toString(value));
    }

    /**
     * 从list移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值，List元素如果不是String类型、基础类型、Date（转成yyyy-MM-dd hh:mm:ss格式）、枚举（转成name()），都将调用转换成json进行存储
     * @return 移除的个数
     */
    public long listRemove(String key, long count, Object value) {
        return stringRedisTemplate.opsForList().remove(key, count, PlatformUtils.toString(value));
    }

    // ===============================List命令结束================================

    // ===============================BitMap命令开始==============================

    /**
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
     * 位的设置或清除取决于 value 参数，可以是 0 也可以是 1 。
     *
     * @param key    键
     * @param offset 偏移量
     * @param value  设置偏移量上的值，true：1，false：0
     * @return 指定偏移量原来储存的位
     */
    public boolean setBit(String key, long offset, boolean value) {
        return stringRedisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
     * 当 offset 比字符串值的长度大，或者 key 不存在时，返回 0（false）
     *
     * @param key    键
     * @param offset 偏移量
     * @return 字符串值指定偏移量上的位(bit)
     */
    public boolean getBit(String key, long offset) {
        return stringRedisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 计算给定字符串中，被设置为 1 的比特位的数量。
     * 一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。
     * start 和 end 参数的设置和 GETRANGE key start end 命令类似，都可以使用负数值： 比如 -1 表示最后一个字节， -2 表示倒数第二个字节，以此类推。
     * 不存在的 key 被当成是空字符串来处理，因此对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 。
     *
     * @param key 键
     * @return 被设置为 1 的位的数量
     */
    public long bitCount(String key) {
        return execute(connection -> connection.bitCount(key.getBytes()));
    }

    /**
     * 计算给定字符串中，被设置为 1 的比特位的数量。
     * 一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。
     * start 和 end 参数的设置和 GETRANGE key start end 命令类似，都可以使用负数值： 比如 -1 表示最后一个字节， -2 表示倒数第二个字节，以此类推。
     * 不存在的 key 被当成是空字符串来处理，因此对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 。
     *
     * @param key   键
     * @param start 计数范围的起始位置
     * @param end   计数范围的结束位置
     * @return 被设置为 1 的位的数量
     */
    public long bitCount(String key, long start, long end) {
        return execute(connection -> connection.bitCount(key.getBytes(), start, end));
    }

    /**
     * 计算给定字符串中，被设置为 1 的比特位的位置索引集合
     * 一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。
     * start 和 end 参数的设置和 GETRANGE key start end 命令类似，都可以使用负数值： 比如 -1 表示最后一个字节， -2 表示倒数第二个字节，以此类推。
     * 不存在的 key 被当成是空字符串来处理，因此对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 。
     *
     * @param key 键
     * @return 被设置为 1 的位的位置索引集合
     */
    public List<Long> bitCountPosIndex(String key) {
        return execute(connection -> {
            Long count = connection.bitCount(key.getBytes());
            List<Long> positions = new ArrayList<>();
            Long position = 0l;
            for (int i = 0; i < count; i++) {
                //每次只返回第一个位置，遍历的方式从下一个位置到结束位置获取bit位为1的位置
                //如果count数量大，那执行命令太多，可能会有性能问题
                Range<Long> range = Range.from(Range.Bound.inclusive(position)).to(Range.Bound.inclusive(-1l));
                position = connection.bitPos(key.getBytes(), true, range);
                positions.add(position);
                position++;
            }
            return positions;
        });
    }

    /**
     * 返回位图中第一个值为 bit 的二进制位的位置。
     * 在默认情况下， 命令将检测整个位图， 但用户也可以通过可选的 start 参数和 end 参数指定要检测的范围。
     *
     * @param key   键
     * @param value 查找的二进制位值，true：1，false：0
     * @return 二进制位的位置
     */
    public long bitPos(String key, boolean value) {
        return execute(connection -> connection.bitPos(key.getBytes(), value));
    }

    /**
     * 返回位图中第一个值为 bit 的二进制位的位置。
     * 在默认情况下， 命令将检测整个位图， 但用户也可以通过可选的 start 参数和 end 参数指定要检测的范围。
     *
     * @param key   键
     * @param value 查找的二进制位值，true：1，false：0
     * @param start 起始位置
     * @param end   结束位置
     * @return 二进制位的位置
     */
    public long bitPos(String key, boolean value, long start, long end) {
        return execute(connection -> {
            Range<Long> range = Range.from(Range.Bound.inclusive(start)).to(Range.Bound.inclusive(end));
            return connection.bitPos(key.getBytes(), value, range);
        });
    }

    /**
     * 对一个或多个保存二进制位的字符串 key 进行位元操作，并将结果保存到 destKey 上。
     * operation 可以是 AND 、 OR 、 NOT 、 XOR 这四种操作中的任意一种：
     * <ul>
     * <li>BITOP AND destkey key [key ...] ，对一个或多个 key 求逻辑并，并将结果保存到 destkey 。</li>
     * <li>BITOP OR destkey key [key ...] ，对一个或多个 key 求逻辑或，并将结果保存到 destkey 。</li>
     * <li>BITOP XOR destkey key [key ...] ，对一个或多个 key 求逻辑异或，并将结果保存到 destkey 。</li>
     * <li>BITOP NOT destkey key ，对给定 key 求逻辑非，并将结果保存到 destkey 。</li>
     * </ul>
     * 除了 NOT 操作之外，其他操作都可以接受一个或多个 key 作为输入。
     *
     * @param op      操作
     * @param destKey 存数计算结果的key
     * @param srcKeys 提取计算参数的key
     * @return 保存到 destkey 的字符串的长度，和输入 key 中最长的字符串长度相等
     */
    public long bitOp(RedisStringCommands.BitOperation op, String destKey, String... srcKeys) {
        byte[][] srcKeysBytes = new byte[srcKeys.length][];
        for (int i = 0; i < srcKeys.length; i++) {
            srcKeysBytes[i] = srcKeys[i].getBytes();
        }
        return execute(connection -> connection.bitOp(op, destKey.getBytes(), srcKeysBytes));
    }
    // ===============================BitMap命令结束==============================


}
