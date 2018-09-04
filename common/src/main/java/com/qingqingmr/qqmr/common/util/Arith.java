package com.qingqingmr.qqmr.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p>
 * 算术运算工具类
 * </p>
 *
 * @author ythu
 * @datetime 2018年1月30日 下午5:34:55
 */
public final class Arith {
    /**
     * 加法运算
     *
     * @param valA 加数
     * @param valB 加数
     * @return
     */
    public static double add(double valA, double valB) {
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.add(b).doubleValue();
    }

    /**
     * 减法运算
     *
     * @param valA 被减数A
     * @param valB 减数B
     * @return 差
     */
    public static double sub(double valA, double valB) {
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.subtract(b).doubleValue();
    }

    /**
     * 乘法运算
     *
     * @param valA 被乘数A
     * @param valB 乘数B
     * @return 积
     */
    public static double mul(double valA, double valB) {
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.multiply(b).doubleValue();
    }

    /**
     * 除法运算
     *
     * @param valA  被除数
     * @param valB  除数
     * @param scale 精度
     * @return
     */
    public static double div(double valA, double valB, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("精确度不能小于0！");
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.divide(b, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 除法运算
     *
     * @param valA  被除数
     * @param valB  除数
     * @param scale 精度
     * @return
     */
    public static double divUp(double valA, double valB, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("精确度不能小于0！");
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.divide(b, scale, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 除法运算
     *
     * @param valA  被除数
     * @param valB  除数
     * @param scale 精度
     * @return
     */
    public static double divDown(double valA, double valB, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("精确度不能小于0！");
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.divide(b, scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param val   原始数字
     * @param scale
     * @return 四舍五入后的数字
     */
    public static double round(double val, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("精确度不能小于0！");
        BigDecimal b = new BigDecimal(Double.toString(val));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * <p>
     * 小数点右移动2位（放大）
     * </p>
     *
     * @param amount
     * @return
     * @author ythu
     * @datetime 2018年3月14日 下午3:06:13
     */
    public static String pointToRight(double amount) {

        return String.valueOf((int) round(mul(amount, 100), 0));
    }

    /**
     * <p>
     * 小数点左移动2位（缩小）
     * </p>
     *
     * @param amount
     * @return
     * @author ythu
     * @datetime 2018年3月14日 下午3:06:36
     */
    public static double pointToLeft(double amount) {

        return div(amount, 100, 2);
    }

    /**
     * 格式化double(保留两位小数#.00)
     *
     * @param number
     * @return
     */
    public static String formatDouble(double number) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(number);
    }

    /**
     * 格式化Long(四位字符串0000)
     *
     * @param number
     * @return
     */
    public static String formatLong(Long number) {
        DecimalFormat df = new DecimalFormat("0000");
        return df.format(number);
    }
}
