package com.qingqingmr.qqmr.domain.bean;

import com.qingqingmr.qqmr.domain.entity.UserAddress;

/**
 * 用户地址业务实体
 * @author crn
 * @datetime 2018-07-10 14:31:22
 */
public class UserAddressVO extends UserAddress {

    /**
     * 省市名称
     */
    private String provinceName;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 区/县名称
     */
    private String areaName;

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
