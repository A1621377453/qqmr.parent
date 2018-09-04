package com.qingqingmr.qqmr.domain.bean;

import java.util.Date;

import com.qingqingmr.qqmr.common.util.Security;

/**
 * <p>
 * 用户邀请人员实体类
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月6日 下午8:25:01
 */
public class InviteMemberVO {
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 添加时间
	 */
	private Date time;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 头像
	 */
	private String photo;
	/**
	 * 性别
	 */
	private String sexStr;
	/**
	 * 角色
	 */
	private String roleTypeStr;
	/**
	 * 直接邀请人id
	 */
	private Long dirInviteId;
	/**
	 * 直接邀请人角色
	 */
	private String dirInviteRole;
	/**
	 * 间接邀请人id
	 */
	private String inDirInviteRole;
	/**
	 * 间接邀请人角色
	 */
	private Long inDirInviteId;
	/**
	 * 会员卡id
	 */
	private Long cardId;
	/**
	 * 是否可更改
	 */
	private Boolean isChange;

	public Long getDirInviteId() {
		return dirInviteId;
	}

	public void setDirInviteId(Long dirInviteId) {
		this.dirInviteId = dirInviteId;
	}

	public String getDirInviteRole() {
		return dirInviteRole;
	}

	public void setDirInviteRole(String dirInviteRole) {
		this.dirInviteRole = dirInviteRole;
	}

	public String getInDirInviteRole() {
		return inDirInviteRole;
	}

	public void setInDirInviteRole(String inDirInviteRole) {
		this.inDirInviteRole = inDirInviteRole;
	}

	public Long getInDirInviteId() {
		return inDirInviteId;
	}

	public void setInDirInviteId(Long inDirInviteId) {
		this.inDirInviteId = inDirInviteId;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public Boolean getIsChange() {
		return isChange;
	}

	public void setIsChange(Boolean change) {
		isChange = change;
	}

	public String getSexStr() {
		return sexStr;
	}

	public void setSexStr(String sexStr) {
		this.sexStr = sexStr;
	}

	public String getRoleTypeStr() {
		return roleTypeStr;
	}

	public void setRoleTypeStr(String roleTypeStr) {
		this.roleTypeStr = roleTypeStr;
	}

	public String getNickName() {
		return Security.decodeHex(this.nickName);
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
