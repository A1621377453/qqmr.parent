package com.qingqingmr.qqmr.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.qingqingmr.qqmr.common.util.Security;

/**
 * 订单业务实体
 *
 * @author crn
 * @datetime 2018-07-13 20:25:00
 */
public class OrderVO {
    /**
     * 订单id
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realityName;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 金额（元）
     */
    private Double amount;
    /**
     * 下单时间
     */
    private String time;

    /**
     * 支付方式 1微信支付
     */
    private Integer payType;

    /**
     * 支付方式字符串
     */
    private String payTypeStr;

    /**
     * 付款日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String payTime;

    /**
     * 是否代付
     */
    private String isPayFor;

    /**
     * 状态
     */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayTypeStr() {
        return payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getIsPayFor() {
        return isPayFor;
    }

    public void setIsPayFor(String isPayFor) {
        this.isPayFor = isPayFor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
