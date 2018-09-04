package com.qingqingmr.qqmr.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统设置
 * 
 * @author ztl
 * @datetime 2018-7-9 09:54:05
 */
public interface SettingPlatformMapper {
	/**
	 * <p>
	 * 平台设置--根据key更新value值
	 * </p>
	 *
	 * @author ztl
	 * @param key
	 * @param value
	 * @return
	 */
	int updateSettingByKey(@Param("_key") String key, @Param("_value") String value);

	/**
	 * <p>
	 * 平台设置-通过key查找value
	 * </p>
	 *
	 * @author ztl
	 * @param key
	 * @return
	 */
	String getSettingValueByKey(String key);

	/**
	 * <p>
	 * 查询平台所有的设置信息
	 * </p>
	 *
	 * @author ztl
	 * @return
	 */
	List<Map<String, String>> listAllSetting();
}