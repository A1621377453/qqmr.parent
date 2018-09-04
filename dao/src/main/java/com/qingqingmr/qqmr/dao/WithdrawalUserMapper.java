package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.bean.WithdrawUserVO;
import com.qingqingmr.qqmr.domain.entity.WithdrawalUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户提现表
 *
 * @author liujinjin
 * @datetime 2018-7-4 11:13:33
 */
public interface WithdrawalUserMapper {
    /**
     * 保存用户提现信息
     *
     * @param withdrawalUser
     * @return
     * @author liujinjin
     */
    int saveWithdrawalUser(WithdrawalUser withdrawalUser);

    /**
     * 通过id查询用户提现信息
     *
     * @param id
     * @return
     * @author liujinjin
     */
    WithdrawalUser getWithdrawalUserById(Long id);

    /**
     * 审核用户提现信息
     *
     * @param withdrawalUser
     * @return
     * @author ztl
     */
    int updateWithdrawalUserById(WithdrawalUser withdrawalUser);

    /**
     * 查询提现列表
     *
     * @return
     * @author ztl
     */
    List<WithdrawUserVO> listWithdrawalUser(Map<String, String> parameters);

    /**
     * 后台-提现列表详情
     *
     * @param id
     * @return
     */
    WithdrawUserVO getWithdrawUserVOById(Long id);

    /**
     * 微信小程序：分页查询提现记录
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    List<WithdrawalUser> listWithdrawalUserPage(Long userId);

    /**
     * 获取平台提现总额
     *
     * @param withdrawStatus
     * @param timeBegin
     * @param timeEnd
     * @return
     */
    Double getWithdrawalAmount(@Param("withdrawStatus") Integer withdrawStatus, @Param("timeBegin") String timeBegin, @Param("timeEnd") String timeEnd);

    /**
     * 根据提现状态获取当天提现人数和金额
     *
     * @param withdrawStatus
     * @return
     */
    Map<Integer, Double> getDayWithdrawal(@Param("withdrawStatus") Integer withdrawStatus);

    /**
     * 根据订单号查询提现的订单
     *
     * @param serviceOrderNo
     * @return
     * @author ztl
     */
    WithdrawalUser getWithdrawUserByOrderNo(String serviceOrderNo);

    /**
     * 更新提现结果
     *
     * @param status
     * @param id
     * @param completeInfo
     * @return
     * @author ztl
     */
    int updateWithdrawResultById(@Param("id") Long id, @Param("status") Integer status, @Param("completeInfo") String completeInfo);
    
    /**
     *查询当前用户当天已申请提现总额 
     * 
     * @param userId
     * @author liujinjin
     */
    Double getWithdrawTotalByUserId(Long userId);
}