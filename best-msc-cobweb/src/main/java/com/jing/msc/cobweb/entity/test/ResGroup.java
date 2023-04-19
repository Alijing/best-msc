package com.jing.msc.cobweb.entity.test;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Objects;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.test
 * @date : 2023/4/19 10:10
 * @description :
 */
public class ResGroup {

    /**
     * 强制读取第三个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    @ExcelProperty(index = 0)
    private String name;
    /**
     * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
     */
    @ExcelProperty("上级资源组")
    private String parentName;

    @ExcelProperty(index = 6)
    private String code;

    public ResGroup() {
    }

    public ResGroup(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResGroup resGroup = (ResGroup) o;
        return Objects.equals(name, resGroup.name) && Objects.equals(parentName, resGroup.parentName) && Objects.equals(code, resGroup.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentName, code);
    }
}
