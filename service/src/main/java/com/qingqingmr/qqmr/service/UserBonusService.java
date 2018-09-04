package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.base.bo.UserBonusSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.bean.UserBonusVO;
import com.qingqingmr.qqmr.domain.entity.UserBonus;
import com.qingqingmr.qqmr.domain.entity.UserBonusDetail;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户奖金
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月6日 下午5:59:36
 */
public interface UserBonusService {

    /**
     * <p>
     * 分页查询用户佣金明细
     * </p>
     *
     * @param currPage
     * @param pageSize
     * @param userId
     * @return
     * @author liujingjing
     * @datetime 2018年7月6日 下午6:03:38
     */
    PageResult<UserBonus> listUserBonusPage(int currPage, int pageSize, long userId);

    /**
     * <p>
     * 查询我的邀请信息
     * </p>
     *
     * @param userId
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 下午4:02:17
     */
    ResultInfo myInvite(long userId);

    /**
     * 根据条件查询获取佣金列表
     *
     * @param bonusSearchBO
     * @param request
     * @return
     */
    PageResult<UserBonusVO> listBonus(UserBonusSearchBO bonusSearchBO, HttpServletRequest request);

    /**
     * 保存佣金明细
     *
     * @param userBonusDetail
     * @return
     */
    ResultInfo saveBonusDetail(UserBonusDetail userBonusDetail);

    /**
     * 保存佣金
     *
     * @param userBonus
     * @return
     */
    ResultInfo saveBonus(UserBonus userBonus);

    /**
     * 发放佣金
     *
     * @param userId
     * @param type 购买会员卡1，更改邀请关系2
     * @return
     */
    ResultInfo rebate(long userId, int type);

    /**
     * 计算人头奖
     *
     * @return
     */
    ResultInfo calcNumberAward();

    /**
     * 返还到期奖金
     *
     * @return
     */
    ResultInfo returnExpireAward();

    /**
     * 更新所有到期佣金标识和时间
     *
     * @param period
     * @return
     */
    int updateReturnExpire(int period);
}
