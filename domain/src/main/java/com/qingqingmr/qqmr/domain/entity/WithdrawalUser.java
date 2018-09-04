package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 用户提现表
 * 
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */
public class WithdrawalUser {

	private Long id;

	/**
	 * 添加时间
	 */
	private Date time;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 提现金额
	 */
	private Double amount;

	/**
	 * 手续费
	 */
	private Double fee;

	/**
	 * 账户可提现金额
	 */
	private Double availableBalance;

	/**
	 * 业务订单号
	 */
	private String serviceOrderNo;

	/**
	 * 提现方式：1零钱，2银行卡
	 */
	private Integer type;

	/**
	 * 提现状态：1待审核，2处理中，3成功，-1拒绝
	 */
	private Integer status;

	/**
	 * 审核时间
	 */
	private Date auditTime;

	/**
	 * 审核人员
	 */
	private Long auditSupervisorId;

	/**
	 * 审核意见
	 */
	private String auditOpinion;

	/**
	 * 提现完成时间
	 */
	private Date completeTime;

	/**
	 * 提现完成信息
	 */
	private String completeInfo;

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(Double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getServiceOrderNo() {
		return serviceOrderNo;
	}

	public void setServiceOrderNo(String serviceOrderNo) {
		this.serviceOrderNo = serviceOrderNo == null ? null : serviceOrderNo.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Long getAuditSupervisorId() {
		return auditSupervisorId;
	}

	public void setAuditSupervisorId(Long auditSupervisorId) {
		this.auditSupervisorId = auditSupervisorId;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion == null ? null : auditOpinion.trim();
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getCompleteInfo() {
		return completeInfo;
	}

	public void setCompleteInfo(String completeInfo) {
		this.completeInfo = completeInfo == null ? null : completeInfo.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	/**
	 * 提现状态  1待审核，2处理中，3成功，-1拒绝
	 */
	public enum WithdrawStatusEnum {

		/**
		 * 待审核
		 */
		PENDING_REVIEW(1, "待审核"),

		/**
		 * 处理中
		 */
		PROCESSING(2, "处理中"),

		/**
		 * 成功
		 */
		SUCCESS(3, "成功"),

		/**
		 * 拒绝
		 */
		REFUSE(-1, "拒绝");

		public int code;
		public String value;

		private WithdrawStatusEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public static WithdrawStatusEnum getEnum(int code) {
			WithdrawStatusEnum[] status = WithdrawStatusEnum.values();
			for (WithdrawStatusEnum statu : status) {
				if (statu.code == code) {
					return statu;
				}
			}
			return null;
		}
	}
}