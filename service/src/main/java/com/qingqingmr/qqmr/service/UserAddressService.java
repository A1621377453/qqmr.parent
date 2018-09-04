package com.qingqingmr.qqmr.service;

import java.util.List;
import java.util.Map;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.bean.UserAddressDetailVO;

public interface UserAddressService {
	
	/**
     * 微信小程序：保存用户收货地址
     * @author liujinjin
     * @param parameters
     * @return
     */
    ResultInfo saveUserAddress(Map<String,String> parameters);
    
    /**
     * 微信小程序：更新收货地址
     * @author liujinjin
     * @param parameters
     * @return
     */
    int updateUserAddress(Map<String,String> parameters);
	
    /**
     * 微信小程序：查询个人收货地址列表
     * @author liujinjin
     * @param userId
     * @return
     */
    List<UserAddressDetailVO> listUserAddressByUserId(String userId);
    
    /**
     * 微信小程序：删除收货地址
     * @author liujinjin
     * @param id
     * @return
     */
    ResultInfo deleteUserAddress(Long id);
}
