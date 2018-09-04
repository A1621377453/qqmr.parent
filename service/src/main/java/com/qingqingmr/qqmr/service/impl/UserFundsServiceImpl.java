package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.enums.AwardTypeEnum;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.Arith;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.dao.UserBonusMapper;
import com.qingqingmr.qqmr.dao.UserFundsMapper;
import com.qingqingmr.qqmr.dao.UserMapper;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.domain.entity.UserFunds;
import com.qingqingmr.qqmr.service.UserFundsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户账户资金
 *
 * @author liujinjin
 * @datetime 2018年7月4日 下午5:11:06
 */
@Service
public class UserFundsServiceImpl implements UserFundsService {
    private static final Logger LOG = LoggerFactory.getLogger(UserFundsServiceImpl.class);
    @Autowired
    private UserFundsMapper userFundsMapper;
    @Autowired
    private SpringContextProperties springContextProperties;
    @Autowired
    private UserBonusMapper userBonusMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 更新用户资金信息
     *
     * @param userFunds
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public int updateUserFunds(UserFunds userFunds) {
        int rows = userFundsMapper.updateUserFunds(userFunds);
        if (rows < 0) {
            throw new ServiceRuntimeException("更新用户资金信息异常", true);
        }
        return rows;
    }

    /**
     * 微信小程序：查询用户总资产 账户余额+冻结金额
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    @Override
    public UserFunds getUserFundsByUserId(Long userId) {
        return userFundsMapper.getUserFundsByUserId(userId);
    }

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
    @Override
    public ResultInfo getTotalAssets(long userId) {
        ResultInfo resultInfo = new ResultInfo();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        User userById = userMapper.getUserById(userId);

        // 用户累计赚取金额
        Double userAmountByUserId = userBonusMapper.getUserAmountByUserId(userId);
        resultMap.put("userAmountByUserId", userAmountByUserId);

        // 获取用户本月直接赚取金额
        Integer roleType = userById.getRoleType();
        Double userCurMonDirectAmount = 0.0;
        if (roleType == RoleTypeEnum.ADVISER.code) {
            userCurMonDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                    roleType, AwardTypeEnum.DIRECT_AWARD.code);
        } else if (roleType == RoleTypeEnum.BEAUTICIAN.code || roleType == RoleTypeEnum.MEMBER.code) {
            userCurMonDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                    roleType, AwardTypeEnum.ONE_AWARD.code);
        }
        // 获取用户本月间接赚取金额
        Double userCurMonInDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                roleType, AwardTypeEnum.NO_DIRECT_AWARD.code);
        // 获取本月累计赚取的金额
        Double totalAmountCurMon = Arith.add(userCurMonDirectAmount, userCurMonInDirectAmount);
        resultMap.put("totalAmountCurMon", totalAmountCurMon);
        UserFunds userFunds = userFundsMapper.getUserFundsByUserId(userId);
        // 账户余额
        Double balance = userFunds.getBalance();
        resultMap.put("balance", balance);
        // 冻结金额
        Double freeze = userFunds.getFreeze();
        resultMap.put("freeze", freeze);
        // 总资产
        Double totalAmount = Arith.add(balance, freeze);
        resultMap.put("totalAmount", totalAmount);

        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;
    }

    /**
     * 校验用户资金是否正常
     *
     * @param userId
     * @return
     * @author ztl
     */
    @Override
    public ResultInfo checkUserFund(Long userId) {
        ResultInfo resultInfo = new ResultInfo();
        // 查询该用户的账户
        UserFunds userFunds = userFundsMapper.getUserFundsByUserId(userId);
        if (userFunds == null) {
            resultInfo.setInfo(-1, "账户不存在");
            return resultInfo;
        }

        String sign = Security.decodeFunds(userFunds.getUserId(), userFunds.getBalance(), userFunds.getFreeze(),
                springContextProperties.getMd5Key());
        if (!sign.equals(userFunds.getSign())) {
            resultInfo.setInfo(-1, "账户异常");
            return resultInfo;
        }
        resultInfo.setInfo(1, "账户正常");
        resultInfo.setObj(userFunds);
        return resultInfo;
    }

