package com.qingqingmr.qqmr.domain.entity;

/**
 * 系统权限表
 *
 * @author crn
 * @datetime 2018-7-3 19:17:54
 */
public class Right {
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 权限描述
     */
    private String description;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}