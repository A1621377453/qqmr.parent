package com.qingqingmr.qqmr.common.enums;

/**
 * <p>
 * 消息类型
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月17日 下午5:36:00
 */
public enum MessageTypeEnum {
	SMS(1, "短信"), STATION(2, "站内信"), EMAIL(3, "邮件");

	private int code;
	private String value;

	private MessageTypeEnum(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
