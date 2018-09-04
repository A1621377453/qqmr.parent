package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.domain.entity.MembershipCard;

/**
 * @author liujinjin
 * @datetime 2018年7月5日 下午6:19:30
 */
public interface MembershipCardService {

    /**
     * 微信小程序：根据用户id查询会员卡信息
     *
     * @param userId
     * @return
     */
    MembershipCard getMembershipCard(Long userId);

    /**
     * 微信小程序：获取将要产生的会员卡卡号
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    String getMembershipCardCode(Long userId);

    /**
     * 微信小程序：微信绑定的用户，下单购买会员卡，添加会员卡记录
     *
     * @param userId
     * @param cardNo
     * @param isPayFor
     * @param orderId
     * @param remark
     * @return
     * @author liujinjin
     */
    int saveMembershipCard(long userId, String cardNo, boolean isPayFor, long orderId, String remark);
}
