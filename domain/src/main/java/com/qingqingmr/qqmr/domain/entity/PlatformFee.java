package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 平台管理费
 *
 * @author crn
 * @datetime 2018-7-3 19:17:54
 */
public class PlatformFee {
    private Long id;

    private Date time;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 业务订单号
     */
    private String serviceOrderNo;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 管理费类型：1佣金，2提现
     */
    private Integer type;

    /**
     * 管理费关联id：按照type进行取对应表的id
     */
    private Long relationId;

    /**
     * 备注
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

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo == null ? null : serviceOrderNo.trim();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 管理费类型：1佣金，2提现
     */
    public enum FeeTypeEnum {

        /**
         * 佣金
         */
        COMMISSION(1, "佣金"),

        /**
         * 提现
         */
        WITHDRAW(2, "提现");

        public int code;
        public String value;

        private FeeTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static FeeTypeEnum getEnum(int code) {
            FeeTypeEnum[] status = FeeTypeEnum.values();
            for (FeeTypeEnum statu : status) {
                if (statu.code == code) {
                    return statu;
                }
            }
            return null;
        }
    }

    public PlatformFee() {
    }

    /**
     * 保存时用的构造方法
     *
     * @param time           添加时间
     * @param userId         用户id
     * @param serviceOrderNo 订单号
     * @param amount         金额
     * @param type           类型
     * @param relationId     关联id
     * @param remark         备注
     */
    public PlatformFee(Date time, Long userId, String serviceOrderNo, Double amount, Integer type, Long relationId, String remark) {
        this.time = time;
        this.userId = userId;
        this.serviceOrderNo = serviceOrderNo;
        this.amount = amount;
        this.type = type;
        this.relationId = relationId;
        this.remark = remark;
    }
}