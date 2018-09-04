package com.qingqingmr.qqmr.base.sms.chuanglan;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 创蓝接口
 *
 * @author crn
 * @datetime 2018-07-05 10:52:28
 */
public class HttpSender {
	/** 日志 */
	public static final Logger LOG = LoggerFactory.getLogger(HttpSender.class);
	/** 接口 */
	public static final String SMS_API_URL = "http://smssh1.253.com/msg/send/json";

	/**
	 * 发送短信
	 *
	 * @param requestServerUrl
	 * @param account
	 * @param pswd
	 * @param msg
	 * @param phone
	 * @return
	 */
	public static String sendSms(String requestServerUrl, String account, String pswd, String msg, String phone) {
		String report = "true";
		JSONObject requestJson = new JSONObject();
		requestJson.put("account", account);
		requestJson.put("password", pswd);
		requestJson.put("msg", msg);
		requestJson.put("phone", phone);
		requestJson.put("report", report);

		String response = sendSmsByPost(requestServerUrl, requestJson.toString());
		LOG.info("短信发送同步信息：{}", response);
		return response;
	}

	/**
	 * 创蓝接口请求
	 *
	 * @param path
	 * @param postContent
	 * @return
	 */
	private static String sendSmsByPost(String path, String postContent) {
		OutputStream os = null;
		try {
			URL url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);// 连接超时 单位毫秒
			httpURLConnection.setReadTimeout(2000);// 读取超时 单位毫秒
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			httpURLConnection.connect();
			os = httpURLConnection.getOutputStream();
			os.write(postContent.getBytes("UTF-8"));
			os.flush();

			StringBuilder sb = new StringBuilder();
			int httpRspCode = httpURLConnection.getResponseCode();
			if (httpRspCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("创蓝接口请求异常：{}", e.getMessage());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}