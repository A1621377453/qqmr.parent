package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.MembershipCard;

/**
 * 用户会员卡信息
 *
 * @author ztl
 * @datetime 2018-7-4 11:36:16
 */
public interface MembershipCardMapper {
    /**
     * 通过id删除用户会员卡
     *
     * @param id
     * @return
     * @author ztl
     */
    int deleteMembershipCardById(Long id);

    /**
     * 保存会员卡信息
     *
     * @param record
     * @return
     * @author ztl
     */
    int saveMembershipCard(MembershipCard record);

    /**
     * 通过id查询会员卡信息
     *
     * @param id
     * @return
     * @author ztl
     */
    MembershipCard getMembershipCardById(Long id);

    /**
     * 更新会员卡信息
     *
     * @param record
     * @return
     * @author ztl
     */
    int updateMembershipCardById(MembershipCard record);

    /**
     * 微信小程序：门店区域+门店模糊查询，查询会员卡表最大的会员卡卡号
     *
     * @param cardNo
     * @return
     * @author liujinjin
     */
    Long countMembershipCardNoPrefix(String cardNo);

    /**
     * 微信小程序：根据用户id查询会员卡信息
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    MembershipCard getMembershipCardNoInfoByUserId(Long userId);
    
    /**
     * 微信小程序：根据id修改会员卡激活状态
     *
     * @param id
     * @return
     * @author liujinjin
     */
    int updateMembershipCardIsActiveById(Long id);
    
	/**
	 * 微信小程序：第一次添加服务时修改会员卡状态和到期时间
	 * 
	 * @author liujingjing
	 * @param id
	 * @return
	 */
	int updateMembershipCard(Long id);
}