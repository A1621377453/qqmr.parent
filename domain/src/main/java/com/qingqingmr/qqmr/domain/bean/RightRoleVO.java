package com.qingqingmr.qqmr.domain.bean;

import com.qingqingmr.qqmr.domain.entity.RightRole;

/**
 * 后台角色列表
 * @author ztl
 * @datetime 2018-07-06 13:59:24
 */
public class RightRoleVO extends RightRole {
    /**
     * 每个角色对应的人数
     */
    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
