package com.jing.msc.cobweb.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.entity.sys.DictItem;
import com.jing.msc.cobweb.mapper.sys.DictItemMapper;
import com.jing.msc.cobweb.service.sys.DictItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 * @author : jing
 * @since : 2024-08-21 10:48:22
 */
@Service(value = "dictItemService")
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

}
