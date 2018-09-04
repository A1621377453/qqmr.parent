package com.qingqingmr.qqmr.domain.bean;

/**
 * 用户收货地址
 * 
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */

public class UserAddressDetailVO {
	
    private Long id;
    
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
     * 省市名称
     */
    public String provinceName;
    /**
     * 市名称
     */
    public String cityName;
    /**
     * 区/县名称
     */
    public String areaName;
    
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
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
		this.detail = detail;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}