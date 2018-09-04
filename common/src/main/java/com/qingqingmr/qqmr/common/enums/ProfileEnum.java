package com.qingqingmr.qqmr.common.enums;

/**
 * Profile环境
 *
 * @author ztl
 * @datetime 2018-7-4 16:16:21
 */
public enum ProfileEnum {
    DEV(1, "dev"), TEST(2, "test"), PROD(3, "prod");

    private int code;
    private String value;

    private ProfileEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ProfileEnum getEnum(int code) {
        ProfileEnum[] profiles = ProfileEnum.values();
        for (ProfileEnum profile : profiles) {
            if (profile.code == code) {

                return profile;
            }
        }

        return DEV;
    }

    public static ProfileEnum getEnum(String value) {
        ProfileEnum[] profiles = ProfileEnum.values();
        for (ProfileEnum profile : profiles) {
            if (profile.value.equals(value)) {

                return profile;
            }
        }

        return DEV;
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
}
