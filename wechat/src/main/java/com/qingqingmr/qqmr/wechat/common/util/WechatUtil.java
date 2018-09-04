package com.qingqingmr.qqmr.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序工具类
 *
 * @author ythu
 * @datetime 2018-07-24 14:15:38
 */
public final class WechatUtil {
    private final static Logger LOG = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 获取用户的openId
     *
     * @param url
     * @param jsCode
     * @param appId
     * @param appSecret
     * @param grantType
     * @return
     */
    public static String getOpenId(String url, String jsCode, String appId, String appSecret, String grantType) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url).append("?appid=").append(appId).append("&secret=").append(appSecret).append("&js_code=").append(jsCode).append("&grant_type=").append(grantType);
        LOG.info("获取用户的openId请求的url--{}", buffer.toString());

        String result = null;
        try {
            result = HttpUtil.httpRequest(buffer.toString(), "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        LOG.info("获取用户的openId的响应数据--{}", result);
        if (StringUtils.isBlank(result)) {
            LOG.info("获取用户的openId的响应数据为空");
            return null;
        }
        // 用户的唯一标识
        JSONObject json = JSONObject.parseObject(result);
        return json.getString("openid");
    }

    /**
     * 获取access_token
     *
     * @param url
     * @param appId
     * @param appSecret
     * @param grantType
     * @return
     */
    public static String getAccessToken(String url, String appId, String appSecret, String grantType) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url).append("?appid=").append(appId).append("&secret=").append(appSecret).append("&grant_type=").append(grantType);
        LOG.info("获取access_token请求的url--{}", buffer.toString());

        String result = null;
        try {
            result = HttpUtil.httpRequest(buffer.toString(), "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        LOG.info("获取access_token响应数据--{}", result);
        if (StringUtils.isBlank(result)) {
            LOG.info("获取access_token响应数据为空");
            return null;
        }
        // 成功：{"access_token":"ACCESS_TOKEN","expires_in":7200}
        // 失败：{"errcode":40013,"errmsg":"invalid appid"}
        return result;
    }

    /**
     * 获取二维码(B方案)
     *
     * @param url
     * @param accessToken
     * @param scene
     * @param page
     * @return
     */
    public static byte[] getWxCode(String url, String accessToken, String scene, String page) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url).append("?access_token=").append(accessToken);
        LOG.info("获取二维码(B方案)请求的url--{}", buffer.toString());
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("scene", URLEncoder.encode(scene, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        map.put("page", page);
        map.put("width", 430);
        map.put("auto_color", false);
        map.put("line_color", FastJsonUtil.parseObj("{'r':'0','g':'0','b':'0'}", JSONObject.class));
        map.put("is_hyaline", false);
        LOG.info("获取二维码(B方案)请求参数--{}", FastJsonUtil.toJsonString(map));
        byte[] bytes = null;
        try {
            bytes = HttpUtil.requestPost(buffer.toString(), map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (bytes == null) {
            LOG.info("获取二维码(B方案)响应数据为空");
            return null;
        }
        // LOG.info("获取二维码(B方案)响应数据--{}", Base64.encodeBase64String(bytes));
        return bytes;
    }
}
