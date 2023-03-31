package com.jing.msc.cobweb.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.entity
 * @date : 2021/11/12 10:07
 * @description :
 */
@TableName("novel_info")
public class Novel implements Serializable {
    private static final long serialVersionUID = 8964955463358958822L;
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 小说名称
     */
    private String name;
    /**
     * 小说地址
     */
    private String path;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifyTime;


    public Novel() {
    }


    public Novel(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
