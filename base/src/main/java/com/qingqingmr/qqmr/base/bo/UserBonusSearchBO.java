package com.qingqingmr.qqmr.base.bo;

/**
 * 佣金列表搜索自定义实体
 *
 * @author crn
 * @datetime 2018-07-12 14:39:20
 */
public class UserBonusSearchBO {

    /**
     * 搜索类型   0:全部，1:交易流水，2:昵称，3:真实姓名，4:手机号
     */
    public String type;

    /**
     * 搜索内容
     */
    public String typeStr;

    /**
     * 角色 0:全部，2:会员，3:美容师，4:顾问
     */
    public String roleType;

    /**
     * 佣金类型：1直推奖，2合作奖，3一级奖，4二级奖，5人头奖，6双人奖
     */
    public String bonusType;

    /**
     * 日期开始
     */
    public String timeBegin;
    /**
     * 日期结束
     */
    public String timeEnd;

    /**
     * 当前页面
     */
    public Integer currPage;

    /**
     * 每页显示数量
     */
    public Integer pageSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public String getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
