package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 用户信息表
 * 
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */
public class UserInfo {
	
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
     * 手机号：冗余字段
     */
    private String mobile;
    
    /**
     * 店面id
     */
    private Long storeId;
    
    /**
     * 邀请人id：美容师和顾问之间不存在邀请关系
     */
    private Long spreadId;
    
    /**
     * 邀请时间
     */
    private Date spreadTime;
    
    /**
     * 所属美容师id：会员所属美容师id，其他的默认为0
     */
    private Long beauticianId;
    
    /**
     * 所属顾问id：会员、美容师对应顾问id，其他默认为0
     */
    private Long adviserId;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getSpreadId() {
        return spreadId;
    }

    public void setSpreadId(Long spreadId) {
        this.spreadId = spreadId;
    }

    public Date getSpreadTime() {
        return spreadTime;
    }

    public void setSpreadTime(Date spreadTime) {
        this.spreadTime = spreadTime;
    }

    public Long getBeauticianId() {
        return beauticianId;
    }

    public void setBeauticianId(Long beauticianId) {
        this.beauticianId = beauticianId;
    }

    public Long getAdviserId() {
        return adviserId;
    }

    public void setAdviserId(Long adviserId) {
        this.adviserId = adviserId;
    }
}