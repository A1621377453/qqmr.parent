package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 用户奖金
 *
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */
public class UserBonus {

    private Long id;

    /**
     * 添加时间
     */
    private Date time;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 业务订单号
     */
    private String serviceOrderNo;

    /**
     * 佣金
     */
    private Double amount;

    /**
     * 佣金类型：1直推奖，2合作奖，3一级奖，4二级奖，5人头奖，6双人奖
     */
    private Integer type;

    /**
     * 关联id：type值1-5时采用t_user_bonus_detail的id，值为6时默认等于-1
     */
    private Long relationId;

    /**
     * 顾问或者美容师人头奖对应的人头数量
     */
    private Integer inviteNumber;

    /**
     * 管理费
     */
    private Double fee;

    /**
     * 是否返还：0表示未返还，1表示已经返还
     */
    private Boolean isReturn;

    /**
     * 返还时间
     */
    private Date returnTime;

    /**
     * 备注
     */
    private String remark;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo == null ? null : serviceOrderNo.trim();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Integer getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(Integer inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getFee() {
        return fee;
    }

    public Boolean getReturn() {
        return isReturn;
    }

    public void setReturn(Boolean aReturn) {
        isReturn = aReturn;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 保存佣金记录构造器(没有管理费)
     *
     * @param time           时间
     * @param userId         用户id
     * @param serviceOrderNo 业务订单号
     * @param amount         金额
     * @param type           类型
     * @param relationId     关联id：type值1-5时采用t_user_bonus_detail的id，值为6时默认等于-1
     * @param inviteNumber   邀请人数
     * @param isReturn       是否返现
     * @param returnTime     返现时间
     * @param remark         备注
     */
    public UserBonus(Date time, Long userId, String serviceOrderNo, Double amount, Integer type, Long relationId, Integer inviteNumber, Boolean isReturn, Date returnTime, String remark) {
        this.time = time;
        this.userId = userId;
        this.serviceOrderNo = serviceOrderNo;
        this.amount = amount;
        this.type = type;
        this.relationId = relationId;
        this.inviteNumber = inviteNumber;
        this.isReturn = isReturn;
        this.returnTime = returnTime;
        this.remark = remark;
    }

    public UserBonus() {

    }
}