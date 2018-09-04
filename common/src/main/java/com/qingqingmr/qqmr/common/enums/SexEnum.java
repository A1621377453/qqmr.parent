package com.qingqingmr.qqmr.common.enums;

/**
 * 性别枚举
 * @author crn
 * @datetime 2018-07-05 14:39:37
 */
public enum SexEnum {
    /**
     * 未知
     */
    UNKNOWN(0, "--"),
    /**
     * 男
     */
    MAN(1, "男"),

    /**
     * 女
     */
    WOMAN(2, "女");

    public int code;
    public String value;

    private SexEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SexEnum getEnum(int code) {
        SexEnum[] status = SexEnum.values();
        for (SexEnum statu : status) {
            if (code == statu.code) {
                return statu;
            }
        }
        return null;
    }
}
