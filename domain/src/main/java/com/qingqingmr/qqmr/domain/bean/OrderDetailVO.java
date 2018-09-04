package com.qingqingmr.qqmr.domain.bean;

import com.qingqingmr.qqmr.common.util.Security;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 订单详情
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月5日 下午3:43:24
 */
public class OrderDetailVO {
    /**
     * 订单id
     */
    private Long id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 状态：1待付款，2交易成功，-1交易关闭
     */
    private Integer status;
    /**
     * 订单时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 支付方式：1微信支付
     */
    private Integer payType;
    /**
     * 支付方式字符串
     */
    private String payTypeStr;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 实际付款金额
     */
    private Double realAmount;
    /**
     * 付款人昵称
     */
    private String nickName;
    /**
     * 付款人真实姓名
     */
    private String realityName;
    /**
     * 付款人手机号
     */
    private String mobile;
    /**
     * 代付人昵称
     */
    private String payForNickName;
    /**
     * 代付人手机号
     */
    private String payForMobile;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer number;

    /**
     * 商品价格
     */
    private Double goodsPrice;
    /**
     * 订单金额
     */
    private Double amount;
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
     * 取消方式：1自动，2手动
     */
    private Integer cancelType;

    /**
     * 商品图片
     */
    private String image;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Double getRealAmount() {
        return realAmount;
    }

	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
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

    public String getPayForNickName() {
        return payForNickName;
    }

    public void setPayForNickName(String payForNickName) {
        this.payForNickName = payForNickName;
    }

    public String getPayForMobile() {
        return payForMobile;
    }

    public void setPayForMobile(String payForMobile) {
        this.payForMobile = payForMobile;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Boolean getIsPayFor() {
        return isPayFor;
    }

    public void setIsPayFor(Boolean payFor) {
        isPayFor = payFor;
    }

    public Long getPayForId() {
        return payForId;
    }

    public void setPayForId(Long payForId) {
        this.payForId = payForId;
    }

    public Integer getCancelType() {
        return cancelType;
    }

    public void setCancelType(Integer cancelType) {
        this.cancelType = cancelType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
