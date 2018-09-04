package com.qingqingmr.qqmr.common.constant;

/**
 * 微信接口定义
 *
 * @author liujingjing
 * @datetime 2018年7月11日 下午3:25:59
 */
public final class WechatConstant {

    /**
     * 微信用户进入小程序
     */
    public static final int WECHAT_ENTER = 101;

    /**
     * 获取所有门店区域信息
     */
    public static final int WECHAT_GET_STOREAREA = 102;

    /**
     * 通过门店区域id获取关联的门店列表
     */
    public static final int WECHAT_GET_STORE = 103;

    /**
     * 绑定微信用户
     */
    public static final int WECHAT_BIND = 104;

    /**
     * VIP特权详情页
     */
    public static final int WECHAT_VIP_PRIVILEGE = 105;

    /**
     * 新增订单
     */
    public static final int WECHAT_ADD_ORDER = 106;

    /**
     * 添加提现记录
     */
    public static final int WECHAT_ADD_WITHDRAWAL = 107;

    /**
     * 账户设置-查询个人信息
     */
    public static final int WECHAT_ACCOUNT_SET = 201;

    /**
     * 修改昵称
     */
    public static final int WECHAT_NICKNAME_UPDATE = 202;

    /**
     * 修改性别
     */
    public static final int WECHAT_SEX_UPDATE = 203;

    /**
     * 添加收货地址
     */
    public static final int WECHAT_ADDRESS_SAVE = 204;

    /**
     * 编辑收货地址
     */
    public static final int WECHAT_ADDRESS_UPDATE = 205;

    /**
     * 更改手机号码
     */
    public static final int WECHAT_MOBILE_UPDATE = 206;

    /**
     * 设置交易密码
     */
    public static final int WECHAT_PAYPASSWORD_SAVE = 207;

    /**
     * 校验交易密码
     */
    public static final int WECHAT_CHECK_PAYPASSWORD = 208;

    /**
     * 查询用户收货地址
     */
    public static final int WECHAT_USER_ADDRESS = 209;

    /**
     * 查询总资产
     */
    public static final int WECHAT_TOTALASSETS_SELECT = 301;

    /**
     * 查询提现记录
     */
    public static final int WECHAT_WITHDRAWAL_RECORD = 302;

    /**
     * 查询我的邀请码
     */
    public static final int WECHAT_INVITECODE_SELECT = 303;

    /**
     * 我的邀请二维码
     */
    public static final int WECHAT_QRCODE = 304;

    /**
     * 查询佣金明细
     */
    public static final int WECHAT_BONUS_RECORD = 305;

    /**
     * 查询用户邀请会员列表
     */
    public static final int WECHAT_INVITEMEMBER_RECORD = 306;

    /**
     * 查询邀请信息
     */
    public static final int WECHAT_MYINVITE = 307;

    /**
     * 微信二维码
     */
    public static final int WECHAT_WX_CODE_QRCODE = 308;

    /**
     * 查询资金流水
     */
    public static final int WECHAT_DEAL_RECORD = 401;

    /**
     * 查询我的订单
     */
    public static final int WECHAT_ORDER_RECORD = 402;

    /**
     * 查询订单详情
     */
    public static final int WECHAT_ORDER_DETAIL = 403;

    /**
     * 取消订单
     */
    public static final int WECHAT_ORDER_CANCEL = 404;

    /**
     * 添加收货地址时查询省市区
     */
    public static final int WECHAT_GET_AREA = 405;

    /**
     * 获取输入提现金额页面的金额信息(账户余额、可提现金额)
     */
    public static final int WECHAT_GET_WITHDRAWAL = 406;

    /**
     * 订单支付
     */
    public static final int WECHAT_PAY_ORDER = 407;

    /**
     * 查询当前用户是否有未完成的订单
     */
    public static final int WECHAT_GET_NOTFINISH_ORDER = 408;

    /**
     * 添加服务记录
     */
    public static final int WECHAT_ADD_SERVICE = 409;

    /**
     * 生成会员卡二维码
     */
    public static final int WECHAT_MEMBERSHIPCARD_QRCODE = 410;

    /**
     * 发送验证码
     */
    public static final int WECHAT_SEND_CODE = 411;

    /**
     * 验证验证码
     */
    public static final int WECHAT_VERIFYCODEE = 412;

    /**
     * 添加订单时，验证用户手机号
     */
    public static final int WECHAT_CHECK_USERMOBILE = 413;

    /**
     * 删除用户收货地址
     */
    public static final int WECHAT_DELETE_USERADDRESS = 414;

    private WechatConstant() {
    }
}
