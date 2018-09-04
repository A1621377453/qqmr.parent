package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.WithdrawalUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WithdrawalUserService {

    /**
     * 微信小程序：分页查询提现记录
     *
     * @return
     * @author liujinjin
     */
    PageResult<WithdrawalUser> listWithdrawalUserPage(int currPage, int pageSize, long userId);

    /**
     * 微信小程序：添加提现记录
     *
     * @return
     * @author liujinjin
     */
    ResultInfo saveWithdrawalUserInfo(Long userId, Double amount, Double availableBalance);

    /**
     * 后台-提现列表
     *
     * @author ztl
     */
    ResultInfo listWithdrawUser(Integer currPage, Integer pageSize, Map<String, String> parameters, HttpServletRequest request);

    /**
     * 后台-提现详情
     *
     * @param id
     * @param request
     * @return
     * @author ztl
     */
    ResultInfo getWithdrawUserVOById(Long id, HttpServletRequest request);

    /**
     * 根据订单号查询提现的订单
     *
     * @param serviceOrderNo
     * @return
     * @author ztl
     */
    WithdrawalUser getWithdrawUserByOrderNo(String serviceOrderNo);

    /**
     * 后台--提现审核
     *
     * @param withdrawalUser
     * @param request
     * @return
     * @author ztl
     */
    ResultInfo updateWithdrawUserById(WithdrawalUser withdrawalUser, HttpServletRequest request);

    /**
     * 更新提现结果
     *
     * @param status
     * @param id
     * @param completeInfo
     * @return
     * @author ztl
     */
    int updateWithdrawResultById(Long id, Integer status, String completeInfo);

    /**
     * 企业付款失败的业务处理
     *
     * @param bizzOrderNo
     * @author ztl
     */
    ResultInfo transferFail(String bizzOrderNo);

    /**
     * 企业付款成功的业务处理
     *
     * @param bizzOrderNo
     * @author ztl
     */
    ResultInfo transferSuccess(String bizzOrderNo);

    /**
     *查询当前用户当天已申请提现总额 
     * 
     * @param userId
     * @author liujinjin
     */
    Double getWithdrawTotalByUserId(Long userId);
}
