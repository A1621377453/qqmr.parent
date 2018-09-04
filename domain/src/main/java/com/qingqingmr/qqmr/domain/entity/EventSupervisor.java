package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 管理员事件记录
 *
 * @author ztl
 * @datetime 2018-7-3 20:29:20
 */
public class EventSupervisor {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 操作用户id
     */
    private Long supervisorId;
    /**
     * 事件类型 (枚举)
     */
    private Integer type;
    /**
     * 操作ip
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 枚举:管理员事件栏目
     *
     * @author ztl
     * @description
     */
    public enum ItemEnum {

        HOME(101, "首页"),

        SUPERVISORMANAGE(201, "员工管理"),

        SUPERVISOREINPUT(202, "员工录入"),

        MEMBERMANAGE(301, "客户管理"),

        SETTING(401, "平台概况"),

        AWARDMANAGE(501, "佣金管理"),

        ORDERMANAGE(601, "订单管理"),

        WITHDRAWMANAGE(701, "提现管理"),

        RUNMANAGE(801, "运营设置"),

        OPERATIONLOG(901, "系统日志管理"),

        FRAMEMANAGE(1001, "架构管理"),

        SETTINGMANAGE(1101, "系统管理");

        private int code;
        private String value;

        private ItemEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }


        public int getCode() {
            return code;
        }


        public void setCode(int code) {
            this.code = code;
        }


        public String getValue() {
            return value;
        }


        public void setValue(String value) {
            this.value = value;
        }


        public static ItemEnum getEnum(int code) {
            ItemEnum[] types = ItemEnum.values();
            for (ItemEnum type : types) {
                if (type.code == code) {

                    return type;
                }
            }

            return null;
        }
    }

    /**
     * @author ztl
     * 枚举:管理员操作事件
     */
    public enum SupervisorEventEnum {

        LOGIN(ItemEnum.HOME.code, "登录系统"),

        LOGOUT(ItemEnum.HOME.code, "退出系统"),

        HOME(ItemEnum.HOME.code, "查看首页"),

        /*----员工管理----*/

        SUPERVISOR_ADVISOR_SHOW(ItemEnum.SUPERVISORMANAGE.code, "查询顾问列表"),

        SUPERVISOR_ADVISOR_SHOW_DETAIL(ItemEnum.SUPERVISORMANAGE.code, "查询顾问详情"),

        SUPERVISOR_ADVISOR_ADD(ItemEnum.SUPERVISORMANAGE.code, "添加顾问"),

        SUPERVISOR_ADVISOR_PERSON_EDIT(ItemEnum.SUPERVISORMANAGE.code, "编辑顾问个人信息"),

        SUPERVISOR_BEAUTICIAN_SHOW(ItemEnum.SUPERVISORMANAGE.code, "查询美容师列表"),

        SUPERVISOR_BEAUTICIAN_SHOW_DETAIL(ItemEnum.SUPERVISORMANAGE.code, "查询美容师详情"),

        SUPERVISOR_BEAUTICIAN_ADD(ItemEnum.SUPERVISORMANAGE.code, "添加美容师"),

        SUPERVISOR_BEAUTICIAN_PERSON_EDIT(ItemEnum.SUPERVISORMANAGE.code, "编辑美容师个人信息"),

        /*----客户管理----*/

        SUPERVISOR_MEMBER_SHOW(ItemEnum.MEMBERMANAGE.code, "查询会员列表"),

        SUPERVISOR_MEMBER_DETAIL(ItemEnum.MEMBERMANAGE.code, "查询会员详情"),

        SUPERVISOR_CUSTOMER_SHOW(ItemEnum.MEMBERMANAGE.code, "查询顾客列表"),

        SUPERVISOR_CUSTOMER_SHOW_DETAIL(ItemEnum.MEMBERMANAGE.code, "查询顾客详情"),

        INVITE_EDIT(ItemEnum.MEMBERMANAGE.code, "更改邀请关系"),

        /*----平台概况----*/

        SETTING_SHOW(ItemEnum.SETTING.code, "查询平台概况"),

        SETTING_PAY_SHOW(ItemEnum.SETTING.code, "查询平台收取费用列表"),

        /*----佣金管理----*/

        AWARD_SHOW(ItemEnum.AWARDMANAGE.code, "查询佣金总表"),

        /*----订单管理----*/

        ORDER_SHOW(ItemEnum.ORDERMANAGE.code, "查询订单列表"),

        ORDER_SHOW_DETAIL(ItemEnum.ORDERMANAGE.code, "查询订单详情"),

        ORDER_QUIT(ItemEnum.ORDERMANAGE.code, "取消订单"),

        /*----提现管理----*/

        WITHDRAW_SHOW(ItemEnum.WITHDRAWMANAGE.code, "查询提现列表"),

        WITHDRAW_SHOW_DETAIL(ItemEnum.WITHDRAWMANAGE.code, "查询提现详情"),

        WITHDRAW_CHECK(ItemEnum.WITHDRAWMANAGE.code, "提现审核"),

        /*----运营设置----*/

        RUN_RULE(ItemEnum.RUNMANAGE.code, "设置运营规则"),

        SALE_RULE(ItemEnum.RUNMANAGE.code, "设置分销规则"),

        /*----系统日志管理----*/
        OPERATIONLOG_SHOW(ItemEnum.OPERATIONLOG.code, "查询操作日志列表"),

        /*----架构管理----*/

        FRAME_SHOW(ItemEnum.FRAMEMANAGE.code, "架构列表"),

        FRAME_DICT_ADD(ItemEnum.FRAMEMANAGE.code, "添加区域"),

        FRAME_DICT_EDIT(ItemEnum.FRAMEMANAGE.code, "编辑区域"),

        FRAME_SHOP_ADD(ItemEnum.FRAMEMANAGE.code, "添加美容店"),

        FRAME_SHOP_Edit(ItemEnum.FRAMEMANAGE.code, "编辑美容店"),

        /*----系统管理----*/

        SHOW_SUPERVISOR(ItemEnum.SETTINGMANAGE.code, "查询管理员列表"),

        ADD_SUPERVISOR(ItemEnum.SETTINGMANAGE.code, "添加管理员"),

        DEL_SUPERVISOR(ItemEnum.SETTINGMANAGE.code, "删除管理员"),

        EDIT_SUPERVISOR(ItemEnum.SETTINGMANAGE.code, "编辑管理员"),

        SHOW_SUPERVISOR_ROLE(ItemEnum.SETTINGMANAGE.code, "查询角色列表"),

        ADD_SUPERVISOR_ROLE(ItemEnum.SETTINGMANAGE.code, "添加角色"),

        EDIT_SUPERVISOR_ROLE(ItemEnum.SETTINGMANAGE.code, "编辑角色"),

        RIGHT_SUPERVISOR(ItemEnum.SETTINGMANAGE.code, "权限分配"),;

        private int code;
        private String value;

        private SupervisorEventEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }


        public void setCode(int code) {
            this.code = code;
        }


        public String getValue() {
            return value;
        }


        public void setValue(String value) {
            this.value = value;
        }

        public static SupervisorEventEnum getEnum(int code) {
            SupervisorEventEnum[] types = SupervisorEventEnum.values();
            for (SupervisorEventEnum type : types) {
                if (type.code == code) {

                    return type;
                }
            }

            return null;
        }
    }
}