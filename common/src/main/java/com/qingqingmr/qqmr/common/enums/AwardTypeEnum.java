package com.qingqingmr.qqmr.common.enums;

/**
 * 奖励的枚举类
 *
 * @author ztl
 * @datetime 2018-7-3 20:01:07
 */
public enum AwardTypeEnum {
    NO_DIRECT_AWARD(0, "非直推奖"),

    DIRECT_AWARD(1, "直推奖"),

    COOPERATE_AWARD(2, "合作奖"),

    ONE_AWARD(3, "一级奖"),

    TWO_AWARD(4, "二级奖"),

    NUMBER_AWARD(5, "人头奖"),

    DOUBLE_AWARD(6, "双人奖");

    public int code;
    public String value;

    private AwardTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static AwardTypeEnum getAwardType(int code) {
        for (AwardTypeEnum type : AwardTypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
