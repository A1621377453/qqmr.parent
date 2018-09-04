package com.qingqingmr.qqmr.domain.bean;

import com.qingqingmr.qqmr.domain.entity.UserFunds;

/**
 * 用户资金信息业务层实体
 * @author crn
 * @datetime 2018-08-01 20:19:23
 */
public class UserFundsVO extends UserFunds {
    // 用户总资产
    private Double totalAssets;

    public Double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Double totalAssets) {
        this.totalAssets = totalAssets;
    }
}
