package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.constant.WechatConstant;
import com.qingqingmr.qqmr.common.util.Encrypt;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.Security;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制层测试
 *
 * @author ythu
 * @datetime 2018-07-14 15:42:13
 */
public class ControllerTest {

    @Test
    public void test01() {
        Map<String, String> params = new HashMap<>();
        params.put("OPT", WechatConstant.WECHAT_QRCODE + "");
        params.put("userId", Security.addSign(1, SystemConstant.ACTION_USER_ID_SIGN, "r17XbJeR"));
        final StringBuffer buffer = new StringBuffer();
        params.forEach((k, v) -> buffer.append(k).append("=").append(v).append("&"));
        buffer.deleteCharAt(buffer.length() - 1);
        String _s = Encrypt.MD5(buffer.toString() + "4COmdnnJ3RWZx77x", Charset.forName("UTF-8"));
        params.put("_s", _s);
        System.out.println(FastJsonUtil.toJsonString(params));
    }
}
