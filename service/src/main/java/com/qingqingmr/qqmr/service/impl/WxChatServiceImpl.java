package com.qingqingmr.qqmr.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.dao.WxChatMapper;
import com.qingqingmr.qqmr.domain.entity.WxChat;
import com.qingqingmr.qqmr.service.WxChatService;

/**
 * @author liujinjin
 * @datetime 2018年7月5日 下午5:23:24
 */
@Service
public class WxChatServiceImpl implements WxChatService {

    private static final Logger LOG = LoggerFactory.getLogger(WxChatServiceImpl.class);
    @Autowired
    private WxChatMapper wxChatMapper;

    /**
     * 添加微信用户的微信绑定表信息
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public ResultInfo saveBindWXChat(Map<String, String> parameters) {
        WxChat wxChat = new WxChat();
        ResultInfo resultInfo = new ResultInfo();
        try {
        	String openId = parameters.get("openId");
        	if(StringUtils.isBlank(openId)) {
        		resultInfo.setInfo(-1, "传输参数为空！");
        		return resultInfo;
        	}
            if (wxChatMapper.getWxChatByOpenId(openId) == null) {
                wxChat.setTime(new Date());//添加时间
                wxChat.setOpenId(parameters.get("openId"));//openid
                wxChat.setNickName(parameters.get("nickName"));//昵称
                wxChat.setImageUrl(parameters.get("photo"));//头像
                wxChat.setSex(Integer.valueOf(parameters.get("sex")));//性别
                wxChat.setWxInfo(parameters.get("wxInfo"));//微信授权信息

                wxChatMapper.saveWxChat(wxChat);
            }
        } catch (Exception e) {
        	LOG.error("新增微信绑定表信息时，出现异常--{}", e.getMessage());
            resultInfo.setInfo(-1, "绑定失败");
            throw new ServiceRuntimeException("新增微信绑定表信息时，出现异常", true);
        }
        
        if (wxChat.getId() < 0) {
        	resultInfo.setInfo(-1, "绑定失败");
            return resultInfo;
        }
        resultInfo.setInfo(1, "绑定成功");
        return resultInfo;
    }

    /**
     * 微信小程序：昵称修改
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int updateUserNickName(String openId, String nickName) {
        int num = wxChatMapper.updateUserNickName(openId,nickName);
        if (num < 1) {
            LOG.error("通过微信openId修改昵称失败");
            throw new ServiceRuntimeException("通过微信openId修改昵称失败", true);
        }
        return num;
    }

    /**
     * 微信小程序：性别修改
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int updateUserSex(String openId, String nickName) {
        int num = wxChatMapper.updateUserSex(openId, nickName);
        if (num < 1) {
            LOG.error("通过微信openId修改性别失败");
            throw new ServiceRuntimeException("通过微信openId修改性别失败", true);
        }
        return num;
    }
}
