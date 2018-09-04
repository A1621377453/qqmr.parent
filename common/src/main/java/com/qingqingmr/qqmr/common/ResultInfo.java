package com.qingqingmr.qqmr.common;

/**
 * <p>
 * 结果信息
 * </p>
 *
 * @author ythu
 * @datetime 2018年1月17日 下午4:47:45
 */
public class ResultInfo {
    /**
     * 数据为空提示信息
     */
    public static final String STR_NULL_OBJ = "亲，数据为空";

    /**
     * 资金异常
     */
    public static final String STR_SIGN_ERROR = "亲，您的资金异常了";

    /**
     * 解析userId有误
     */
    public static final int ERROR_100 = -100;

    /**
     * 系统繁忙
     */
    public static final int ERROR_500 = -500;

    /**
     * 数据已经更新
     */
    public static final int ALREADY_RUN = -101;

    /**
     * 消息码 -- 默认：code = 0 ; 成功：code > 0; 失败：code < 1
     */
    private int code = 0;

    /**
     * 提示信息
     */
    private String msg = "亲，系统繁忙！";

    /**
     * 结果集中的对象
     */
    private Object obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public ResultInfo(int code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public ResultInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultInfo() {
    }

    /**
     * <p>
     * 设置内容
     * </p>
     *
     * @param code
     * @param msg
     * @param obj
     * @author ythu
     * @datetime 2018年1月17日 下午4:52:26
     */
    public void setInfo(int code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    /**
     * <p>
     * 设置内容
     * </p>
     *
     * @param code
     * @param msg
     * @author ythu
     * @datetime 2018年1月17日 下午4:52:26
     */
    public void setInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ///////////////////////////////////////////////////////////
    // 系统提示登录编码
    public static final int EXCEPTION_CODE_1 = -9999;
    // 系统提示登录信息
    public static final String EXCEPTION_MSG_1 = "您还未登录，请先登录";

    // 系统提示权限编码
    public static final int EXCEPTION_CODE_2 = -9998;
    // 系统提示权限信息
    public static final String EXCEPTION_MSG_2 = "对不起，您没有操作权限";

    // 系统提示重复提交编码
    public static final int EXCEPTION_CODE_3 = -9997;
    // 系统提示重复提交信息
    public static final String EXCEPTION_MSG_3 = "请勿重复提交";

    // 系统异常编码
    public static final int EXCEPTION_CODE_500 = -9996;
    // 系统异常信息
    public static final String EXCEPTION_MSG_500 = "请求页面丢失";

    // 系统异常编码
    public static final int EXCEPTION_CODE_404 = -9995;
    // 系统异常信息
    public static final String EXCEPTION_MSG_404 = "系统异常";
    ///////////////////////////////////////////////////////////
}
