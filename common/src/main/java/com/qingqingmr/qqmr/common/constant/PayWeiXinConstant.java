package com.qingqingmr.qqmr.common.constant;

/**
 * 支付常量
 *
 * @author ztl
 * @datetime 2018-07-10 15:21:40
 */
public final class PayWeiXinConstant {

    /**
     * <p>
     * 支付类型
     * </p>
     *
     * @author ztl
     * @datetime 2018-7-10 16:56:37
     */
    public final class PayType {
        /**
         * 企业付款
         */
        public static final int PAY = 101;

        /**
         * 用户支付
         */
        public static final int HOLD = 102;
    }

    /**
     * <p>
     * 返回状态码
     * </p>
     *
     * @author ztl
     * @datetime 2018-7-10 16:56:37
     */
    public final class ReturnCode {
        // 成功
        public final static String SUCCESS = "SUCCESS";

        // 失败：当状态为FAIL时，存在业务结果未明确的情况，所以如果状态FAIL，请务必再请求一次查询接口
        public final static String FAIL = "FAIL";
    }

    /**
     * <p>
     * 业务结果
     * </p>
     *
     * @author ztl
     * @datetime 2018-7-10 16:56:37
     */
    public final class ResultCode {
        // 成功
        public final static String SUCCESS = "SUCCESS";

        // 失败
        public final static String FAIL = "FAIL";
    }

    /**
     * <p>
     * 企业转账查询最终状态
     * </p>
     *
     * @author ztl
     * @datetime 2018-7-10 16:56:37
     */
    public final class TransferStatus {
        // 转账成功
        public final static String SUCCESS = "SUCCESS";

        // 转账失败
        public final static String FAIL = "FAIL";

        // 处理中
        public final static String PROCESSING = "PROCESSING";
    }

    /**
     * 校验用户姓名选项
     */
    // 不校验真实姓名
    public final static String CHECK_NAME_1 = "NO_CHECK";
    // 强校验真实姓名
    public final static String CHECK_NAME_2 = "FORCE_CHECK";

    /**
     * 企业付款操作说明信息
     */
    // 用户提现
    public final static String DESC_1 = "企业付款到账户余额";

    //拒绝提现
    public final static String REFUSE_WITHDRAW = "拒绝提现";

    /**
     * 微信支付--加密方式
     */
    // MD5-默认
    public final static String SIGN_TYPE_MD5 = "MD5";
    // HMAC-SHA256
    public final static String SIGN_TYPE_HMAC = "HMAC-SHA256";

    /**
     * 微信支付--商家名称
     */
    // 商家名称
    public final static String BUSINESS_NAME = "清轻美容";

    /**
     * 微信支付--标价币种
     */
    // 人民币
    public final static String FEE_TYPE_CNY = "CNY";

    /**
     * 微信支付--指定支付方式
     */
    // 可限制用户不能使用信用卡支付
    public final static String LIMIT_PAY = "no_credit";

    /**
     * 微信支付--交易类型
     */
    // 小程序取值如下
    public final static String TRADE_TYPE_01 = "JSAPI";

    /**
     * 微信支付--交易状态
     *
     * @author liujingjing
     * @datetime 2018年7月15日 上午11:44:49
     */
    public class OrderTradeState {
        // 支付成功
        public static final String SUCCESS = "SUCCESS";

        // 转入退款
        public static final String REFUND = "REFUND";

        // 未支付
        public static final String NOTPAY = "NOTPAY";

        // 已关闭
        public static final String CLOSED = "CLOSED";

        // 已撤销（刷卡支付）
        public static final String REVOKED = "REVOKED";

        // 用户支付中
        public static final String USERPAYING = "USERPAYING";

        // 支付失败(其他原因，如银行返回失败)
        public static final String PAYERROR = "PAYERROR";
    }
}
