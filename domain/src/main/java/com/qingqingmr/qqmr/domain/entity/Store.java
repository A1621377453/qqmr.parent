package com.qingqingmr.qqmr.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 门店实体类
 * 
 * @author liujingjing
 * @datetime 2018年7月3日 下午7:29:18
 */
public class Store {
	private Long id;

	/**
	 * 添加时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date time;

	/**
	 * 门店名称
	 */
	private String name;

	/**
	 * 门店类型：1直营店，2加盟店
	 */
	private Integer type;

	/**
	 * 区域id
	 */
	private Long areaId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	/**
	 * 门店枚举
	 * 
	 * @author liujingjing
	 * @datetime 2018年7月3日 下午7:56:44
	 */
	public enum StoreTypeEnum {
		/**
		 * 直营店
		 */
		STRAIGHT_STORES(1, "直营店"),

		/**
		 * 加盟店
		 */
		NAPA_STORES(2, "加盟店");

		public int code;
		public String value;

		private StoreTypeEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public static StoreTypeEnum getEnum(int code) {
			StoreTypeEnum[] storeTypeEnums = StoreTypeEnum.values();
			for (StoreTypeEnum storeTypeEnum : storeTypeEnums) {
				if (storeTypeEnum.code == code) {

					return storeTypeEnum;
				}
			}

			return null;
		}
	}

}