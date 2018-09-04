package com.qingqingmr.qqmr.base.sms;

/**
 * 短信场景
 *
 * @author crn
 * @datetime 2018-07-05 10:52:28
 */
public enum SmsSceneEnum {
	
	NOTICE(1, "修改邀请人通知指定的员工"),

	BIND(2, "绑定小程序"),

	FORGET_PWD(3, "忘记交易密码"),

	UPDATE_MOB(4, "更改手机号");

	/** 编码 */
	public int code;
	/** 描述 */
	public String scene;

	private SmsSceneEnum(int code, String scene) {
		this.code = code;
		this.scene = scene;
	}

	public static SmsSceneEnum getEnum(int code) {
		SmsSceneEnum[] clients = SmsSceneEnum.values();
		for (SmsSceneEnum cli : clients) {
			if (cli.code == code) {
				return cli;
			}
		}
		return null;
	}
}
