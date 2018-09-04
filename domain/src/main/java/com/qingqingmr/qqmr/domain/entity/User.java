package com.qingqingmr.qqmr.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.qingqingmr.qqmr.common.util.Security;

import java.util.Date;

/**
 * 用户实体类
 *
 * @author liujingjing
 * @datetime 2018年7月3日 下午8:08:49
 */
public class User {
    private Long id;

    /**
     * 添加时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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
     * 交易密码
     */
    private String payPassword;

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
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 登录ip
     */
    private String loginIp;

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
        this.name = name == null ? null : name.trim();
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

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * nickName十六进制自动转码
     *
     * @return
     */
    public String getNickName() {
        return Security.decodeHex(this.nickName);
    }

	/**
	 * nickName十六进制自动编码
	 *
	 * @param nickName
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoto() {
		return photo;
	}

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
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

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId == null ? null : wxOpenId.trim();
    }

}