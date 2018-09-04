package com.qingqingmr.qqmr.common.util;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据安全加密
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午7:16:49
 */
public class Security {

    /**
     * <p>
     * id加密
     * </p>
     *
     * @param id            需要加密的id
     * @param action        固定字符串
     * @param encryptionKey 加密key
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午6:33:03
     */
    public static String addSign(long id, String action, String encryptionKey) {
        String des = Encrypt.encryptDES(
                id + "," + action + "," + DateUtil.dateToString(new Date(), SystemConstant.DATE_TIME_FORMAT),
                encryptionKey);
        String md5 = Encrypt.MD5(des + encryptionKey);
        String sign = des + md5.substring(0, 8);
        return sign;
    }

    //去掉有效时间的加密
    public static String addWXSign(long id, String action, String encryptionKey) {
        String des = Encrypt.encryptDES(
                id + "," + action + "," + "", encryptionKey);
        String md5 = Encrypt.MD5(des + encryptionKey);
        String sign = des + md5.substring(0, 8);
        return sign;
    }

    /**
     * <p>
     * id解密
     * </p>
     *
     * @param sign          加密串
     * @param action        固定字符串
     * @param validLength   有效时间
     * @param encryptionKey 解密key
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午6:34:18
     */
    public static ResultInfo decodeSign(String sign, String action, int validLength, String encryptionKey) {
        ResultInfo result = new ResultInfo();
        if (StringUtils.isBlank(sign) || sign.length() < 8) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        String des = sign.substring(0, sign.length() - 8);
        String key = sign.substring(sign.length() - 8);
        String md5 = Encrypt.MD5(des + encryptionKey);
        if (!key.equals(md5.substring(0, 8))) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        String[] decryArray = Encrypt.decryptDES(des, encryptionKey).split(",");
        if (decryArray.length != 3) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        if (!decryArray[1].equals(action)) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        Date validTime = DateUtil.strToDate(decryArray[2], SystemConstant.DATE_TIME_FORMAT);
        if (validTime == null) {
            result.setInfo(-1, "无效请求");
            return result;
        }
        if (!DateUtil.isInValidTime(validTime, validLength)) {
            result.setInfo(-1, "请求时间已过期，请重新请求");
            return result;
        }

        if (!StrUtil.isNumericInt(decryArray[0])) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        result.setInfo(1, "解密成功");
        result.setObj(Long.parseLong(decryArray[0]));

        return result;
    }

    public static ResultInfo decodeWXSign(String sign, String action, String encryptionKey) {
        ResultInfo result = new ResultInfo();
        if (StringUtils.isBlank(sign) || sign.length() < 8) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        String des = sign.substring(0, sign.length() - 8);
        String key = sign.substring(sign.length() - 8);
        String md5 = Encrypt.MD5(des + encryptionKey);
        if (!key.equals(md5.substring(0, 8))) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        String[] decryArray = Encrypt.decryptDES(des, encryptionKey).split(",");
        if (decryArray.length != 2) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        if (!decryArray[1].equals(action)) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        if (!StrUtil.isNumericInt(decryArray[0])) {
            result.setInfo(-1, "无效请求");
            return result;
        }

        result.setInfo(1, "解密成功");
        result.setObj(Long.parseLong(decryArray[0]));

        return result;
    }

    /**
     * 创建账户的sign
     *
     * @param userId
     * @param balance
     * @param freeze
     * @param md5Sign
     * @return
     */
    public static String decodeFunds(long userId, double balance, double freeze, String md5Sign) {
        return Encrypt.MD5(userId + Arith.formatDouble(balance) + Arith.formatDouble(freeze) + md5Sign, "utf-8");
    }

    /**
     * 微信网关请求参数加密
     *
     * @param map
     * @param md5Key
     * @return
     */
    public static String wxGatewaySign(Map<String, String> map, String md5Key) {
        final List<String> keys = new ArrayList<>();
        map.forEach((k, v) -> keys.add(k));
        List<String> _keys = keys.stream().filter(k -> !"_s".equals(k)).sorted().collect(Collectors.toList());
        final StringBuffer buffer = new StringBuffer();
        _keys.forEach(k -> buffer.append(k).append("=").append(map.get(k)).append("&"));
        buffer.deleteCharAt(buffer.length() - 1);
        return Encrypt.MD5(buffer.toString() + md5Key, Charset.forName("UTF-8"));
    }

    /**
     * 微信网关请求构建url
     *
     * @param map
     * @param md5Key
     * @return
     */
    public static String wxGatewayBuildUrl(Map<String, String> map, String md5Key) {
        StringBuffer buffer = new StringBuffer();
        map.put("_s", wxGatewaySign(map, md5Key));
        buffer.append("?");
        map.forEach((k, v) -> buffer.append(k).append("=").append(v).append("&"));
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    /**
     * 十六进制解码
     *
     * @param hex 十六进制字符串
     * @return
     */
    public static String decodeHex(String hex) {
        if (StringUtils.isBlank(hex)) {
            return "";
        }
        if (!StrUtil.isHexStr(hex)) {
            return hex;
        }
        try {
            return new String(Hex.decodeHex(hex.toCharArray()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 十六进制编码
     *
     * @param str 普通字符串
     * @return
     */
    public static String encodeHexString(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        try {
            return Hex.encodeHexString(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
