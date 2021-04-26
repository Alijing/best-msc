package com.jing.msc.cobweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.dao.SpiderMapper;
import com.jing.msc.cobweb.entity.Spider;
import com.jing.msc.cobweb.service.SpiderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.service.impl
 * @date : 2021/4/23 14:59
 * @description :
 */
@Service(value = "spiderService")
@Transactional(rollbackFor = Exception.class)
public class SpiderServiceImpl extends ServiceImpl<SpiderMapper, Spider> implements SpiderService {


}
