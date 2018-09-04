package com.qingqingmr.qqmr.base.bo;

/**
 * 后台订单搜索自定义实体
 *
 * @author crn
 * @datetime 2018-07-13 20:20:56
 */
public class OrderSearchBO {
    /**
     * 搜索类型   0:全部，1:订单号码，2:昵称，3:真实姓名，4:手机号
     */
    public String type;

    /**
     * 搜索内容
     */
    public String typeStr;

    /**
     * 订单状态 0全部, 1待付款，2交易成功，-1交易关闭
     */
    public String orderStatus;

    /**
     * 下单日期开始
     */
    public String timeBegin;
    /**
     * 下单日期结束
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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
