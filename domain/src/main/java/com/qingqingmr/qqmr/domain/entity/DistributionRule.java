package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分销规则设置
 *
 * @author ztl
 * @datetime 2018-7-3 19:15:49
 */
public class DistributionRule {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 角色：1会员，2美容师，3顾问
     */
    private Integer role;
    /**
     * 类型：1直推奖，2合作奖，3一级奖，4二级奖，5人头奖，6双人奖(枚举）
     */
    private Integer type;
    /**
     * 分销规则
     */
    private String rule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }

    /**
     * 分销规则角色枚举
     */
    public enum DistriRoleEnum {
        MEMBER(1, "会员"),
        BEAUTICIAN(2, "美容师"),
        ADVISER(3, "顾问");

        public int code;
        public String value;

        private DistriRoleEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static DistriRoleEnum getEnum(int code) {
            DistriRoleEnum[] distriRoleEnum = DistriRoleEnum.values();
            for (DistriRoleEnum distriRole : distriRoleEnum) {
                if (distriRole.code == code) {
                    return distriRole;
                }
            }

            return null;
        }
    }
}