package com.qingqingmr.qqmr.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.dao.UserAddressMapper;
import com.qingqingmr.qqmr.domain.bean.UserAddressDetailVO;
import com.qingqingmr.qqmr.domain.entity.UserAddress;
import com.qingqingmr.qqmr.service.UserAddressService;

@Service
public class UserAddressServiceImpl implements UserAddressService {

	private static final Logger LOG = LoggerFactory.getLogger(UserAddressServiceImpl.class);
	@Autowired
	private UserAddressMapper userAddressMapper;

	/**
	 * 微信小程序：保存用户收货地址
	 *
	 * @param parameters
	 * @return
	 * @author liujinjin
	 */
	@Transactional
	@Override
	public ResultInfo saveUserAddress(Map<String, String> parameters) {
		ResultInfo resultInfo = new ResultInfo();
		UserAddress userAddress = new UserAddress();
		try {
			long userId = Long.parseLong(parameters.get("userId"));
			List<UserAddressDetailVO> list = userAddressMapper.listUserAddressByuserId(userId);
			if(list.size()>=3) {
				resultInfo.setInfo(-1, "最多可新建三个收货地址");
				return resultInfo;
			}
			userAddress.setTime(new Date());// 时间
			userAddress.setUserId(userId);// 用户id
			userAddress.setName(parameters.get("name"));// 姓名
			userAddress.setMobile(parameters.get("mobile"));// 手机号
			userAddress.setProvinceId(Long.valueOf(parameters.get("provinceId")));// 省份id
			userAddress.setCityId(Long.valueOf(parameters.get("cityId")));// 地市id
			userAddress.setAreaId(Long.valueOf(parameters.get("areaId")));// 区域id
			userAddress.setDetail(parameters.get("detail"));// 详细地址
			String isDefault = parameters.get("isDefault");// 是否默认地址
			if (isDefault.equals("1")) {
				userAddress.setIsDefault(true);
				// 该条收货地址为默认地址，其他地址要修改为非默认地址
				userAddressMapper.updateUserAddressIsDefault(Long.valueOf(parameters.get("userId")));
			} else {
				userAddress.setIsDefault(false);
			}
			userAddress.setIsDel(false);// 是否删除
			userAddressMapper.saveUserAddress(userAddress);
		} catch (Exception e) {
			LOG.error("添加用户收货地址时，出现异常--{}", e.getMessage());
			throw new ServiceRuntimeException("添加用户收货地址时，出现异常", true);
		}

		if (userAddress.getId() < 0) {
			resultInfo.setInfo(-1, "添加收货地址失败");
			return resultInfo;
		}
		resultInfo.setInfo(1, "添加收货地址成功");
		return resultInfo;
	}

	/**
	 * 微信小程序：更新收货地址
	 *
	 * @param parameters
	 * @return
	 * @author liujinjin
	 */
	@Transactional
	@Override
	public int updateUserAddress(Map<String, String> parameters) {
		int num = 0;
		try {
			Long id = Long.parseLong(parameters.get("id"));
			UserAddress userAddress = new UserAddress();
			userAddress.setId(id);// id
			userAddress.setTime(new Date());// 时间
			userAddress.setUserId(Long.valueOf(parameters.get("userId")));// 用户id
			userAddress.setName(parameters.get("name"));// 姓名
			userAddress.setMobile(parameters.get("mobile"));// 手机号
			userAddress.setProvinceId(Long.valueOf(parameters.get("provinceId")));// 省份id
			userAddress.setCityId(Long.valueOf(parameters.get("cityId")));// 地市id
			userAddress.setAreaId(Long.valueOf(parameters.get("areaId")));// 区域id
			userAddress.setDetail(parameters.get("detail"));// 详细地址
			String isDefault = parameters.get("isDefault");// 是否默认地址
			if (isDefault.equals("1")) {
				userAddress.setIsDefault(true);
				userAddressMapper.updateUserAddressIsDefault(Long.valueOf(parameters.get("userId")));
			} else {
				userAddress.setIsDefault(false);
			}
			num = userAddressMapper.updateUserAddress(userAddress);
			if (num < 1) {
				LOG.error("修改用户收货地址失败");
				throw new ServiceRuntimeException("修改用户收货地址失败", true);
			}
		} catch (Exception e) {
			LOG.error("修改用户收货地址时，出现异常--{}", e.getMessage());
			throw new ServiceRuntimeException("修改用户收货地址时，出现异常", true);
		}

		return num;
	}

	/**
	 * 微信小程序：查询个人收货地址列表
	 *
	 * @param parameters
	 * @return
	 * @author liujinjin
	 */
	@Override
	public List<UserAddressDetailVO> listUserAddressByUserId(String userId) {
		return userAddressMapper.listUserAddressByuserId(Long.valueOf(userId));
	}
	
	/**
	 * 微信小程序：删除收货地址
	 *
	 * @param id
	 * @return
	 * @author liujinjin
	 */
	@Transactional
	@Override
	public ResultInfo deleteUserAddress(Long id) {
		ResultInfo resultInfo = new ResultInfo();
		int num = 0;
		try {
			UserAddress userAddress = userAddressMapper.getUserAddressById(id);
			if(userAddress == null) {
				resultInfo.setInfo(-1, "未查出该条用户收货地址");
				return resultInfo;
			}
			num = userAddressMapper.deleteUserAddress(id);
			if (num < 1) {
				LOG.error("删除用户收货地址失败");
				resultInfo.setInfo(-1, "删除收货地址失败");
				throw new ServiceRuntimeException(resultInfo.getMsg(), true);
			}
		} catch (Exception e) {
			LOG.error("删除用户收货地址时，出现异常--{}", e.getMessage());
			throw new ServiceRuntimeException("出现异常", true);
		}
		resultInfo.setInfo(1, "删除收货地址成功");
		return resultInfo;
	}

}
