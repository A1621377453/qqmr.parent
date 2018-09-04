package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.dao.*;
import com.qingqingmr.qqmr.domain.bean.VipPrivilegeDetailVo;
import com.qingqingmr.qqmr.domain.entity.Goods;
import com.qingqingmr.qqmr.domain.entity.MembershipCard;
import com.qingqingmr.qqmr.domain.entity.Store;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private MembershipCardMapper membershipCardMapper;
	@Autowired
	private StoreMapper storeMapper;

	/**
	 * 微信小程序：获取VIP特权详情页信息
	 *
	 * @param
	 * @return
	 * @author liujinjin
	 */
	@Override
	public VipPrivilegeDetailVo getVipPrivilegeDetail(Map<String, String> parameters) {
		VipPrivilegeDetailVo vipPrivilegeDetailVo = new VipPrivilegeDetailVo();

		Goods goods = goodsMapper.getGoodsInfo();
		Integer number = orderMapper.getOrderPaySuccessCount();
		User user = userMapper.getUserById(Long.valueOf(parameters.get("userId")));
		vipPrivilegeDetailVo.setGoodsId(goods.getId().toString());
		int roleType = user.getRoleType();
		if (roleType == 2) {
			MembershipCard membershipCard = membershipCardMapper
					.getMembershipCardNoInfoByUserId(Long.valueOf(parameters.get("userId")));
			String cardNo = membershipCard.getCardNo();
			vipPrivilegeDetailVo.setCardNo(cardNo);
			vipPrivilegeDetailVo.setName(goods.getName());
			if (goods.getType() == 1) {
				vipPrivilegeDetailVo.setType("会员卡");
			} else if (goods.getType() == 2) {
				vipPrivilegeDetailVo.setType("其他");
			}
			vipPrivilegeDetailVo.setImage(goods.getImage());
			vipPrivilegeDetailVo.setPrice(goods.getPrice());
			vipPrivilegeDetailVo.setContent(goods.getContent());
			vipPrivilegeDetailVo.setService(goods.getService());
			vipPrivilegeDetailVo.setServiceCount(goods.getServiceCount().toString());
			if (goods.getServiceForm() == 1) {
				vipPrivilegeDetailVo.setServiceForm("到店");
			}
			vipPrivilegeDetailVo.setPurchaseNum(number.toString());// 购买人数

			boolean isActive = membershipCard.getIsActive();
			if (isActive == true) {
				Date expireTime = membershipCard.getExpireTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(expireTime);
				vipPrivilegeDetailVo.setValidityPeriod(dateString);// 有效时间
			} else {
				vipPrivilegeDetailVo.setValidityPeriod("未激活");
			}
		} else {
			Store store = storeMapper.getStoreNameByUserId(Long.valueOf(parameters.get("userId")));
			if (store != null) {
				Long storeId = store.getId();
				Long areaId = store.getAreaId();
				DecimalFormat df = new DecimalFormat("0000");
				String cardCode = df.format(storeId).toString() + df.format(areaId).toString() + "****";
				vipPrivilegeDetailVo.setCardNo(cardCode);
				vipPrivilegeDetailVo.setName(goods.getName());
				if (goods.getType() == 1) {
					vipPrivilegeDetailVo.setType("会员卡");
				} else if (goods.getType() == 2) {
					vipPrivilegeDetailVo.setType("其他");
				}
				vipPrivilegeDetailVo.setImage(goods.getImage());
				vipPrivilegeDetailVo.setPrice((goods.getPrice()));
				vipPrivilegeDetailVo.setContent(goods.getContent());
				vipPrivilegeDetailVo.setService(goods.getService());
				vipPrivilegeDetailVo.setServiceCount(goods.getServiceCount().toString());
				if (goods.getServiceForm() == 1) {
					vipPrivilegeDetailVo.setServiceForm("到店");
				}
				vipPrivilegeDetailVo.setPurchaseNum(number.toString());// 购买人数
				vipPrivilegeDetailVo.setValidityPeriod("");// 有效时间(非会员的话不显示有效时间)
			}
		}
		return vipPrivilegeDetailVo;
	}

}
