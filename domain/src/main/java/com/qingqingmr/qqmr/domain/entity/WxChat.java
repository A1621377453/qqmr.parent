package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

/**
 * 微信绑定
 *
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */
public class WxChat {

	private Long id;
	/**
	 * 添加时间
	 */
	private Date time;

	/**
	 * 微信openId
	 */
	private String openId;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 昵称十六进制转码
	 */
	@SuppressWarnings("unused")
	private String nickNameHex;

	/**
	 * 头像
	 */
	private String imageUrl;

	/**
	 * 性别（0未知，1男，2女）
	 */
	private Integer sex;

	/**
	 * 微信授权信息
	 */
	private String wxInfo;

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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId == null ? null : openId.trim();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName == null ? null : nickName.trim();
	}

	public String getNickNameHex() {
		if (StringUtils.isNotBlank(this.nickName)) {
			try {
				return new String(Hex.decodeHex(this.nickName.toCharArray()), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return this.nickName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl == null ? null : imageUrl.trim();
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getWxInfo() {
		return wxInfo;
	}

	public void setWxInfo(String wxInfo) {
		this.wxInfo = wxInfo == null ? null : wxInfo.trim();
	}
}