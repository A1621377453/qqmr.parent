package com.qingqingmr.qqmr.base.spring;

import com.qingqingmr.qqmr.common.enums.ProfileEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Spring配置的系统属性
 *
 * @author ztl
 * @datetime 2018-7-4 16:13:25
 */
@Component
public class SpringContextProperties implements EnvironmentAware {

    /**
     * Spring当前环境
     */
    private Environment environment;

    /**
     * 后台加密串：MD5密码加密；DES参数加密/解密
     */
    @Value("${MD5_KEY}")
    private String md5Key;

    @Value("${DES_KEY}")
    private String desKey;

    /**
     * 小程序加密串：MD5接口签名校验；DES参数加密/解密
     */
    @Value("${APP_MD5_KEY}")
    private String appMd5Key;

    @Value("${APP_DES_KEY}")
    private String appDesKey;

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public String getDesKey() {
        return desKey;
    }

    public void setDesKey(String desKey) {
        this.desKey = desKey;
    }

    public String getAppMd5Key() {
        return appMd5Key;
    }

    public void setAppMd5Key(String appMd5Key) {
        this.appMd5Key = appMd5Key;
    }

    public String getAppDesKey() {
        return appDesKey;
    }

    public void setAppDesKey(String appDesKey) {
        this.appDesKey = appDesKey;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    /**
     * <p>
     * 获取当前运行激活的Profile
     * </p>
     *
     * @return
     * @author ztl
     * @datetime 2018-7-4 16:14:38
     */
    public String getProfile() {
        if (getEnvironment().getActiveProfiles() == null || getEnvironment().getActiveProfiles().length < 1) {
            if (getEnvironment().getDefaultProfiles() == null || getEnvironment().getDefaultProfiles().length < 1) {
                return ProfileEnum.DEV.getValue();
            }
            return getEnvironment().getDefaultProfiles()[0];
        }
        return getEnvironment().getActiveProfiles()[0];
    }


    /*******************************微信支付参数********************************/
    /**
     * 是否开启企业付款(0/1)
     */
    @Value("${IS_OPEN_PAY}")
    private String isOpenPay;
    /**
     * 企业付款到微信余额请求URL
     */
    @Value("${PAY_URL}")
    private String payUrl;
    /**
     * 查询企业付款的信息URL
     */
    @Value("${PAY_QUERY_URL}")
    private String payQueryUrl;
    /**
     * 平台登陆商户号
     */
    @Value("${MCHNT_CD}")
    private String mchntCd;
    /**
     * 提现用的CA证书
     */
    @Value("${CERT_URL}")
    private String certUrl;
    /**
     * 商户号对应的appid
     */
    @Value("${MCH_APP_ID}")
    private String mchAppId;
    /**
     * 小程序秘钥
     */
    @Value("${APP_SECRET}")
    private String appSecret;
    /**
     * 接口对接密钥
     */
    @Value("${API_SIGN}")
    private String apiSign;
    /**
     * 统一下单URL
     */
    @Value("${ORDER_URL}")
    private String orderUrl;
    /**
     * 统一下单查询URL
     */
    @Value("${ORDER_QUERY_URL}")
    private String orderQueryUrl;
    /**
     * 统一下单通知地址
     */
    @Value("${ORDER_CALLBACK_ASYN_URL}")
    private String orderCallbackAsynUrl;

    public String getIsOpenPay() {
        return isOpenPay;
    }

    public void setIsOpenPay(String isOpenPay) {
        this.isOpenPay = isOpenPay;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getPayQueryUrl() {
        return payQueryUrl;
    }

    public String getMchntCd() {
        return mchntCd;
    }

    public String getCertUrl() {
        return certUrl;
    }

    public String getMchAppId() {
        return mchAppId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getApiSign() {
        return apiSign;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public String getOrderQueryUrl() {
        return orderQueryUrl;
    }

    public String getOrderCallbackAsynUrl() {
        return orderCallbackAsynUrl;
    }
}