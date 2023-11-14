package com.jing.common.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.config
 * @date : 2021/4/25 14:11
 * @description :
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    protected final Logger logger = LoggerFactory.getLogger(CustomMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "creatorId", Long.class, -1L);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "reviserId", Long.class, -1L);
        this.strictInsertFill(metaObject, "revisionTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "logicFlag", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 起始版本 3.3.3(推荐)
        this.strictUpdateFill(metaObject, "reviserId", Long.class, -1L);
        this.strictUpdateFill(metaObject, "revisionTime", LocalDateTime.class, LocalDateTime.now());
    }

}
