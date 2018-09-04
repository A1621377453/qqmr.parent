package com.qingqingmr.qqmr.common.constant;

/**
 * 系统常量
 * @author crn
 * @datetime 2018-07-04 18:12:46
 */
public class SystemConstant {
	/** 用户名前缀 */
	public static final String NAME_PREFIX = "QQMR_";

    /** 项目根路径 */
    public static final String APPLICATION_BASEURL = "127.0.0.1";

    /** 后台用户登陆，保存session字符串 */
    public static final String SESSION_SUPERVISOR = "back_supervisor";

	/** 后台用户登陆，保存cookie名称 */
    public static final String COOKIE_NAME = "username";

	/** 后台用户登陆，保存cookie有效时长 */
    public static final int COOKIE_SUPERVISOR = 24 * 3600;

    /** 后台用户登陆，连续错误次数 */
    public static final int CURR_LOGIN_FAIL_COUNT = 3;

    /** 后台用户登陆，连续错误次数时间间隔 */
    public static final long FAIL_COUNT_TIME = 30L;

    /** 后台更改邀请人短信通知模板 */
    public static final String SMS_TEMPLATE = "【清轻美容】[姓名/昵称]您好，系统管理员于[年-月-日-时-分-秒]将会员[昵称]，手机号[手机号]与您绑定邀请关系，请知悉！";

	/******************** 日期格式化标准 *****************************/
	/** 日期时间格式化 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式化 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 时间格式化 */
	public static final String TIME_FORMAT = "HH:mm:ss";
	/** 日期时间格式化-后台 */
	public static final String DATE_TIME_FORMAT_BACK = "yy/MM/dd HH:mm:ss";
	/** 日期时间格式化-支付使用 */
	public static final String DATE_TIME_FORMAT_FULL = "yyyyMMddHHmmss";

	/** 一天两端时间 */
	public static final String STARTTIME = " 00:00:00";
	public static final String ENDTIME = " 23:59:59";

	/** 缓存时间:60s */
	public static final int CACHE_TIME_SECOND_60 = 60;
	/** 替换短信验证码 */
	public static final String SMS_CODE = "sms_code";
	/** 短信验证码，缓存时间:30min */
	public static final int CACHE_TIME_MINUTE_30 = 30;
	/** redis缓存系统常量的时间 */
	public static final int CACHE_TIME_DAY_30 = 30;
	/** redis缓存系统常量的时间 */
	public static final int CACHE_TIME_HOUR_2 = 2;

	/******************** 数据加密参数 *****************************/
	/** 加密串有效时间(s) */
	public static final int VALID_TIME = 3600;
	/** 加密action标识:用户ID */
	public static final String ACTION_USER_ID_SIGN = "user";
	/** 加密action标识:订单ID */
	public static final String ORDER_ID_SIGN = "order";



}