    /**
     * 微信小程序：获取提现时的金额信息（账户余额、可提现金额）
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    @Override
    public ResultInfo getWithdrawalAmountInfo(Long userId) {
        ResultInfo resultInfo = new ResultInfo();
        // 查询该用户的账户
        UserFunds userFunds = userFundsMapper.getUserFundsByUserId(userId);
        if (userFunds == null) {
            resultInfo.setInfo(-1, "账户不存在");
            return resultInfo;
        }
        // 账户余额
        Double balance = userFunds.getBalance();
        // 查询可提现金额
        Double amount = userFunds.getAvailableBalance();

        Map<String, Double> resultMap = new HashMap<String, Double>();
        resultMap.put("balance", balance);
        resultMap.put("amount", amount);
        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;
    }

    /**
     * 增加账户余额
     *
     * @param userId
     * @param balance
     * @return
     */
    @Transactional
    @Override
    public int updateAddBalance(long userId, double balance) {
        return userFundsMapper.updateAddBalance(userId, balance);
    }

    /**
     * 减少账户余额
     *
     * @param userId
     * @param balance
     * @return
     */
    @Transactional
    @Override
    public int updateSubBalance(long userId, double balance) {
        return userFundsMapper.updateSubBalance(userId, balance);
    }

    /**
     * 增加账户可提现余额
     *
     * @param userId
     * @param balance
     * @return
     */
    @Transactional
    @Override
    public int updateAddAvailableBalance(long userId, double balance) {
        return userFundsMapper.updateAddAvailableBalance(userId, balance);
    }

    /**
     * 增加账户余额和账户可提现余额
     *
     * @param userId
     * @param balance
     * @return
     */
    @Transactional
    @Override
    public int updateAddBalanceAndAvailableBalance(long userId, double balance) {
        return userFundsMapper.updateAddBalanceAndAvailableBalance(userId, balance);
    }

    /**
     * 增加冻结金额，减少账户余额和账户可提现余额
     *
     * @param userId
     * @param amount
     * @return
     */
    @Transactional
    @Override
    public int updateAddFreezeAndSubBalanceAndAvailableBalance(long userId, double amount) {
        return userFundsMapper.updateAddFreezeAndSubBalanceAndAvailableBalance(userId, amount);
    }

    /**
     * 减少冻结金额，增加账户余额和账户可提现余额
     *
     * @param userId
     * @param amount
     * @return
     */
    @Transactional
    @Override
    public int updateSubFreezeAndAddBalanceAndAvailableBalance(long userId, double amount) {
        return userFundsMapper.updateSubFreezeAndAddBalanceAndAvailableBalance(userId, amount);
    }

    /**
     * 增加冻结金额，减少账户余额
     *
     * @param userId
     * @param amount
     * @return
     */
    @Transactional
    @Override
    public int updateAddFreezeAndSubBalance(long userId, double amount) {
        return userFundsMapper.updateAddFreezeAndSubBalance(userId, amount);
    }

    /**
     * 减少冻结金额，增加账户余额
     *
     * @param userId
     * @param amount
     * @return
     */
    @Transactional
    @Override
    public int updateSubFreezeAndAddBalance(long userId, double amount) {
        return userFundsMapper.updateSubFreezeAndAddBalance(userId, amount);
    }

    /**
     * 更新资金签名
     *
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public int updateFundsSign(long userId) {
        UserFunds userFunds = getUserFundsByUserId(userId);
        String sign = Security.decodeFunds(userId, userFunds.getBalance(), userFunds.getFreeze(),
                springContextProperties.getMd5Key());
        return userFundsMapper.updateFundsSign(userId, sign);
    }
}
