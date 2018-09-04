package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * <p>
 * 支付第三方回调记录表
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-10 16:09:16
 */
public class PaymentCallBack {
    private Long id;
    private Date time;
    private String requestOrderNo;
    private String cbParams;
    private Integer dataType;

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

    public String getRequestOrderNo() {
        return requestOrderNo;
    }

    public void setRequestOrderNo(String requestOrderNo) {
        this.requestOrderNo = requestOrderNo;
    }

    public String getCbParams() {
        return cbParams;
    }

    public void setCbParams(String cbParams) {
        this.cbParams = cbParams;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public PaymentCallBack(Long id, Date time, String requestOrderNo, String cbParams, Integer dataType) {
        this.id = id;
        this.time = time;
        this.requestOrderNo = requestOrderNo;
        this.cbParams = cbParams;
        this.dataType = dataType;
    }

    public PaymentCallBack() {
    }

    public PaymentCallBack(Date time, String requestOrderNo, String cbParams, Integer dataType) {
        this.time = time;
        this.requestOrderNo = requestOrderNo;
        this.cbParams = cbParams;
        this.dataType = dataType;
    }
}
