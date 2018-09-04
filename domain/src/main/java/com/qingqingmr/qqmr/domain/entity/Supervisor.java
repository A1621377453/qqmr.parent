package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 管理员实体类
 * 
 * @author liujingjing
 * @datetime 2018年7月3日 下午8:00:49
 */
public class Supervisor {
	private Long id;

	/**
	 * 添加时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date time;

	/**
	 * 用户名称
	 */
	private String name;

	/**
	 * 真实姓名
	 */
	private String realityName;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 录登密码
	 */
	private String password;

	/**
	 * 锁定状态:0-未锁定 ,1-锁定
	 */
	private Integer lockStatus;

	/**
	 * 登录次数
	 */
	private Long loginCount;

	/**
	 * 上次登录时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
	 * 上次登录ip
	 */
	private String lastLoginIp;

	/**
	 * 创建者id
	 */
	private Long createrId;

	/**
	 * 连续登录失败的次数
	 */
	private Integer passwordContinueFails;

	/**
	 * 密码连续错误被锁定
	 */
	private Boolean isPasswordLocked;

	/**
	 * 密码连续错误被锁定时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date passwordLockedTime;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getRealityName() {
		return realityName;
	}

	public void setRealityName(String realityName) {
		this.realityName = realityName == null ? null : realityName.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Long getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp == null ? null : lastLoginIp.trim();
	}

	public Long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Long createrId) {
		this.createrId = createrId;
	}

	public Integer getPasswordContinueFails() {
		return passwordContinueFails;
	}

	public void setPasswordContinueFails(Integer passwordContinueFails) {
		this.passwordContinueFails = passwordContinueFails;
	}

	public Boolean getIsPasswordLocked() {
		return isPasswordLocked;
	}

	public void setIsPasswordLocked(Boolean isPasswordLocked) {
		this.isPasswordLocked = isPasswordLocked;
	}

	public Date getPasswordLockedTime() {
		return passwordLockedTime;
	}

	public void setPasswordLockedTime(Date passwordLockedTime) {
		this.passwordLockedTime = passwordLockedTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	/**
	 * 枚举:管理员锁定状态
	 * 
	 * @author liujingjing
	 * @datetime 2018年7月3日 下午8:05:46
	 */
	public enum LockStatus {
		/** 0:未锁定 */
		STATUS_0_NORMAL(0, "未锁定"),

		/** 1:锁定 */
		STATUS_1_LOCKED(1, "锁定");

		public int code;
		public String value;

		private LockStatus(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public static LockStatus getEnum(int code) {
			LockStatus[] statuies = LockStatus.values();
			for (LockStatus stat : statuies) {
				if (stat.code == code) {

					return stat;
				}
			}

			return STATUS_1_LOCKED;
		}
	}
}