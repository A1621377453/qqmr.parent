package com.qingqingmr.qqmr.base.bo;

/**
 * 顾问搜索自定义业务实体
 *
 * @author crn
 * @datetime 2018-07-05 15:42:33
 */
public class EmployeeSearchBO {
    /**
     * 搜索类型   0:全部，1:员工编号，2:姓名，3:手机号，4:身份证号，5:QQ号，6:所属顾问
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
     * 出生日期开始
     */
    public String birthDateBegin;
    /**
     * 出生日期结束
     */
    public String birthDateEnd;
    /**
     * 入职时间开始
     */
    public String entryDateBegin;
    /**
     * 入职时间结束
     */
    public String entryDateEnd;
    /**
     * 创建时间开始
     */
    public String creatTimeBegin;
    /**
     * 创建时间结束
     */
    public String creatTimeEnd;

    /**
     * 当前页面
     */
    public Integer currPage;

    /**
     * 每页显示数量
     */
    public Integer pageSize;

    /**
     * 角色类型：1表示美容师，2表示顾问
     */
    private Integer roleType;

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
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

    public String getBirthDateBegin() {
        return birthDateBegin;
    }

    public void setBirthDateBegin(String birthDateBegin) {
        this.birthDateBegin = birthDateBegin;
    }

    public String getBirthDateEnd() {
        return birthDateEnd;
    }

    public void setBirthDateEnd(String birthDateEnd) {
        this.birthDateEnd = birthDateEnd;
    }

    public String getEntryDateBegin() {
        return entryDateBegin;
    }

    public void setEntryDateBegin(String entryDateBegin) {
        this.entryDateBegin = entryDateBegin;
    }

    public String getEntryDateEnd() {
        return entryDateEnd;
    }

    public void setEntryDateEnd(String entryDateEnd) {
        this.entryDateEnd = entryDateEnd;
    }

    public String getCreatTimeBegin() {
        return creatTimeBegin;
    }

    public void setCreatTimeBegin(String creatTimeBegin) {
        this.creatTimeBegin = creatTimeBegin;
    }

    public String getCreatTimeEnd() {
        return creatTimeEnd;
    }

    public void setCreatTimeEnd(String creatTimeEnd) {
        this.creatTimeEnd = creatTimeEnd;
    }
}
