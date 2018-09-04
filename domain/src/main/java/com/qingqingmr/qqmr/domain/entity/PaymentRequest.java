package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * <p>
 * 支付请求第三方记录表
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-11 10:59:48
 */
public class PaymentRequest {
    private Long id;
    private Date time;
    private Long userId;
    private String bizzOrderNo;
    private Integer bizzType;
    private String orderNo;
    private Integer payType;
    private Integer status = -1;
    private Date completedTime;
    private String aynsUrl;
    private String reqParams;

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

    public String getBizzOrderNo() {
        return bizzOrderNo;
    }

    public void setBizzOrderNo(String bizzOrderNo) {
        this.bizzOrderNo = bizzOrderNo;
    }

    public Integer getBizzType() {
        return bizzType;
    }

    public void setBizzType(Integer bizzType) {
        this.bizzType = bizzType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }

    public String getAynsUrl() {
        return aynsUrl;
    }

    public void setAynsUrl(String aynsUrl) {
        this.aynsUrl = aynsUrl;
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
        this.reqParams = reqParams;
    }
}
