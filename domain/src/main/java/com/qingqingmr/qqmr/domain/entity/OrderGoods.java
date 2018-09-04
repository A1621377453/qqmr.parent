package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单商品详情
 *
 * @author crn
 * @datetime 2018-7-3 19:17:54
 */
public class OrderGoods {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品价格
     */
    private double goodsPrice;

    /**
     * 商品数量
     */
    private Integer number;

    /**
     * 有效时长
     */
    private Double validityPeriod;

    /**
     * 有效时长单位：1分钟 2小时 3天 4周 5月 6年
     */
    private Integer validityPeriodUnit;

    /**
     * 订单id
     */
    private Long orderId;

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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Double validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Integer getValidityPeriodUnit() {
        return validityPeriodUnit;
    }

    public void setValidityPeriodUnit(Integer validityPeriodUnit) {
        this.validityPeriodUnit = validityPeriodUnit;
    }

    public OrderGoods(Date time, Long goodsId, double goodsPrice, Integer number, Double validityPeriod, Integer validityPeriodUnit, Long orderId) {
        this.time = time;
        this.goodsId = goodsId;
        this.goodsPrice = goodsPrice;
        this.number = number;
        this.validityPeriod = validityPeriod;
        this.validityPeriodUnit = validityPeriodUnit;
        this.orderId = orderId;
    }

    public OrderGoods() {
    }

    /**
     * 有效时长单位：1分钟 2小时 3天 4周 5月 6年
     */
    public enum ValidityPeriodUnitEnum {
        /**
         * 分钟
         */
        MINUTE(1, "分钟"),

        /**
         * 小时
         */
        HOUR(2, "小时"),

        /**
         * 天
         */
        DAY(3, "天"),

        /**
         * 周
         */
        WEEK(4, "周"),

        /**
         * 月
         */
        MONTH(5, "月"),

        /**
         * 年
         */
        YEAR(6, "年");

        public int code;
        public String value;

        private ValidityPeriodUnitEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static ValidityPeriodUnitEnum getEnum(int code) {
            ValidityPeriodUnitEnum[] status = ValidityPeriodUnitEnum.values();
            for (ValidityPeriodUnitEnum statu : status) {
                if (statu.code == code) {
                    return statu;
                }
            }
            return null;
        }
    }
}