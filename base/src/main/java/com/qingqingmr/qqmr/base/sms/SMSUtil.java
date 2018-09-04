package com.qingqingmr.qqmr.base.sms;

import com.qingqingmr.qqmr.base.sms.chuanglan.HttpSender;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.constant.SystemRedisKeyConstant;
import com.qingqingmr.qqmr.common.enums.ProfileEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信工具类
 *
 * @author crn
 * @datetime 2018-07-05 10:52:28
 */
public class SMSUtil {
	public static final Logger LOG = LoggerFactory.getLogger(HttpSender.class);

	/**
	 * 发送短信
	 *
	 * @author crn
	 * @datetime 2018-07-05 10:52:28
	 * @param smsProvider
	 *            短信提供商
	 * @param smsAccount
	 *            账号
	 * @param smsPassword
	 *            密码
	 * @param mobile
	 *            手机号
	 * @param smsContent
	 *            内容
	 */
	public static void sendSMS(String smsProvider, String smsAccount, String smsPassword, String mobile,
			String smsContent) {
		String data = null;

		if ("创蓝".equals(smsProvider)) {
			data = HttpSender.sendSms(HttpSender.SMS_API_URL, smsAccount, smsPassword, smsContent, mobile);
		} else {
			LOG.info("暂无其他短信接口");
		}

		LOG.info("发送短信：{}", data);
	}

	/**
	 * 短信验证码
	 *
	 * @author crn
	 * @datetime 2018-07-05 10:52:28
	 * @param smsProvider
	 *            短信提供商
	 * @param smsAccount
	 *            账号
	 * @param smsPassword
	 *            密码
	 * @param mobile
	 *            手机号
	 * @param content
	 *            内容
	 * @param effectiveTime
	 *            有效时长(min)
	 * @param scene
	 *            短信发送场景
	 * @param smsType
	 *            短信类型（1普通短信:2语音短信）
	 * @param redisTemplate
	 *            当前请求
	 * @param profile
	 *            当前环境
	 */
	public static void sendCode(String smsProvider, String smsAccount, String smsPassword, String mobile,
			String content, int effectiveTime, String scene, int smsType, RedisTemplate<String, Object> redisTemplate,
			ProfileEnum profile) {
		// 缓存时间
		effectiveTime = effectiveTime < 1 ? SystemConstant.CACHE_TIME_MINUTE_30 : effectiveTime;
		// 是否发送验证码
		boolean isSend = !ProfileEnum.PROD.getValue().equals(profile.getValue()) ? false : true;
		// 随机产生验证码，默认是6个8
		int randomCode = isSend ? (new Random()).nextInt(899999) + 100000 : 888888;

		// 缓存验证码的key
		String key = profile.getValue() + SystemRedisKeyConstant.MOBILE + mobile + "." + scene;
		Object cache = redisTemplate.opsForValue().get(key);
		String smsContent = null;
		// 判断缓存是否有对应的key
		if (cache != null && !"".equals(cache.toString())) {
			smsContent = content.replace(SystemConstant.SMS_CODE, cache.toString());
		} else {
			smsContent = content.replace(SystemConstant.SMS_CODE, String.valueOf(randomCode));
			redisTemplate.opsForValue().set(key, String.valueOf(randomCode), effectiveTime, TimeUnit.MINUTES);
		}

		// 接入短信接口
		if (isSend) {
			sendSMS(smsProvider, smsAccount, smsPassword, mobile, smsContent);
		}
		LOG.info("发送短信内容：{}", smsContent);
	}
}