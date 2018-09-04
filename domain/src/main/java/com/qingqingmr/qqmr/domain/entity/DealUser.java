package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户交易记录
 *
 * @author ztl
 * @datetime 2018-7-3 19:15:49
 */
public class DealUser {
	private Long id;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date time;

	private Long userId;
	/**
	 * 业务订单号
	 */
	private String serviceOrderNo;
	/**
	 * 收支类型：1收入，2支出
	 */
	private Integer dealType;
	/**
	 * 操作类型 (用枚举表示)
	 */
	private Integer operationType;
	/**
	 * 操作金额
	 */
	private Double amount;
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

	public Integer getDealType() {
		return dealType;
	}

	public void setDealType(Integer dealType) {
		this.dealType = dealType;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public DealUser(Long userId, String serviceOrderNo, Integer dealType, Integer operationType, Double amount,
			String remark) {
		this.userId = userId;
		this.serviceOrderNo = serviceOrderNo;
		this.dealType = dealType;
		this.operationType = operationType;
		this.amount = amount;
		this.remark = remark;
	}

	public DealUser() {
	}
}