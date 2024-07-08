package com.jing.msc.cobweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.mapper.NovelContentMapper;
import com.jing.msc.cobweb.entity.NovelContent;
import com.jing.msc.cobweb.service.NovelContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service.impl
 * @date : 2021/11/12 10:26
 * @description :
 */
@Service("novelContentService")
@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
public class NovelContentServiceImpl extends ServiceImpl<NovelContentMapper, NovelContent> implements NovelContentService {


}
