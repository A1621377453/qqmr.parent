package com.qingqingmr.qqmr.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.qingqingmr.qqmr.common.util.Security;

import java.util.Date;

/**
 * 客户业务实体
 *
 * @author crn
 * @datetime 2018-07-09 18:42:20
 */
public class UserVO {

    /**
     * 编号id
     */
    private Long id;
    /**
     * 注册时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 绑定手机号
     */
    private String mobile;
    /**
     * 直接邀请人id
     */
    private Long dirInviteId;
    /**
     * 直接邀请人姓名
     */
    private String dirInviteName;
    /**
     * 直接邀请人手机号
     */
    private String dirInviteMobile;
    /**
     * 直接邀请人角色
     */
    private String dirInviteRole;
    /**
     * 间接邀请人id
     */
    private Long inDirInviteId;
    /**
     * 间接邀请人姓名
     */
    private String inDirInviteName;
    /**
     * 间接邀请人手机号
     */
    private String inDirInviteMobile;
    /**
     * 间接邀请人角色
     */
    private String inDirInviteRole;
    /**
     * 会员卡id
     */
    private Long cardId;
    /**
     * 会员卡状态
     */
    private Boolean isActive;

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

    public String getNickName() {
        return Security.decodeHex(this.nickName);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getDirInviteId() {
        return dirInviteId;
    }

    public void setDirInviteId(Long dirInviteId) {
        this.dirInviteId = dirInviteId;
    }

    public String getDirInviteName() {
        return Security.decodeHex(this.dirInviteName);
    }

    public void setDirInviteName(String dirInviteName) {
        this.dirInviteName = dirInviteName;
    }

    public String getDirInviteMobile() {
        return dirInviteMobile;
    }

    public void setDirInviteMobile(String dirInviteMobile) {
        this.dirInviteMobile = dirInviteMobile;
    }

    public String getDirInviteRole() {
        return dirInviteRole;
    }

    public void setDirInviteRole(String dirInviteRole) {
        this.dirInviteRole = dirInviteRole;
    }

    public Long getInDirInviteId() {
        return inDirInviteId;
    }

    public void setInDirInviteId(Long inDirInviteId) {
        this.inDirInviteId = inDirInviteId;
    }

    public String getInDirInviteName() {
        return Security.decodeHex(this.inDirInviteName);
    }

    public void setInDirInviteName(String inDirInviteName) {
        this.inDirInviteName = inDirInviteName;
    }

    public String getInDirInviteMobile() {
        return inDirInviteMobile;
    }

    public void setInDirInviteMobile(String inDirInviteMobile) {
        this.inDirInviteMobile = inDirInviteMobile;
    }

    public String getInDirInviteRole() {
        return inDirInviteRole;
    }

    public void setInDirInviteRole(String inDirInviteRole) {
        this.inDirInviteRole = inDirInviteRole;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
