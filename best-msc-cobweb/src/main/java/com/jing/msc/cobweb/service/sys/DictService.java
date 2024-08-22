package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.Dict;
import com.jing.msc.cobweb.entity.sys.DictItem;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author : jing
 * @since : 2024-08-21 10:48:22
 */
public interface DictService extends IService<Dict> {


    /**
     * 通过字典编码获取字典项列表
     *
     * @param dictCode 字典编码
     * @return {@link List }<{@link DictItem }>
     */
    List<DictItem> getDictItemList(String dictCode);


}
