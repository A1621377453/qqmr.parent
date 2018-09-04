package com.qingqingmr.qqmr.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 员工列表
 *
 * @author ztl
 * @datetime 2018-7-3 20:28:28
 */
public class Employee {
    private Long id;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 员工编号：前缀+递增序列
     */
    private String sno;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 真实姓名
     */
    private String realityName;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 头像
     */
    private String photo;
    /**
     * '性别：0未知，1男，2女',
     */
    private Integer sex;
    /**
     * 邮箱
     */
    private String email;
    /**
     * qq号
     */
    private String qq;
    /**
     * 出生日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date birthDate;
    /**
     * 入职日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date entryDate;
    /**
     * 通讯地址
     */
    private String address;
    /**
     * 备注
     */
    private String remark;
    /**
     * 店面id
     */
    private Long storeId;
    /**
     * 角色类型：1表示美容师，2表示顾问
     */
    private Integer roleType;
    /**
     * 上级id：如果没有上级，那么默认值就是-1
     */
    private Long parentId;

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

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno == null ? null : sno.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getRealityName() {
        return realityName;
    }

    public void setRealityName(String realityName) {
        this.realityName = realityName == null ? null : realityName.trim();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 角色类型：1表示美容师，2表示顾问
     */
    public enum RoleTypeEnum {

        /**
         * 美容师
         */
        BEAUTICIAN(1, "美容师"),

        /**
         * 顾问
         */
        COUNSELOR(2, "顾问");

        public int code;
        public String value;

        private RoleTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static RoleTypeEnum getEnum(int code) {
            RoleTypeEnum[] status = RoleTypeEnum.values();
            for (RoleTypeEnum statu : status) {
                if (code == statu.code) {
                    return statu;
                }
            }
            return null;
        }
    }
}