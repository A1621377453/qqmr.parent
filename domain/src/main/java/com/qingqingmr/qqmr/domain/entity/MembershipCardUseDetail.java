package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 会员卡使用详情
 *
 * @author ythu
 * @datetime 2018-7-3 19:24:42
 */
public class MembershipCardUseDetail {
    /**
     * 主键
     */
    private Long id;
    /**
     * 添加时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 会员卡id
     */
    private Long cardId;
    /**
     * 添加记录的用户id
     */
    private Long userId;
    /**
     * 服务内容
     */
    private String content;
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

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public MembershipCardUseDetail(Date time, Long cardId, Long userId, String content, String remark) {
		super();
		this.time = time;
		this.cardId = cardId;
		this.userId = userId;
		this.content = content;
		this.remark = remark;
	}

	public MembershipCardUseDetail() {
	}
    
    
}