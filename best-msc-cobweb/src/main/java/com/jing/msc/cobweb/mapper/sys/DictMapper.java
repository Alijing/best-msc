package com.jing.msc.cobweb.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.cobweb.entity.sys.Dict;
import com.jing.msc.cobweb.entity.sys.DictItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author : jing
 * @since : 2024-08-21 10:48:22
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 通过字典编码获取字典项列表
     *
     * @param dictCode 字典编码
     * @return {@link List }<{@link DictItem }>
     */
    List<DictItem> selectDictItemByCode(@Param("code") String dictCode);


}
