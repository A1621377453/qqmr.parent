package com.qingqingmr.qqmr.wechat.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信小程序参数
 *
 * @author ythu
 * @datetime 2018-07-24 13:54:40
 */
@Component
public class WechatProperties {

    /*****************微信小程序参数*****************************/
    /**
     * 换取openId的地址
     */
    @Value("${JS_CODE_SESSION_URL}")
    private String jsCodeSessionUrl;

    /**
     * 授权类型
     */
    @Value("${GRANT_TYPE}")
    private String grantType;

    /**
     * 获取二维码请求地址(B方案)
     */
    @Value("${WX_CODE_UN_LIMIT_URL}")
    private String wxCodeUnLimitUrl;

    public String getJsCodeSessionUrl() {
        return jsCodeSessionUrl;
    }

    public void setJsCodeSessionUrl(String jsCodeSessionUrl) {
        this.jsCodeSessionUrl = jsCodeSessionUrl;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getWxCodeUnLimitUrl() {
        return wxCodeUnLimitUrl;
    }

    public void setWxCodeUnLimitUrl(String wxCodeUnLimitUrl) {
        this.wxCodeUnLimitUrl = wxCodeUnLimitUrl;
    }

    /*******************获取微信access_token参数************************/
    /**
     * 请求地址
     */
    @Value("${ACCESS_TOKEN_URL}")
    private String accessTokenUrl;

    /**
     * grant type
     */
    @Value("${ACCESS_TOKEN_GRANT_TYPE}")
    private String accessTokenGrantType;

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getAccessTokenGrantType() {
        return accessTokenGrantType;
    }

    public void setAccessTokenGrantType(String accessTokenGrantType) {
        this.accessTokenGrantType = accessTokenGrantType;
    }
}
