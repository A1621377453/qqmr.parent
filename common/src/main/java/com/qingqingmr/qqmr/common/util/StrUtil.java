package com.qingqingmr.qqmr.common.util;

import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关工具类
 */
public final class StrUtil {

    private StrUtil() {
    }

    /**
     * 只允许输入汉字（^[\u4E00-\u9FA5]+$）,默认长度2~16字符
     *
     * @return
     */
    public static boolean isValidAreaname(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        String reg = "^[\u4E00-\u9FA5]{2,16}$";
        return name.matches(reg);
    }

    /**
     * 用户名是否符合规范（^[\u4E00-\u9FA5A-Za-z0-9_]+$）
     *
     * @param username
     * @param min
     * @param max
     * @return
     */
    public static boolean isValidUsername(String username, Integer min, Integer max) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        String reg = "^[\u4E00-\u9FA5A-Za-z0-9_]{" + min + "," + max + "}$";
        return username.matches(reg);
    }

    /**
     * 密码是否符合规范（[a-zA-Z0-9]{6,15}）
     *
     * @return
     */
    public static boolean isValidPassword(String password) {
        return isValidPassword(password, 6, 15);
    }

    /**
     * 密码是否符合规范（[a-zA-Z0-9]{min,max}）
     *
     * @return
     */
    public static boolean isValidPassword(String password, Integer min, Integer max) {
        if (null == password) {
            return false;
        }
        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+{" + min + "," + max + "}$";
        return password.matches(reg);
    }


    /**
     * 是否有效手机号码
     *
     * @param mobileNum
     * @return
     */
    public static boolean isMobileNum(String mobileNum) {
        if (null == mobileNum) {
            return false;
        }
        return mobileNum.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))(\\d{8})$");
    }

    /**
     * 区号+座机号码+分机号码
     *
     * @param fixedPhone
     * @return
     */
    public static boolean isFixedPhone(String fixedPhone) {
        String reg = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        return Pattern.matches(reg, fixedPhone);
    }

    /**
     * 是否有效邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email) {
            return false;
        }
        return email.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
    }

    /**
     * 是否是QQ邮箱
     */
    public static boolean isQQEmail(String email) {
        if (null == email)
            return false;
        return email.matches("^[\\s\\S]*@qq.com$");
    }

    /**
     * 是否为16-22位银行账号
     *
     * @param bankAccount
     * @return
     */
    public static boolean isBankAccount(String bankAccount) {
        if (null == bankAccount) {
            return false;
        }
        return bankAccount.matches("^\\d{16,22}$");
    }

    /**
     * 是否是纯数字，不含空格
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否数值类型，整数或小数
     *
     * @return
     */
    public static boolean isNumericalValue(String str) {
        if (null == str) {
            return false;
        }
        return str.matches("^[+-]?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d)+)?$");
    }

    /**
     * 是否整数(^[+-]?(([1-9]{1}\\d*)|([0]{1}))$)
     *
     * @param str
     * @return
     */
    public static boolean isNumericInt(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("(^[+-]?([0-9]|([1-9][0-9]*))$)");
    }

    /**
     * 是否正整数
     *
     * @param number
     * @return
     */
    public static boolean isNumericPositiveInt(String number) {
        if (null == number) {
            return false;
        }
        return number.matches("^[+-]?(([1-9]{1}\\d*)|([0]{1}))$");
    }

    /**
     * 判断是否是正整数数或者一位小数
     *
     * @param str
     * @return
     */
    public static boolean isOneDouble(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("^(\\d+\\.\\d{1,1}|\\d+)$");
    }

    /**
     * 判断给定字符串是否小于给定的值(min)
     *
     * @param str
     * @param min
     * @return
     */
    public static boolean isNumLess(String str, float min) {
        if (str == null) {
            return false;
        }
        if (!isNumericalValue(str)) {
            return false;
        }
        float val = Float.parseFloat(str);
        return (val < min);
    }

    /**
     * 判断给定的字符串大于说给定的值
     *
     * @param str
     * @param max
     * @return
     */
    public static boolean isNumMore(String str, float max) {
        if (str == null) {
            return false;
        }
        if (!isNumericalValue(str)) {
            return false;
        }
        float val = Float.parseFloat(str);
        return (val > max);
    }

    /**
     * 是否小数
     *
     * @param str
     * @return
     */
    public static boolean isNumericDouble(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("^[+-]?(([1-9]\\d*\\.?\\d+)|(0{1}\\.\\d+)|0{1})");
    }

    /**
     * 是否是16进制颜色值
     *
     * @param str
     * @return
     */
    public static boolean isColor(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("(^([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$)");
    }

    /**
     * 判断是否是Boolean值
     *
     * @param str
     * @return
     * @author huangyunsong
     * @createDate 2015年12月8日
     */
    public static boolean isBoolean(String str) {
        if (str == null) {
            return false;
        }
        return str.equals("true") || str.equals("false");
    }

    /**
     * 判断是否是日期,格式：yyyy-MM-dd
     *
     * @param str
     * @return
     */
    public static boolean isDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 字符串加星号
     * </p>
     *
     * @param str
     * @param start 0到start，显示
     * @param end   length(str)-end到length(str)，显示
     * @param count 加星号数量
     * @return
     */
    public static String asterisk(String str, int start, int end, int count) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNotBlank(str)) {
            int length = str.length();
            if (start <= length) {
                result.append(str.substring(0, start));
            } else {
                result.append(str.substring(0, length));
            }
            for (int i = 0; i < count; i++) {
                result.append("*");
            }
            if (end <= length) {
                result.append(str.substring(length - end, length));
            } else {
                result.append(str.substring(0, length));
            }
        }
        return result.toString();
    }

    /**
     * 判断是否是几位小数
     *
     * @param num
     * @param digit
     * @return
     */
    public static boolean isMaxDigitDoubleNum(String num, int... digit) {
        if (null == num) {
            return false;
        }
        int bit = (digit == null || digit.length < 1) ? 2 : (digit[0] < 1 ? 2 : digit[0]);
        String reg = "^([1-9][0-9]*(\\.[0-9]{1," + bit + "})?)|(0\\.[0-9]{1," + bit + "})$";
        return num.matches(reg);
    }

    /**
     * 从字符串文本中获得数字
     *
     * @param text
     * @return
     */
    public static String getDigit(String text) {
        String digit = "";
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(text);
        while (m.find()) {
            String find = m.group(1).toString();
            digit += find;
        }
        return digit;
    }

    /**
     * 是否有效的身份证号码
     *
     * @param idNumber
     * @return
     */
    public static boolean isIdNumber(String idNumber) {
        if (null == idNumber) {
            return false;
        }
        return idNumber.matches(
                "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    }

    /**
     * 判断字符串是否是十六进制
     *
     * @param hex
     * @return
     */
    public static boolean isHexStr(String hex) {
        if (hex == null) {
            return false;
        }
        return hex.matches("^[0-9a-fA-F]+$");
    }

    /**
     * 是否包含表情符
     *
     * @param str
     * @return true表示存在，false表示不存在
     */
    public static boolean isEmoji(String str) {
        if (StringUtils.isBlank(str)) return false;
        String other = EmojiParser.removeAllEmojis(str);
        return str.length() != other.length();
    }
}
