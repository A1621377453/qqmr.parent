package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户会员卡信息
 *
 * @author ythu
 * @datetime 2018-7-3 19:24:42
 */
public class MembershipCard {
    /**
     * 主键
     */
    private Long id;
    /**
     * 添加时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 是否激活：0未激活，1已激活
     */
    private Boolean isActive;
    /**
     * 激活时间
     */
    private Date activeTime;
    /**
     * 是否是代付卡：0不是，1是
     */
    private Boolean isPayFor;
    /**
     * 到期时间
     */
    private Date expireTime;
    /**
     * 是否到期：0表示没到期，1表示到期
     */
    private Boolean isExpire;
    /**
     * 关联订单号
     */
    private Long orderId;
    /**
     *
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Boolean getIsPayFor() {
        return isPayFor;
    }

    public void setIsPayFor(Boolean isPayFor) {
        this.isPayFor = isPayFor;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Boolean getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(Boolean isExpire) {
        this.isExpire = isExpire;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}