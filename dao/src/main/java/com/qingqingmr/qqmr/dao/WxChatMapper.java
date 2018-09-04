package com.qingqingmr.qqmr.dao;


import org.apache.ibatis.annotations.Param;

import com.qingqingmr.qqmr.domain.entity.WxChat;

/**
 * 微信绑定表
 *
 * @author liujinjin
 * @datetime 2018-7-4 11:13:33
 */
public interface WxChatMapper {
    
    /**
     * 通过id删除
     * @author liujinjin
     * @param id
     * @return
     */
    int deleteWxChatById(Long id);
    /**
     * 保存微信绑定信息
     * @author liujinjin
     * @param wxChat
     * @return
     */
    int saveWxChat(WxChat wxChat);
   
    /**
     * 通过id查询微信绑定信息
     * @author liujinjin
     * @param id
     * @return
     */
    WxChat getWxChatById(Long id);
    
    /**
     * 更新微信绑定信息
     * @author liujinjin
     * @param wxChat
     * @return
     */
    int updateWxChat(WxChat wxChat);
    
	/**
	 * 微信小程序：昵称修改
	 * 
	 * @param openId
	 * @param nickName
	 * @return
	 * @author liujinjin
	 */
	int updateUserNickName(@Param("openId") String openId, @Param("nickName") String nickName);

	/**
	 * 微信小程序：性别修改
	 * 
	 * @param openId
	 * @param sex
	 * @return
	 * @author liujinjin
	 */
	int updateUserSex(@Param("openId") String openId, @Param("sex") String sex);
	
	 /**
     * 通过微信openId查询微信绑定信息
     * @author liujinjin
     * @param openId
     * @return
     */
    WxChat getWxChatByOpenId(String openId);
}