package com.qingqingmr.qqmr.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * FastJson工具类
 * </p>
 *
 * @author ythu
 * @datetime 2018年3月2日 上午10:52:37
 */
public final class FastJsonUtil {

    private FastJsonUtil() {
    }

    private final static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * <p>
     * obj转换成JSONObject string
     * </p>
     *
     * @param obj
     * @return
     * @author ythu
     * @datetime 2018年3月2日 上午11:26:36
     */
    public static String toJsonString(Object obj) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer(DEFFAULT_DATE_FORMAT));

        return JSON.toJSONString(obj, serializeConfig, SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * <p>
     * JSONObject string转换成object
     * </p>
     *
     * @param text
     * @param clazz
     * @return
     * @author ythu
     * @datetime 2018年3月2日 上午11:27:16
     */
    public static <T> T parseObj(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    /**
     * <p>
     * JSONObject string转换成list object
     * </p>
     *
     * @param text
     * @param clazz
     * @return
     * @author ythu
     * @datetime 2018年3月2日 上午11:27:16
     */
    public static <T> List<T> parseListObj(String text, Class<T> clazz) {

        return JSON.parseArray(text, clazz);
    }
}
