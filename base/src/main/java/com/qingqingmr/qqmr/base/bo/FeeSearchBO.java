package com.qingqingmr.qqmr.base.bo;

/**
 * 收取费用搜索自定义业务实体
 *
 * @author crn
 * @datetime 2018-07-05 15:42:33
 */
public class FeeSearchBO {
    /**
     * 搜索类型   0:全部，1:交易流水，2:扣款人昵称，3:扣款人真实姓名，4:扣款人手机号
     */
    public String type;

    /**
     * 搜索内容
     */
    public String typeStr;

    /**
     * 性别 0:全部，1:男，2:女
     */
    public String sex;

    /**
     * 角色 0:全部，2:会员，3:美容师，4:顾问
     */
    public String roleType;

    /**
     * 费用类型 0:全部，1:佣金管理费，2:提现手续费
     */
    public String feeType;

    /**
     * 交易日期开始
     */
    public String dealDateBegin;
    /**
     * 交易日期结束
     */
    public String dealDateEnd;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getDealDateBegin() {
        return dealDateBegin;
    }

    public void setDealDateBegin(String dealDateBegin) {
        this.dealDateBegin = dealDateBegin;
    }

    public String getDealDateEnd() {
        return dealDateEnd;
    }

    public void setDealDateEnd(String dealDateEnd) {
        this.dealDateEnd = dealDateEnd;
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
