package com.qingqingmr.qqmr.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密解密方法
 */
public class Encrypt {

    /**
     * MD5 摘要，使用系统缺省字符集编码
     *
     * @param input
     * @return
     */
    public static String MD5(String input) {
        return MD5(input, Charset.defaultCharset());
    }

    /**
     * MD5 摘要
     *
     * @param input
     * @param charset
     * @return
     */
    public static String MD5(String input, String charset) {
        return MD5(input, Charset.forName(charset));
    }

    /**
     * MD5 摘要
     *
     * @param input
     * @param charset
     * @return
     */
    public static String MD5(String input, Charset charset) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md.update(input.getBytes(charset));

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] tmp = md.digest();
        char[] str = new char[16 * 2];

        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }

        String result = new String(str);

        return result;
    }

    /**
     * 加密3DES 默认GB2312
     *
     * @param input
     * @param key
     * @return
     */
    public static String encrypt3DES(String input, String key) {
        return encrypt3DES(input, key, Charset.forName("GB2312"));
    }

    /**
     * 加密3DES
     *
     * @param input
     * @param key
     * @param charset
     * @return
     */
    public static String encrypt3DES(String input, String key, Charset charset) {
        try {
            return byte2hex(encryptDESede(input.getBytes(charset.name()), key.getBytes()));
        } catch (Exception localException) {
        }
        return "";
    }

    /**
     * 解密3DES 默认GB2312
     *
     * @param input
     * @param key
     * @return
     */
    public static String decrypt3DES(String input, String key) {
        return decrypt3DES(input, key, Charset.forName("GB2312"));
    }

    /**
     * 解密3DES
     *
     * @param input
     * @param key
     * @param charset 字符编码
     * @return
     */
    public static String decrypt3DES(String input, String key, Charset charset) {
        try {
            return new String(decryptDESede(hex2byte(input.getBytes()), key.getBytes()), charset.name());
        } catch (Exception localException) {
        }
        return "";
    }

    /**
     * 加密DES 默认GB2312
     *
     * @param input
     * @param key
     * @return
     */
    public static String encryptDES(String input, String key) {
        return encryptDES(input, key, Charset.forName("GB2312"));
    }

    /**
     * 加密DES
     *
     * @param input
     * @param key
     * @param charset
     * @return
     */
    public static String encryptDES(String input, String key, Charset charset) {
        try {
            return byte2hex(encrypt(input.getBytes(charset.name()), key.getBytes()));
        } catch (Exception localException) {
        }
        return "";
    }

    /**
     * 解密DES 默认GB2312
     *
     * @param input
     * @param key
     * @return
     */
    public static String decryptDES(String input, String key) {
        return decryptDES(input, key, Charset.forName("GB2312"));
    }

    /**
     * 解密DES
     *
     * @param input
     * @param key
     * @param charset 字符编码
     * @return
     */
    public static String decryptDES(String input, String key, Charset charset) {
        try {
            return new String(decrypt(hex2byte(input.getBytes()), key.getBytes()), charset.name());
        } catch (Exception localException) {
        }
        return "";
    }

    /**
     * 比较两个字节数组
     *
     * @param input1
     * @param input2
     * @return
     */
    public static boolean compare(byte[] input1, byte[] input2) {
        if ((input1 == null) || (input2 == null)) {
            return false;
        }
        if (input1.length != input2.length) {
            return false;
        }
        for (int i = 0; i < input1.length; i++) {
            if (input1[i] != input2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字节转16进制
     *
     * @param buffer
     * @return
     */
    public static String byte2hex(byte[] buffer) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < buffer.length; n++) {
            stmp = Integer.toHexString(buffer[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 16进制转字节
     *
     * @param buffer
     * @return
     */
    public static byte[] hex2byte(byte[] buffer) {
        if (buffer.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[buffer.length / 2];
        for (int n = 0; n < buffer.length; n += 2) {
            String item = new String(buffer, n, 2);
            b2[(n / 2)] = ((byte) Integer.parseInt(item, 16));
        }
        return b2;
    }

    /**
     * DES加密
     *
     * @param src
     * @param key 长度为8位
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(1, securekey, sr);
        return cipher.doFinal(src);
    }

    /**
     * DES解密
     *
     * @param src
     * @param key 长度为8位
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, sr);
        return cipher.doFinal(src);
    }

    /**
     * 3DES加密【无向量】
     *
     * @param src
     * @param key 长度24位
     * @return
     * @throws Exception
     */
    public static byte[] encryptDESede(byte[] src, byte[] key) throws Exception {
        DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey secretKey = keyFactory.generateSecret(deSedeKeySpec);
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());
        return cipher.doFinal(src);
    }

    /**
     * 3DES解密【无向量】
     *
     * @param src
     * @param key 长度24位
     * @return
     * @throws Exception
     */
    public static byte[] decryptDESede(byte[] src, byte[] key) throws Exception {
        DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey secretKey = keyFactory.generateSecret(deSedeKeySpec);
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new SecureRandom());
        return cipher.doFinal(src);
    }
}