package com.jing.msc.cobweb.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.entity.sys.Dict;
import com.jing.msc.cobweb.entity.sys.DictItem;
import com.jing.msc.cobweb.mapper.sys.DictMapper;
import com.jing.msc.cobweb.service.sys.DictService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 * @author : jing
 * @since : 2024-08-21 10:48:22
 */
@Service(value = "dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<DictItem> getDictItemList(String dictCode) {
        Assert.hasText(dictCode, "字典编码不能为空");
        List<DictItem> dictItems = baseMapper.selectDictItemByCode(dictCode);
        if (CollectionUtils.isNotEmpty(dictItems)) {
            return dictItems;
        }
        return Collections.emptyList();
    }


}
