package com.qingqingmr.qqmr.domain.entity;

/**
 * 系统权限模块
 *
 * @author crn
 * @datetime 2018-7-3 19:17:54
 */
public class RightModule {
    private Long id;

    /**
     * 模块名称
     */
    private String name;

    /**
     * 模块描述
     */
    private String description;

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
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}