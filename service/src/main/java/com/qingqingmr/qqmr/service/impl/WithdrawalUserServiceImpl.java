package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.PayWeiXinConstant;
import com.qingqingmr.qqmr.common.enums.BizzTypeEnum;
import com.qingqingmr.qqmr.common.util.GenerateCode;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.dao.WithdrawalUserMapper;
import com.qingqingmr.qqmr.domain.bean.SupervisorCurrentVO;
import com.qingqingmr.qqmr.domain.bean.WithdrawUserVO;
import com.qingqingmr.qqmr.domain.entity.DealUser;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.domain.entity.UserFunds;
import com.qingqingmr.qqmr.domain.entity.WithdrawalUser;
import com.qingqingmr.qqmr.service.*;
import com.qingqingmr.qqmr.service.payment.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WithdrawalUserServiceImpl implements WithdrawalUserService {
    private static final Logger LOG = LoggerFactory.getLogger(WithdrawalUserServiceImpl.class);

    @Autowired
    private WithdrawalUserMapper withdrawalUserMapper;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private DealUserService dealUserService;
    @Autowired
    private UserFundsService userFundsService;
    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private SpringContextProperties springContextProperties;


    /**
     * 微信小程序：分页查询提现记录
     *
     * @return
     * @author liujinjin
     */
    @Override
    public PageResult<WithdrawalUser> listWithdrawalUserPage(int currPage, int pageSize, long userId) {
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<WithdrawalUser> result = new PageResult<WithdrawalUser>(currPage, pageSize);

        Page<WithdrawalUser> page = PageHelper.startPage(currPage, pageSize);
        List<WithdrawalUser> listDealUserPages = withdrawalUserMapper.listWithdrawalUserPage(userId);

        result.setPage(listDealUserPages);
        result.setTotalCount(page.getTotal());

        return result;
    }

    /**
     * 微信小程序：添加提现记录
     *
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public ResultInfo saveWithdrawalUserInfo(Long userId, Double amount, Double availableBalance) {
        ResultInfo resultInfo = new ResultInfo();
        WithdrawalUser WithdrawalUser = new WithdrawalUser();
        try {
            WithdrawalUser.setTime(new Date());
            WithdrawalUser.setUserId(userId);
            WithdrawalUser.setAmount(amount);
            WithdrawalUser.setFee(0.00);
            WithdrawalUser.setAvailableBalance(availableBalance);
            String orderNo = GenerateCode.getOrderNo(BizzTypeEnum.REPAYMENT.getPrefix());
            WithdrawalUser.setServiceOrderNo(orderNo);
            WithdrawalUser.setType(1);
            WithdrawalUser.setStatus(1);
            int rows = withdrawalUserMapper.saveWithdrawalUser(WithdrawalUser);
            if (rows < 1) {
                resultInfo.setInfo(-1, "保存提现记录表失败");
                LOG.info("添加提现记录，保存提现记录时：", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }

            //校验资金是否正常
            resultInfo = userFundsService.checkUserFund(userId);
            if (resultInfo.getCode() < 1) {
                return resultInfo;
            }

            //修改用户资金表里的数据：账户余额、冻结金额
            rows = userFundsService.updateAddFreezeAndSubBalanceAndAvailableBalance(userId, amount);
            if (rows < 1) {
                resultInfo.setInfo(-1, "修改资金失败");
                LOG.info("添加提现记录，修改资金表账户余额、冻结金额、可提现金额时：", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }

            rows = userFundsService.updateFundsSign(userId);
            if (rows < 1) {
                resultInfo.setInfo(-1, "修改资金签名失败");
                LOG.info("添加提现记录，修改资金签名时：", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }

            //增加提现失败的交易记录
            DealUser dealUser = new DealUser(userId, WithdrawalUser.getId().toString(), 2, BizzTypeEnum.REPAYMENT.getCode(), amount, "提现支出");
            rows = dealUserService.saveDealUser(dealUser);
            if (rows < 1) {
                resultInfo.setInfo(-1, "保存交易记录表失败");
                LOG.info("添加提现记录时：", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }
        } catch (Exception e) {
            LOG.error("添加提现记录时，出现异常--{}", e.getMessage());
            throw new ServiceRuntimeException("添加提现记录时，出现异常", true);
        }
        resultInfo.setInfo(1, "新增提现记录成功");
        return resultInfo;
    }

    /**
     * 后台-提现列表
     *
     * @author ztl
     */
    @Override
    public ResultInfo listWithdrawUser(Integer currPage, Integer pageSize, Map<String, String> parameters, HttpServletRequest request) {
        ResultInfo info = new ResultInfo();
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<WithdrawUserVO> result = new PageResult<WithdrawUserVO>(currPage, pageSize);

        Page<WithdrawUserVO> page = PageHelper.startPage(currPage, pageSize);
        List<WithdrawUserVO> listWithdrawalUser = withdrawalUserMapper.listWithdrawalUser(parameters);

        result.setPage(listWithdrawalUser);
        result.setTotalCount(page.getTotal());

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.WITHDRAW_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        info.setInfo(1, "查询成功");
        info.setObj(result);
        return info;
    }

    /**
     * 后台-提现列表详情
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public ResultInfo getWithdrawUserVOById(Long id, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();

        WithdrawUserVO withdrawUser = withdrawalUserMapper.getWithdrawUserVOById(id);

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.WITHDRAW_SHOW_DETAIL);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        resultInfo.setInfo(1, "查询成功");
        resultInfo.setObj(withdrawUser);
        return resultInfo;
    }

    /**
     * 根据订单号查询提现的订单
     *
     * @param serviceOrderNo
     * @return
     * @author ztl
     */
    @Override
    public WithdrawalUser getWithdrawUserByOrderNo(String serviceOrderNo) {
        return withdrawalUserMapper.getWithdrawUserByOrderNo(serviceOrderNo);
    }

    /**
     * 提现审核
     *
     * @param withdrawalUser
     * @param request
     * @return
     * @author ztl
     */
    @Transactional
    @Override
    public ResultInfo updateWithdrawUserById(WithdrawalUser withdrawalUser, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        SupervisorCurrentVO supervisorCurrent = supervisorService.getSupervisorCurrent(request);

        if (supervisorCurrent == null) {
            resultInfo.setInfo(ResultInfo.EXCEPTION_CODE_1, ResultInfo.EXCEPTION_MSG_1);
            return resultInfo;
        }

        withdrawalUser.setAuditSupervisorId(supervisorCurrent.getId());
        if (PayWeiXinConstant.REFUSE_WITHDRAW.equals(withdrawalUser.getAuditOpinion())) {
            withdrawalUser.setStatus(-1);
            try {
                resultInfo = transferFail(withdrawalUser.getServiceOrderNo());
                if (resultInfo.getCode() < 1) {
                    return resultInfo;
                }
            } catch (Exception e) {
                LOG.error("付款失败--{}", e.getMessage());
                resultInfo.setInfo(-1, e.getMessage());
                return resultInfo;
            }
            //更新审核结果 更新状态和备注
            if (withdrawalUserMapper.updateWithdrawalUserById(withdrawalUser) < 0) {
                throw new ServiceRuntimeException("审核失败", true);
            }
        } else {

            if (springContextProperties.getIsOpenPay().equals("0")) {
                try {
                    resultInfo = transferSuccess(withdrawalUser.getServiceOrderNo());
                    if (resultInfo.getCode() < 1) {
                        return resultInfo;
                    }
                } catch (Exception e) {
                    LOG.error("付款失败--{}", e.getMessage());
                    resultInfo.setInfo(-1, e.getMessage());
                    return resultInfo;
                }
            } else {
                withdrawalUser.setStatus(2);
                //更新审核结果 更新状态和备注
                if (withdrawalUserMapper.updateWithdrawalUserById(withdrawalUser) < 0) {
                    throw new ServiceRuntimeException("处理中", true);
                }

                //调用第三方的企业付款的逻辑
                Map<String, Object> param = new HashMap<>();
                param.put("userId", withdrawalUser.getUserId());
                param.put("amount", withdrawalUser.getAmount());

                try {
                    resultInfo = paymentService.pay(withdrawalUser.getServiceOrderNo(), request, param);
                    if (resultInfo.getCode() < 1) {
                        return resultInfo;
                    }
                } catch (Exception e) {
                    LOG.error("企业付款--{}", e.getMessage());
                    resultInfo.setInfo(-1, e.getMessage());
                    return resultInfo;
                }
            }

        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.WITHDRAW_CHECK);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        resultInfo.setInfo(1, "审核成功");
        return resultInfo;
    }

    /**
     * 更新提现结果
     *
     * @param status
     * @param id
     * @param completeInfo
     * @return
     * @author ztl
     */
    @Override
    public int updateWithdrawResultById(Long id, Integer status, String completeInfo) {
        return withdrawalUserMapper.updateWithdrawResultById(id, status, completeInfo);
    }

    /**
     * 企业付款成功的业务处理
     *
     * @param bizzOrderNo
     * @author ztl
     */
    @Transactional
    public ResultInfo transferSuccess(String bizzOrderNo) {
        ResultInfo resultInfo = new ResultInfo();

        if (StringUtils.isBlank(bizzOrderNo)) {
            resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return resultInfo;
        }

        //查询当前提现状况
        WithdrawalUser withdrawUser = getWithdrawUserByOrderNo(bizzOrderNo);
        if (withdrawUser == null) {
            resultInfo.setInfo(-1, "提现数据为空");
            return resultInfo;
        }

        //校验资金是否正常
        resultInfo = userFundsService.checkUserFund(withdrawUser.getUserId());
        if (resultInfo.getCode() < 1) {
            return resultInfo;
        }

        //更新账户数据
        UserFunds userFunds = (UserFunds) resultInfo.getObj();
        double reeeze = userFunds.getFreeze() - withdrawUser.getAmount();
        userFunds.setFreeze(reeeze);
        userFunds.setSign(Security.decodeFunds(userFunds.getUserId(), userFunds.getBalance(), reeeze, springContextProperties.getMd5Key()));

        //更新提现的订单的结果
        if (updateWithdrawResultById(withdrawUser.getId(), 3, "提现成功") < 0) {
            throw new ServiceRuntimeException("更新提现订单异常", true);
        }

        try {
            userFundsService.updateUserFunds(userFunds);
        } catch (Exception e) {
            LOG.error("更新账户资金--{}", e.getMessage());
            resultInfo.setInfo(-1, e.getMessage());
            return resultInfo;
        }

        resultInfo.setInfo(1, "提现成功");
        return resultInfo;

    }

    /**
     * 企业付款失败的业务处理
     *
     * @param bizzOrderNo
     * @author ztl
     */
    @Transactional
    public ResultInfo transferFail(String bizzOrderNo) {
        ResultInfo resultInfo = new ResultInfo();

        if (StringUtils.isBlank(bizzOrderNo)) {
            resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return resultInfo;
        }

        //查询当前提现状况
        WithdrawalUser withdrawUser = getWithdrawUserByOrderNo(bizzOrderNo);
        if (withdrawUser == null) {
            resultInfo.setInfo(-1, "提现数据为空");
            return resultInfo;
        }

        //校验资金是否正常
        resultInfo = userFundsService.checkUserFund(withdrawUser.getUserId());
        if (resultInfo.getCode() < 1) {
            return resultInfo;
        }

        //更新账户数据
        UserFunds userFunds = (UserFunds) resultInfo.getObj();
        double reeeze = userFunds.getFreeze() - withdrawUser.getAmount();
        double balance = userFunds.getBalance() + withdrawUser.getAmount();
        double avaBalance = userFunds.getAvailableBalance() + withdrawUser.getAmount();
        userFunds.setAvailableBalance(avaBalance);
        userFunds.setFreeze(reeeze);
        userFunds.setBalance(balance);
        userFunds.setSign(Security.decodeFunds(userFunds.getUserId(), balance, reeeze, springContextProperties.getMd5Key()));

        //增加提现失败的交易记录
        DealUser dealUser = new DealUser();
        dealUser.setUserId(withdrawUser.getUserId());
        dealUser.setAmount(withdrawUser.getAmount());
        dealUser.setDealType(1);
        dealUser.setOperationType(BizzTypeEnum.REPAYMENT.getCode());
        dealUser.setRemark("提现退回");
        dealUser.setServiceOrderNo(withdrawUser.getServiceOrderNo());

        //更新提现的订单的结果
        if (updateWithdrawResultById(withdrawUser.getId(), -1, "提现退回") < 0) {
            throw new ServiceRuntimeException("更新提现订单异常", true);
        }

        try {
            userFundsService.updateUserFunds(userFunds);
            dealUserService.saveDealUser(dealUser);
        } catch (Exception e) {
            LOG.error("更新账户或者添加交易记录--{}", e.getMessage());
            resultInfo.setInfo(-1, e.getMessage());
            return resultInfo;
        }

        resultInfo.setInfo(1, "提现退回");
        return resultInfo;

    }

    /**
     * 查询当前用户当天已申请提现总额
     *
     * @param userId
     * @author liujinjin
     */
    @Override
    public Double getWithdrawTotalByUserId(Long userId) {
        return withdrawalUserMapper.getWithdrawTotalByUserId(userId);
    }

}
