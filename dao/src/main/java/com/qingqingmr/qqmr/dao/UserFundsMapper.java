package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.bean.UserFundsVO;
import com.qingqingmr.qqmr.domain.entity.UserFunds;
import org.apache.ibatis.annotations.Param;

/**
 * 用户资金信息表
 *
 * @author liujinjin
 * @datetime 2018-7-4 11:13:33
 */
public interface UserFundsMapper {

    /**
     * 通过id删除
     *
     * @param id
     * @return
     * @author liujinjin
     */
    int deleteUserFundsById(Long id);

    /**
     * 保存用户资金信息
     *
     * @param userFunds
     * @return
     * @author liujinjin
     */
    int saveUserFunds(UserFunds userFunds);
    
    /**
     * 更新用户资金
     *
     * @param userFunds
     * @return
     * @author liujinjin
     */
    int updateUserFunds(UserFunds userFunds);

    /**
     * 根据用户id查询用户资金信息
     *
     * @param userId
     * @return
     */
    UserFundsVO getUserFundsByUserId(Long userId);

    /**
     * 增加账户余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateAddBalance(@Param("userId") long userId, @Param("balance") double balance);


    /**
     * 减少账户余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateSubBalance(@Param("userId") long userId, @Param("balance") double balance);

    /**
     * 增加账户可提现余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateAddAvailableBalance(@Param("userId") long userId, @Param("balance") double balance);

    /**
     * 增加账户余额和账户可提现余额
     *
     * @param userId
     * @param balance
     * @return
     */
    int updateAddBalanceAndAvailableBalance(@Param("userId") long userId, @Param("balance") double balance);

    /**
     * 增加冻结金额，减少账户余额和账户可提现余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateAddFreezeAndSubBalanceAndAvailableBalance(@Param("userId") long userId, @Param("amount") double amount);

    /**
     * 减少冻结金额，增加账户余额和账户可提现余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateSubFreezeAndAddBalanceAndAvailableBalance(@Param("userId") long userId, @Param("amount") double amount);

    /**
     * 增加冻结金额，减少账户余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateAddFreezeAndSubBalance(@Param("userId") long userId, @Param("amount") double amount);

    /**
     * 减少冻结金额，增加账户余额
     *
     * @param userId
     * @param amount
     * @return
     */
    int updateSubFreezeAndAddBalance(@Param("userId") long userId, @Param("amount") double amount);

    /**
     * 更新资金签名
     *
     * @param userId
     * @param sign
     * @return
     */
    int updateFundsSign(@Param("userId") long userId, @Param("sign") String sign);

}