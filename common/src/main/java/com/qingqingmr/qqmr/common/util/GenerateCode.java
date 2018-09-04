package com.qingqingmr.qqmr.common.util;

import java.util.*;

/**
 * <p>
 * 自动生成编码
 * </p>
 *
 * @author ZTL
 * @datetime 2018-7-6 17:59:22
 */
public class GenerateCode {

    /**
     * <p>
     * 获得多个编码
     * </p>
     *
     * @param length
     * @param num
     * @return
     * @author ZTL
     * @datetime 2018-7-6 17:59:22
     */
    public static List<String> genCodes(int length, long num) {
        List<String> results = new ArrayList<String>();
        for (int j = 0; j < num; j++) {
            String val = genCode(length);
            if (results.contains(val)) {
                continue;
            } else {
                results.add(val);
            }
        }
        return results;
    }

    /**
     * <p>
     * 获得一个编码
     * </p>
     *
     * @param length
     * @return
     * @author ZTL
     * @datetime 2018-7-6 17:59:22
     */
    public static String genCode(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * <p>
     * 获取唯一标识
     * </p>
     *
     * @return
     * @author ZTL
     * @datetime 2018-7-6 17:59:22
     */
    public static String getMark() {

        return UUID.randomUUID().toString();
    }

    /**
     * <p>
     * 获取订单号：长度19位
     * </p>
     *
     * @param bizzType
     * @return
     * @author ZTL
     * @datetime 2018-7-6 17:59:22
     */
    public static String getOrderNo(String bizzType) {

        return bizzType + DateUtil.dateToString(new Date(), "yyMMddHHMMss") + genCode(6).toUpperCase();
    }

    /**
     * <p>
     * 获取纯数字流水号:长度26位
     * </p>
     *
     * @return
     * @author ZTL
     * @datetime 2018-7-6 17:59:22
     */
    public static String getOrderNoDigital() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 14; i++) {
            buffer.append(random.nextInt(10));
        }
        return DateUtil.dateToString(new Date(), "yyMMddHHMMss") + buffer.toString();
    }
}
