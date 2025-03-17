package com.jing.msc.cobweb.util;

import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.JsonUtils;
import com.jing.msc.cobweb.entity.socket.InfoText;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装WebSocketUtil工具类，用于提供对session链接、断开连接、推送消息的简单控制。
 *
 * @author : jing
 * @since : 2025/3/10 11:05
 */
public class WebsocketUtil {
    /**
     * 记录当前在线的Session
     */
    private static final Map<String, Session> ONLINE_SESSION = new ConcurrentHashMap<>();

    /**
     * 记录每个用户的通知队列 太多会连接会断掉所以只记录最后一次的通知信息
     */
    private static final Map<String, InfoText> ONLINE_INFO = new ConcurrentHashMap<>();

    /**
     * 最近通知
     */
    private static final LinkedList<InfoText> LAST_INFO_TEXTS = new LinkedList<>();

    /**
     * 最大通知保留数量
     */
    private static final int MAX_INFO_AMOUNT = 5;

    /**
     * 用于控制同步代码块
     */
    private static final Object SYN = new Object();

    /**
     * 添加最近的一条通知
     *
     * @param infoText 通知信息
     */
    public static void addLastInfoText(InfoText infoText) {
        synchronized (SYN) {
            //最新的永远在第一条
            LAST_INFO_TEXTS.addFirst(infoText);
            //删除的永远是最后一条
            if (LAST_INFO_TEXTS.size() > MAX_INFO_AMOUNT) {
                LAST_INFO_TEXTS.removeLast();
            }
        }
    }

    /**
     * 获取最近通知
     *
     * @return 通知信息
     */
    public static LinkedList<InfoText> getLastInfoText() {
        //实际集合并不返回
        return new LinkedList<>(LAST_INFO_TEXTS);
    }

    /**
     * 添加session
     *
     * @param account 操作员账号
     * @param session 操作员的session
     */
    public static void addSession(String account, Session session) {
        // 此处只允许一个用户的session链接。一个用户的多个连接，我们视为无效。
        ONLINE_SESSION.putIfAbsent(account, session);
    }

    /**
     * 关闭session
     *
     * @param account 操作员账号
     */
    public static void removeSession(String account) {
        Session session = ONLINE_SESSION.get(account);
        if (session == null) {
            return;
        }
        try (Session s = session) {
            ONLINE_SESSION.remove(account);
        } catch (Exception e) {
            throw new CustomException(ResultEnum.WEBSOCKET_SESSION_ERROR);
        }
    }

    /**
     * 为用户添加消息infoText
     *
     * @param account  用户账号
     * @param infoText 发送给用户的信息
     */
    public static void addInfoList(String account, InfoText infoText) {
        //只记录最后一次通知
        ONLINE_INFO.put(account, infoText);
    }

    /**
     * 删除指定用户的指定信息队列 如果用户不存在需要发送的队列了  就删除用户
     *
     * @param account 用户账号
     */
    public static void removeInfoList(String account) {
        ONLINE_INFO.remove(account);
    }

    /**
     * 根据session给单个用户推送消息   这个方法的使用主要还是在处理类中
     *
     * @param session  操作员的session
     * @param infoText 发送人的信息
     */
    public static void sendMessage(Session session, InfoText infoText) {
        if (session == null) {
            return;
        }
        //判断当前连接是否打开
        if (session.isOpen()) {
            // 异步
            RemoteEndpoint.Async async = session.getAsyncRemote();
            async.sendText(JsonUtils.toJson(infoText));
        }
    }

    /**
     * 根据用户ID给单个用户推送消息   建立此方法允许其它操作给指定用户发送信息
     *
     * @param account  指定用户账号
     * @param infoText 发送人的信息
     * @param isLine   此人不在线时是否存入消息列表
     * @return 是否成功发送
     */
    public static Boolean sendMessage(String account, InfoText infoText, int isLine) {
        Session session = ONLINE_SESSION.get(account);
        if (session == null) {
            //如果要给所有人发 且此人不在线则存入此人的消息列表中在线时再显示
            if (isLine == 0) {
                addInfoList(account, infoText);
            }
            return false;
        }
        if (session.isOpen()) {
            infoText.removeUnread(account);
            // 异步
            RemoteEndpoint.Async async = session.getAsyncRemote();
            async.sendText(JsonUtils.toJson(infoText));
            return true;
        }
        return false;
    }

    /**
     * 向除了自己以外的所有在线人发送消息
     *
     * @param infoText 发送人的信息
     */
    public static void sendMessageForAllExceptOneself(InfoText infoText) {
        String operationId = infoText.getOperatorId();
        for (String key : ONLINE_SESSION.keySet()) {
            if (key.equals(operationId)) {
                continue;
            }
            infoText.addRead(key);
            sendMessage(ONLINE_SESSION.get(key), infoText);
        }
    }

    /**
     * 向所有在线人发送消息
     *
     * @param infoText 发送人的信息
     */
    public static void sendMessageForAll(InfoText infoText) {
        //jdk8 新方法
        ONLINE_SESSION.forEach((sessionId, session) -> sendMessage(session, infoText));
    }

    /**
     * 发送指定用户的未读消息
     *
     * @param account 用户账号
     */
    public static void unreadInfo(String account) {
        if (!ONLINE_INFO.containsKey(account)) {
            return;
        }
        InfoText infoText = ONLINE_INFO.get(account);
        LocalDateTime now = LocalDateTime.now();
        //在开始时间之后最晚接收时间之前 则进行推送
        if (now.isAfter(infoText.getSendTime()) && now.isBefore(infoText.getLatestReceiveTime())) {
            Boolean isSend = sendMessage(account, infoText, 0);
            //发送成功后移除
            if (isSend)
                removeInfoList(account);
        } else {//如果最晚接收时间已过且超过三天则消除这条通知
            Period period = Period.between(now.toLocalDate(), infoText.getLatestReceiveTime().toLocalDate());
            if (period.getDays() >= 3) {
                removeInfoList(account);
            }
        }
    }

}
