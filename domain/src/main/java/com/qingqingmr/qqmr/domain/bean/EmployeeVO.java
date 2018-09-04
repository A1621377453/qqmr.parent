package com.qingqingmr.qqmr.domain.bean;

import com.qingqingmr.qqmr.domain.entity.Employee;

/**
 * 员工业务实体
 *
 * @author crn
 * @datetime 2018-07-05 13:57:01
 */
public class EmployeeVO extends Employee {
    /**
     * 性别：未知，男，女
     */
    private String sexStr;

    /**
     * 角色类型：表示美容师，表示顾问
     */
    private String roleTypeStr;

    /**
     * 出生日期字符串
     */
    private String birthDateStr;

    /**
     * 入职日期字符串
     */
    private String entryDateStr;

    /**
     * 所属顾问真实姓名
     */
    private String belongCounselor;

    /**
     * 所属门店名称
     */
    private String storeName;
    /**
     * 门店区域名称
     */
    private String areaName;

    /**
     * 该美容师对应的userId
     */
    private Long userId;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBelongCounselor() {
        return belongCounselor;
    }

    public void setBelongCounselor(String belongCounselor) {
        this.belongCounselor = belongCounselor;
    }

    public String getBirthDateStr() {
        return birthDateStr;
    }

    public void setBirthDateStr(String birthDateStr) {
        this.birthDateStr = birthDateStr;
    }

    public String getEntryDateStr() {
        return entryDateStr;
    }

    public void setEntryDateStr(String entryDateStr) {
        this.entryDateStr = entryDateStr;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public String getRoleTypeStr() {
        return roleTypeStr;
    }

    public void setRoleTypeStr(String roleTypeStr) {
        this.roleTypeStr = roleTypeStr;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
