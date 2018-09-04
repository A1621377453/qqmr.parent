package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.PaymentRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 支付请求第三方记录表
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-10 16:10:09
 */
public interface PaymentRequestMapper {

    /**
     * <p>
     * 添加
     * </p>
     *
     * @param paymentRequest
     * @return
     * @author ztl
     */
    int savePaymentRequestSelective(PaymentRequest paymentRequest);

    /**
     * <p>
     * 根据第三方交易流水号查询请求日志
     * </p>
     *
     * @param orderNo
     * @return
     * @author ztl
     */
    PaymentRequest getPaymentRequestByOrderNo(String orderNo);

    /**
     * <p>
     * 更新状态（status=0，1）
     * </p>
     *
     * @param status
     * @param id
     * @return
     * @author ztl
     */
    int updateStatus(@Param("status") int status, @Param("id") long id);

    /**
     * <p>
     * 更新完成时间
     * </p>
     *
     * @param completedTime
     * @param id
     * @return
     * @author ztl
     */
    int updateCompletedTime(@Param("completedTime") Date completedTime, @Param("id") long id);

    /**
     * 根据业务订单号和业务类型查询支付请求
     *
     * @param bizzOrderNo
     * @param bizzType
     * @return
     * @author ztl
     */
    PaymentRequest getPaymentRequest(@Param("bizzOrderNo") String bizzOrderNo, @Param("bizzType") int bizzType);

}
