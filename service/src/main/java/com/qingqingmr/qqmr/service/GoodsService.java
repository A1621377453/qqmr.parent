package com.qingqingmr.qqmr.service;

import java.util.Map;

import com.qingqingmr.qqmr.domain.bean.VipPrivilegeDetailVo;

/**
 * 自定义：VIP特权详情实体
 * @author liujinjin
 * @datetime 2018年7月10日 下午8:11:14
 */
public interface GoodsService {
	
	/**
	 * 微信小程序：获取VIP特权详情页信息
	 * @author liujinjin
	 * @param parameters
	 * @return
	 */
	VipPrivilegeDetailVo getVipPrivilegeDetail(Map<String,String> parameters);
	
}
