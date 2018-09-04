package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.Arith;
import com.qingqingmr.qqmr.dao.*;
import com.qingqingmr.qqmr.domain.bean.ResultVO;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.PlatformOverviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台概况实现类
 *
 * @author crn
 * @datetime 2018-07-11 17:39:01
 */
@Service
public class PlatformOverviewServiceImpl implements PlatformOverviewService {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformOverviewServiceImpl.class);

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private PlatformFeeMapper PlatformFeeMapper;
    @Autowired
    private UserBonusMapper userBonusMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WithdrawalUserMapper withdrawalUserMapper;
    @Autowired
    private EventSupervisorService eventSupervisorService;

    /**
     * 获取平台概况
     *
     * @param timeBegin
     * @param timeEnd
     * @param request
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @datetime 2018/7/11 20:35
     * @author crn
     */
    @Override
    public ResultInfo getPlatformOverview(String timeBegin, String timeEnd, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 购买会员
        Double purchaseMemberCardAmount = orderGoodsMapper.getPurchaseMemberCardAmount(Goods.GoodsTypeEnum.MEMBERSHIP_CARD.code, Order.DealStatusEnum.SUCCESS.code, timeBegin, timeEnd);
        purchaseMemberCardAmount = purchaseMemberCardAmount == null ? 0L : purchaseMemberCardAmount;
        // 成功提现
        Double withdrawalAmount = withdrawalUserMapper.getWithdrawalAmount(WithdrawalUser.WithdrawStatusEnum.SUCCESS.code, timeBegin, timeEnd);
        withdrawalAmount = withdrawalAmount == null ? 0L : withdrawalAmount;
        // 资金进出总计
        Double fundTotal = Arith.sub(purchaseMemberCardAmount, withdrawalAmount);
        resultMap.put("purchaseMemberCardAmount", purchaseMemberCardAmount);
        resultMap.put("withdrawalAmount", withdrawalAmount);
        resultMap.put("fundTotal", fundTotal);

        // 会员数量
        Long memberCount = userMapper.countUser(RoleTypeEnum.MEMBER.code, timeBegin, timeEnd);
        memberCount = memberCount == null ? 0L : memberCount;
        // 顾客数量
        Long customerCount = userMapper.countUser(RoleTypeEnum.CUSTOMER.code, timeBegin, timeEnd);
        customerCount = customerCount == null ? 0L : customerCount;
        // 会员统计
        Long userCount = memberCount + customerCount;
        resultMap.put("memberCount", memberCount);
        resultMap.put("customerCount", customerCount);
        resultMap.put("userCount", userCount);

        List<ResultVO> roleAmount = userBonusMapper.getRoleAmount(timeBegin, timeEnd);
        // 顾问奖励
        Double adviserAward = null;
        // 美容师奖励
        Double beauticianAward = null;
        // 会员奖励
        Double memberAward = null;
        if (null != roleAmount && roleAmount.size() > 0) {
            for (ResultVO resultPO : roleAmount) {
                if (RoleTypeEnum.MEMBER.code == resultPO.getType()) {
                    memberAward = resultPO.getAmount();
                } else if (RoleTypeEnum.BEAUTICIAN.code == resultPO.getType()) {
                    beauticianAward = resultPO.getAmount();
                } else if (RoleTypeEnum.ADVISER.code == resultPO.getType()) {
                    adviserAward = resultPO.getAmount();
                }
            }
        }
        adviserAward = adviserAward == null ? 0L : adviserAward;
        beauticianAward = beauticianAward == null ? 0L : beauticianAward;
        memberAward = memberAward == null ? 0L : memberAward;
        // 奖励统计
        Double awardTotal = Arith.add(Arith.add(adviserAward, beauticianAward), memberAward);
        resultMap.put("adviserAward", adviserAward);
        resultMap.put("beauticianAward", beauticianAward);
        resultMap.put("memberAward", memberAward);
        resultMap.put("awardTotal", awardTotal);

        List<ResultVO> platformFee = PlatformFeeMapper.getPlatformFee(timeBegin, timeEnd);
        // 佣金管理费
        Double commissionFee = null;
        // 提现手续费
        Double withdrawFee = null;
        if (null != platformFee && platformFee.size() > 0) {
            for (ResultVO resultPO : platformFee) {
                if (PlatformFee.FeeTypeEnum.COMMISSION.code == resultPO.getType()) {
                    commissionFee = resultPO.getAmount();
                } else if (PlatformFee.FeeTypeEnum.WITHDRAW.code == resultPO.getType()) {
                    withdrawFee = resultPO.getAmount();
                }
            }
        }
        commissionFee = commissionFee == null ? 0L : commissionFee;
        withdrawFee = withdrawFee == null ? 0L : withdrawFee;
        // 收取费用
        Double feeTotal = Arith.add(commissionFee, withdrawFee);
        resultMap.put("commissionFee", commissionFee);
        resultMap.put("withdrawFee", withdrawFee);
        resultMap.put("feeTotal", feeTotal);

        // 添加管理员操作事件
        try {
            resultInfo = eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.SETTING_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;
    }

    /**
     * 获取当日平台概况
     *
     * @return
     */
    @Override
    public ResultInfo getDayPlatformOverview() {
        ResultInfo resultInfo = new ResultInfo();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 当日成功提现
        Map<Integer, Double> dayWithdrawalSuccess = withdrawalUserMapper.getDayWithdrawal(WithdrawalUser.WithdrawStatusEnum.SUCCESS.code);
        Map<Integer, Double> dayWithdrawalPending = withdrawalUserMapper.getDayWithdrawal(WithdrawalUser.WithdrawStatusEnum.PENDING_REVIEW.code);
        resultMap.put("dayWithdrawalSuccess", dayWithdrawalSuccess);
        resultMap.put("dayWithdrawalPending", dayWithdrawalPending);
        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;
    }
}
