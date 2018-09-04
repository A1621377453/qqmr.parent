package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商品
 *
 * @author ztl
 * @datetime 2018-7-3 20:31:20
 */
public class Goods {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品类型：1会员卡，2其他
     */
    private Integer type;
    /**
     * 商品图片
     */
    private String image;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 有效时长
     */
    private Double validityPeriod;
    /**
     * 有效时长单位：1分钟 2小时 3天 4周 5月 6年
     */
    private Integer validityPeriodUnit;
    /**
     * 服务内容
     */
    private String service;
    /**
     * 服务次数：-1表示次数不限制
     */
    private Integer serviceCount;
    /**
     * 服务形式：1到店
     */
    private Integer serviceForm;
    /**
     * 商品内容
     */
    private String content;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service == null ? null : service.trim();
    }

    public Integer getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceCount = serviceCount;
    }

    public Integer getServiceForm() {
        return serviceForm;
    }

    public void setServiceForm(Integer serviceForm) {
        this.serviceForm = serviceForm;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }


    /**
     * 商品类型：1表示会员卡，2表示其他
     */
    public enum GoodsTypeEnum {

        /**
         * 会员卡
         */
        MEMBERSHIP_CARD(1, "会员卡"),

        /**
         * 其他
         */
        OTHER(2, "其他");

        public int code;
        public String value;

        private GoodsTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static GoodsTypeEnum getEnum(int code) {
            GoodsTypeEnum[] status = GoodsTypeEnum.values();
            for (GoodsTypeEnum statu : status) {
                if (code == statu.code) {
                    return statu;
                }
            }
            return null;
        }
    }

}