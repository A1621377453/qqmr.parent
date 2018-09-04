package com.qingqingmr.qqmr.dao;


import com.qingqingmr.qqmr.domain.bean.UserAddressDetailVO;
import com.qingqingmr.qqmr.domain.bean.UserAddressVO;
import com.qingqingmr.qqmr.domain.entity.UserAddress;

import java.util.List;

/**
 * 用户收货地址表
 *
 * @author liujinjin
 * @datetime 2018-7-4 11:13:33
 */
public interface UserAddressMapper {
	
	/**
     * 通过id删除
     * @author liujinjin
     * @param id
     * @return
     */
    int deleteUserAddressById(Long id);
    
    /**
     * 保存用户收货地址
     * @author liujinjin
     * @param userAddress
     * @return
     */
    int saveUserAddress(UserAddress userAddress);
   
    /**
     * 通过id查询用户收货地址
     * @author liujinjin
     * @param id
     * @return
     */
    UserAddress getUserAddressById(Long id);
    
    /**
     * 更新收货地址
     * @author liujinjin
     * @param userAddress
     * @return
     */
    int updateUserAddress(UserAddress userAddress);
    
    /**
     * 更新收货地址表是否默认收货地址状态
     * @author liujinjin
     * @param userAddress
     * @return
     */
    int updateUserAddressIsDefault(Long userId);

    /**
     * 根据userId查询用户收货地址
     * @param userId
     * @return
     */
    List<UserAddressVO> listUserAddress(Long userId);
    
    /**
     * 微信小程序：根据userId查询用户收货地址
     * @param userId
     * @return
     */
    List<UserAddressDetailVO> listUserAddressByuserId(Long userId);
    
    /**
     * 通过id逻辑删除用户收货地址
     * @author liujinjin
     * @param id
     * @return
     */
    int deleteUserAddress(Long id);
}