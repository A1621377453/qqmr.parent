package com.qingqingmr.qqmr.base.bo;

/**
 * 顾问搜索自定义业务实体
 *
 * @author crn
 * @datetime 2018-07-09 15:42:33
 */
public class UserSearchBO {
    /**
     * 搜索类型   0:全部，1:ID(员工编号)，2:昵称，3:手机号，4:直接邀请人姓名，5:直接邀请人手机号，6:间接邀请人姓名，7:间接邀请人手机号
     */
    public String type;

    /**
     * 搜索内容
     */
    public String typeStr;

    /**
     * 直接邀请人角色 0:全部，2:会员，3:美容师，4:顾问
     */
    public String dirInviteRole;

    /**
     * 间接邀请人角色 0:全部，2:会员，3:美容师，4:顾问
     */
    public String inDirInviteRole;
    /**
     * 注册日期开始
     */
    public String timeBegin;

    /**
     * 注册日期结束
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

    /**
     * 角色类型：1表示顾客，2表示会员
     */
    private Integer roleType;

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

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }
}
