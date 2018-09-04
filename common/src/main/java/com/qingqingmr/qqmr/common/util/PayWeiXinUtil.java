package com.qingqingmr.qqmr.common.util;

import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信支付工具类
 *
 * @author ztl
 * @datetime 2018-07-13 14:49:45
 */
public class PayWeiXinUtil {

    /**
     * 企业付款签名
     *
     * @param params
     * @param apiKey
     * @return
     */
    public static String sign(Map<String, String> params, String apiKey) {
        StringBuffer sb = new StringBuffer();
        final List<String> keys = new ArrayList<>();
        params.keySet().forEach(k -> keys.add(k));
        List<String> list = keys.stream().filter(k -> !"sign".equals(k) && !"key".equals(k)).sorted().collect(Collectors.toList());
        list.forEach(k -> {
            if (StringUtils.isNotBlank(params.get(k))) {
                sb.append(k).append("=").append(params.get(k)).append("&");
            }
        });
        sb.append("key=").append(apiKey);
        String sign = Encrypt.MD5(sb.toString(), "utf-8").toUpperCase();
        return sign;
    }

    /**
     * 组装请求的xml
     *
     * @param params
     * @return
     */
    public static String getRequestXml(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        final List<String> keys = new ArrayList<>();
        params.keySet().forEach(k -> keys.add(k));
        keys.forEach(k -> {
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + params.get(k) + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + params.get(k) + "</" + k + ">");
            }
        });
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * XML转换成JSONObject【fastjson】
     *
     * @param xml
     * @return
     */
    public static JSONObject parseXmlToJson(String xml) {

        return FastJsonUtil.parseObj(XML.toJSONObject(xml).getJSONObject("xml").toString(), JSONObject.class);
    }

	/**
	 * 返回微信通知结果
	 * 
	 * @param returnCode
	 * @param returnMsg
	 * @return
	 */
	public static String renderWechatAsyn(String returnCode, String returnMsg) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<xml>");
		buffer.append("<return_code><![CDATA[").append(returnCode).append("]]><return_code>");
		buffer.append("<return_msg><![CDATA[").append(returnMsg).append("]]><return_msg>");
		buffer.append("</xml>");
		return buffer.toString();
	}
}
