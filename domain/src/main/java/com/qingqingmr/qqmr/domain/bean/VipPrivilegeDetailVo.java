package com.qingqingmr.qqmr.domain.bean;

/**
 * 自定义：VIP特权详情实体
 * 
 * @author liujinjin
 * @datetime 2018年7月10日 下午8:11:14
 */
public class VipPrivilegeDetailVo {
	/**
	 * 会员卡卡号
	 */
	private String cardNo;

	/**
	 * 商品id
	 */
	private String goodsId;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品类型：：1会员卡，2其他
	 */
	private String type;
	/**
	 * 商品图片
	 */
	private String image;

	/**
	 * 商品价格
	 */
	private Double price;

	/**
	 * 商品内容
	 */
	private String content;

	/**
	 * 服务内容
	 */
	private String service;

	/**
	 * 服务次数
	 */
	private String serviceCount;

	/**
	 * 服务形式：1到店
	 */
	private String serviceForm;

	/**
	 * 购买人数
	 */
	private String purchaseNum;

	/**
	 * 有效期：是否激活，激活后显示到期时间
	 */
	private String validityPeriod;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(String serviceCount) {
		this.serviceCount = serviceCount;
	}

	public String getServiceForm() {
		return serviceForm;
	}

	public void setServiceForm(String serviceForm) {
		this.serviceForm = serviceForm;
	}

	public String getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(String purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
}
