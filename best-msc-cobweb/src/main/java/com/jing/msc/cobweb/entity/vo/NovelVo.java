package com.jing.msc.cobweb.entity.vo;

import com.jing.common.core.base.BasePage;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.vo
 * @date : 2022/5/31 18:11
 * @description :
 */
public class NovelVo extends BasePage {

    /**
     * 小说名称
     */
    private String name;

    /**
     * 批量操作时选择的 id
     */
    private List<Long> batchId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getBatchId() {
        return batchId;
    }

    public void setBatchIds(List<Long> batchId) {
        this.batchId = batchId;
    }
}
