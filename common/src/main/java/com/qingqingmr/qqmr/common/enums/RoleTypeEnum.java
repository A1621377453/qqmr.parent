package com.qingqingmr.qqmr.common.enums;

/**
 * <p>
 * 角色枚举
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月3日 下午8:27:27
 */
public enum RoleTypeEnum {
    /**
     * 顾客
     */
    CUSTOMER(1, "顾客"),

    /**
     * 会员
     */
    MEMBER(2, "会员"),

    /**
     * 美容师
     */
    BEAUTICIAN(3, "美容师"),

    /**
     * 顾问
     */
    ADVISER(4, "顾问");

    public int code;
    public String value;

    private RoleTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static RoleTypeEnum getEnum(int code) {
        RoleTypeEnum[] roleTypeEnums = RoleTypeEnum.values();
        for (RoleTypeEnum roleTypeEnum : roleTypeEnums) {
            if (roleTypeEnum.code == code) {

                return roleTypeEnum;
            }
        }

        return null;
    }
}
