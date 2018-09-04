package com.qingqingmr.qqmr.domain.bean;

import java.util.Date;

/**
 * 用户保存实体类
 *
 * @author liujingjing
 * @datetime 2018年7月3日 下午8:08:49
 */
public class UserSaveVO {
	private Long id;

	/**
	 * 添加时间
	 */
	private Date time;

	/**
	 * 用户名
	 */
	private String name;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 性别：0未填写，1男，2女
	 */
	private Integer sex;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 头像
	 */
	private String photo;

	/**
	 * 角色：1顾客，2会员，3美容师，4顾问
	 */
	private Integer roleType;

	/**
	 * 员工id：role_type为3,4的时候才会用到该字段，否则为-1
	 */
	private Long employeeId;

	/**
	 * 微信openid
	 */
	private String wxOpenId;

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
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public UserSaveVO(Long id, Date time, String name, String mobile, String password, Integer sex, String nickName,
			String photo, Integer roleType, Long employeeId, String wxOpenId) {
		this.id = id;
		this.time = time;
		this.name = name;
		this.mobile = mobile;
		this.password = password;
		this.sex = sex;
		this.nickName = nickName;
		this.photo = photo;
		this.roleType = roleType;
		this.employeeId = employeeId;
		this.wxOpenId = wxOpenId;
	}

	public UserSaveVO() {
	}
}
