package com.qingqingmr.qqmr.web;

import com.qingqingmr.qqmr.common.util.Security;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>
 * </p>
 *
 * @author chenrenning
 * @datetime 2018/7/3 18:03
 */
public class StringTester {
    public static void main(String args[]) {
        List names = new ArrayList();

        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.add("Baidu");
        names.add("Sina");

        names.forEach(System.out::println);
        for (Object name : names) {
            System.out.println("当前类Java8Tester的main方法输出：" + name);
        }
    }

    @Test
    public void test1() {
        String a = "12";
        String b = "1";
        System.out.println("当前类StringTester的test1方法输出：" + StringUtils.equals(a, b));
        if (StringUtils.equals(a, b)) {

        }
    }

    @Test
    public void test3() {
        String[] datas = new String[]{"peng", "zhang", "li"};
//        Arrays.sort(datas);
        Arrays.sort(datas, (v1, v2) -> Integer.compare(v1.length(), v2.length()));
        Stream.of(datas).forEach(param -> System.out.println(param));
    }

    @Test
    public void test4() {
        String s = "0.00";
        double f = Double.parseDouble(s);
        double value = new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(value);
    }
    @Test
    public void test5() {
        System.out.println(Security.encodeHexString("顾问"));
    }
    @Test
    public void test6() throws UnsupportedEncodingException {
        System.out.println(Security.decodeHex("e69ca8e581b6e5a883e5a883f09f988a"));
    }
}
