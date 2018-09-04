package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单
 *
 * @author crn
 * @datetime 2018-7-3 19:17:54
 */
public class Order {
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private Double amount;

    /**
     * 实际付款金额
     */
    private Double realAmount;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 状态：1待付款，2交易成功，-1交易关闭
     */
    private Integer status;

    /**
     * 支付方式：1微信支付
     */
    private Integer payType;

    /**
     * 取消方式：0默认，1自动，2手动
     */
    private Integer cancelType;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 是否代付：1是，0不是
     */
    private Boolean isPayFor;

    /**
     * 代付id
     */
    private Long payForId;

    /**
     * 第三方订单号：如微信订单号
     */
    private String transactionId;

    /**
     * 订单备注
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Double realAmount) {
        this.realAmount = realAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getCancelType() {
        return cancelType;
    }

    public void setCancelType(Integer cancelType) {
        this.cancelType = cancelType;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Boolean getPayFor() {
        return isPayFor;
    }

    public void setPayFor(Boolean payFor) {
        isPayFor = payFor;
    }

    public Long getPayForId() {
        return payForId;
    }

    public void setPayForId(Long payForId) {
        this.payForId = payForId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 交易状态：1待付款，2交易成功，-1交易关闭
     */
    public enum DealStatusEnum {

        /**
         * 待付款
         */
        OBLIGATION(1, "待付款"),

        /**
         * 交易成功
         */
        SUCCESS(2, "交易成功"),

        /**
         * 交易关闭
         */
        CLOSE(-1, "交易关闭");

        public int code;
        public String value;

        private DealStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static DealStatusEnum getEnum(int code) {
            DealStatusEnum[] status = DealStatusEnum.values();
            for (DealStatusEnum statu : status) {
                if (statu.code == code) {
                    return statu;
                }
            }
            return null;
        }
    }


    /**
     * 取消方式：1自动，2手动'
     */
    public enum CancelTypeEnum {

        /**
         * 自动
         */
        automatic(1, "自动"),

        /**
         * 手动
         */
        Manual(2, "手动");

        public int code;
        public String value;

        private CancelTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static CancelTypeEnum getEnum(int code) {
            CancelTypeEnum[] status = CancelTypeEnum.values();
            for (CancelTypeEnum statu : status) {
                if (statu.code == code) {
                    return statu;
                }
            }
            return null;
        }
    }
}