package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.UserFunds;

/**
 * @author liujinjin
 * @datetime 2018年7月4日 下午8:07:59
 */
public interface UserFundsService {

    /**
     * 更新用户资金信息
     *
     * @param userFunds
     * @return
     * @author ztl
     */
    int updateUserFunds(UserFunds userFunds);

    /**
     * 根据用户id查询用户资金详情
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    UserFunds getUserFundsByUserId(Long userId);

    /**
     * <p>
     * 查询总资产信息
     * </p>
     *
     * @param userId
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 下午8:15:29
     */
    ResultInfo getTotalAssets(long userId);

    /**
     * 校验用户资金是否正常
     *
     * @param userId
     * @return
     * @author ztl
     */
    ResultInfo checkUserFund(Long userId);

    /**
     * 微信小程序：获取提现时的金额信息（账户余额、可提现金额）
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    ResultInfo getWithdrawalAmountInfo(Long userId);

    /**
     * 增加账户余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateAddBalance(long userId, double balance);

    /**
     * 减少账户余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateSubBalance(long userId, double balance);

    /**
     * 增加账户可提现余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateAddAvailableBalance(long userId, double balance);

    /**
     * 增加账户余额和账户可提现余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateAddBalanceAndAvailableBalance(long userId, double balance);

    /**
     * 增加冻结金额，减少账户余额和账户可提现余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateAddFreezeAndSubBalanceAndAvailableBalance(long userId, double amount);

    /**
     * 减少冻结金额，增加账户余额和账户可提现余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateSubFreezeAndAddBalanceAndAvailableBalance(long userId, double amount);

    /**
     * 增加冻结金额，减少账户余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateAddFreezeAndSubBalance(long userId, double amount);

    /**
     * 减少冻结金额，增加账户余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateSubFreezeAndAddBalance(long userId, double amount);

    /**
     * 更新资金签名
     *
     * @param userId
     * @return
     */
    int updateFundsSign(long userId);
}
