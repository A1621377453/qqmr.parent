package com.qingqingmr.qqmr.domain.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qingqingmr.qqmr.common.util.Security;

import java.util.Date;

/**
 * 佣金业务实体
 *
 * @author crn
 * @datetime 2018-07-12 14:30:02
 */
public class UserBonusVO {
    /**
     * 交易流水
     */
    private String orderNo;
    /**
     * 用户userId
     */
    private String userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realityName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 角色
     */
    private String roleType;
    /**
     * 角色字符串
     */
    private String roleTypeStr;
    /**
     * 佣金
     */
    private String amount;
    /**
     * 佣金类型 1直推奖，2合作奖，3一级奖，4二级奖，5人头奖，6双人奖
     */
    private String type;
    /**
     * 佣金类型字符串
     */
    private String typeStr;
    /**
     * 时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return Security.decodeHex(this.nickName);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealityName() {
        return realityName;
    }

    public void setRealityName(String realityName) {
        this.realityName = realityName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleTypeStr() {
        return roleTypeStr;
    }

    public void setRoleTypeStr(String roleTypeStr) {
        this.roleTypeStr = roleTypeStr;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
