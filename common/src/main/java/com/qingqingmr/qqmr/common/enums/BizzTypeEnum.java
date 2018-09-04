package com.qingqingmr.qqmr.common.enums;

/**
 * <p>
 * 业务标识
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-10 16:29:13
 */
public enum BizzTypeEnum {

    REPAYMENT(1, "P", "微信支付业务标识", 2),

    MAKE_LOAN(2, "M", "企业付款到余额业务标识", 2),

    RAKE_BACK(3, "R", "返佣业务标识", 1),

    PURCHASE_GOODS(4, "G", "购买商品", 2),

    MANAGE_FEE(5, "F", "扣除管理费", 2);;

    private int code;
    private String prefix;
    private String desc;
    private int dealType; // 1表示收入，2表示支出

    private BizzTypeEnum(int code, String prefix, String desc, int dealType) {
        this.code = code;
        this.prefix = prefix;
        this.desc = desc;
        this.dealType = dealType;
    }

    public int getCode() {
        return code;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDesc() {
        return desc;
    }

    public int getDealType() {
        return dealType;
    }
}
