package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 用户资金信息
 * 
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */
public class UserFunds {

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
	 * 账户余额
	 */
	private Double balance;

	/**
	 * 可用的账户余额（用作可提现）
	 */
	private Double availableBalance;

	/**
	 * 冻结金额
	 */
	private Double freeze;

	/**
	 * 用户资金加密：MD5(user_id+balance+freeze)
	 */
	private String sign;

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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(Double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public Double getFreeze() {
		return freeze;
	}

	public void setFreeze(Double freeze) {
		this.freeze = freeze;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}