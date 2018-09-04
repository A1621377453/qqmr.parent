package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 用户收货地址
 * 
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */

public class UserAddress {
	
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
     * 姓名
     */
    private String name;
    
    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 省份id：冗余字段
     */
    private Long provinceId;
    
    /**
     * 城市id：冗余字段
     */
    private Long cityId;
    
    /**
     * 地区id
     */
    private Long areaId;
    
    /**
     * 详细地址
     */
    private String detail;
    
    /**
     * 是否是默认地址：0不是默认地址，1是默认地址
     */
    private Boolean isDefault;
    
    /**
     * 是否已经删除：0没有删除，1已经删除
     */
    private Boolean isDel;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }
}