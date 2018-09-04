package com.qingqingmr.qqmr.service;

import java.util.Map;

import com.qingqingmr.qqmr.common.ResultInfo;

/**
 *
 * @author liujinjin
 * @datetime 2018年7月5日 下午5:23:16
 */
public interface WxChatService {
	
   /**
    * 微信小程序：添加微信用户的用户资金表记录
    * @author liujinjin
    * @param parameters
    * @return
    */
	ResultInfo saveBindWXChat(Map<String, String> parameters);
	
	/**
	 * 微信小程序：昵称修改
	  * @param openId
	 * @param nickName
	 * @return 
	 * @author liujinjin
	 */
	int updateUserNickName(String openId, String nickName);
	
	/**
	 * 微信小程序：性别修改
	 * @author liujinjin
     * @param openId
	 * @return 
	 * @author liujinjin
	 */
	int updateUserSex(String openId, String nickName);
}
