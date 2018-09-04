package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 用户奖金详细表
 *
 * @author liujinjin
 * @datetime 2018-7-3 19:24:22
 */
public class UserBonusDetail {

    private Long id;

    /**
     * 添加时间：计算人头奖励，以该时间为准
     */
    private Date time;

    /**
     * 得到佣金的用户id
     */
    private Long userId;

    /**
     * 产生佣金的关联用户id
     */
    private Long relationUserId;

    /**
     * 产生佣金的关联用户的邀请人id
     */
    private Long relationUserSpreadId;

    /**
     * 类型：1直推奖，2合作奖，3一级奖，4二级奖，5人头奖，6双人奖
     */
    private Integer type;

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

    public Long getRelationUserId() {
        return relationUserId;
    }

    public void setRelationUserId(Long relationUserId) {
        this.relationUserId = relationUserId;
    }

    public Long getRelationUserSpreadId() {
        return relationUserSpreadId;
    }

    public void setRelationUserSpreadId(Long relationUserSpreadId) {
        this.relationUserSpreadId = relationUserSpreadId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 保存构造函数
     *
     * @param time                 时间
     * @param userId               用户id
     * @param relationUserId       关联用户id
     * @param relationUserSpreadId 关联用户id的直接邀请人
     * @param type                 奖励类型
     */
    public UserBonusDetail(Date time, Long userId, Long relationUserId, Long relationUserSpreadId, Integer type) {
        this.time = time;
        this.userId = userId;
        this.relationUserId = relationUserId;
        this.relationUserSpreadId = relationUserSpreadId;
        this.type = type;
    }

    public UserBonusDetail() {
    }
}