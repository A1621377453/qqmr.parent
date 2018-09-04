package com.qingqingmr.qqmr.domain.bean;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 操作日志实体类
 *
 * @author ztl
 * @datetime 2018-07-05 15:57:21
 */
public class SupervisorEventLogVO {

    private Long id;

    /**
     * 管理员账号
     */
    private String name;

    /**
     * 真实姓名
     */
    private String realityName;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 操作者ip
     */
    private String ip;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealityName() {
        return realityName;
    }

    public void setRealityName(String realityName) {
        this.realityName = realityName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
