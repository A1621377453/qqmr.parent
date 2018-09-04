package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author liujinjin
 * @datetime 2018年7月4日 下午7:54:22
 */
public interface UserInfoService {
	
    /**
     * 微信小程序：添加微信用户的用户信息表记录
     * @author liujinjin
     * @param parameters
     * @return
     */
	int saveBindWXUserInfo(Map<String, String> parameters);
	
	/**
	 * 更新用户邀请人信息
	 * @param inviteeUserId
	 * @param inviterUserId
	 * @param type
	 * @param request
     * @return
	 */
    ResultInfo updateInviter(Long inviteeUserId, Long inviterUserId, Integer type, HttpServletRequest request);

	/**
	 * 根据userId获取用户直接邀请人信息
	 * @param userId
	 * @return
	 */
	ResultInfo getInviterInfo(Long userId);

	/**
	 * 根据userId获取用户信息
	 *
	 * @param userId
	 * @return
	 */
	UserInfo getUserInfoByUserId(Long userId);

	/**
	 * 发送短信
	 *
	 * @param mobile
	 * @param scene
	 * @param smsType 1短信  2语音
	 * @return com.qingqingmr.qqmr.common.ResultInfo
	 * @datetime 2018/7/17 11:55
	 * @author crn
	 */
	ResultInfo sendCode(String mobile, String scene, int smsType, String content);

	/**
	 * 修改被邀请人信息
	 *
	 * @param inviteeUserId
	 * @param inviterUserId
     * @param type
	 * @return
	 */
	ResultInfo updateInviterInfo(Long inviteeUserId, Long inviterUserId,int type);
}
