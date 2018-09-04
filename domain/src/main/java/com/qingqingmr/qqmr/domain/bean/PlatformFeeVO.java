package com.qingqingmr.qqmr.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.qingqingmr.qqmr.common.util.Security;

import java.util.Date;

/**
 * 收取费用业务实体
 *
 * @author crn
 * @datetime 2018-07-12 10:42:16
 */
public class PlatformFeeVO {
    /**
     * 交易流水
     */
    private String orderNo;
    /**
     * 划入账户
     */
    private String acount = "平台账户";
    /**
     * 扣款人userId
     */
    private String userId;
    /**
     * 扣款人昵称
     */
    private String nickName;
    /**
     * 扣款人真实姓名
     */
    private String realityName;
    /**
     * 性别 1男，2女
     */
    private String sex;
    /**
     * 性别字符串
     */
    private String sexStr;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 角色 1顾客，2会员，3美容师，4顾问
     */
    private String roleType;
    /**
     * 角色字符串
     */
    private String roleTypeStr;
    /**
     * 扣款金额
     */
    private String amount;
    /**
     * 费用类型 1:佣金管理费 2:提现手续费
     */
    private String type;
    /**
     * 费用类型字符串
     */
    private String typeStr;
    /**
     * 交易时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
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
