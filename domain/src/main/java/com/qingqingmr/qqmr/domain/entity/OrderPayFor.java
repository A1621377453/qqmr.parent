package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单代付信息
 *
 * @author crn
 * @datetime 2018-7-3 19:17:54
 */
public class OrderPayFor {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 代付用户id：冗余字段
     */
    private Long payUserId;

    /**
     * 状态：0用户未关联，1用户已关联
     */
    private Integer status;

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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public Long getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(Long payUserId) {
        this.payUserId = payUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public OrderPayFor(Date time, String name, String mobile, String cardNo, Long payUserId, Integer status) {
        this.time = time;
        this.name = name;
        this.mobile = mobile;
        this.cardNo = cardNo;
        this.payUserId = payUserId;
        this.status = status;
    }

    public OrderPayFor() {
    }
}