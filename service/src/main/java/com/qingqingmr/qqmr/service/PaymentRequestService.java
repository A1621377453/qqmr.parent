package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.enums.BizzTypeEnum;
import com.qingqingmr.qqmr.domain.entity.PaymentCallBack;
import com.qingqingmr.qqmr.domain.entity.PaymentRequest;

import java.util.Date;
import java.util.List;

/**
 * 请求日志
 *
 * @author ztl
 * @datetime 2018-07-10 16:06:10
 */
public interface PaymentRequestService {

    /**
     * <p>
     * 请求日志
     * </p>
     *
     * @param userId
     * @param bizzOrderNo
     * @param bizzType
     * @param orderNo
     * @param payType
     * @param aynsUrl
     * @param reqParams
     * @return
     * @author ztl
     */
    boolean savePaymentRequestSelective(long userId, String bizzOrderNo, BizzTypeEnum bizzType, String orderNo, int payType, String aynsUrl, String reqParams);

    /**
     * <p>
     * 响应日志
     * </p>
     *
     * @param requestOrderNo
     * @param cbParams
     * @param dataType
     * @return
     * @author ztl
     */
    boolean savePaymentCallBackSelective(String requestOrderNo, String cbParams, int dataType);

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
     * 更新请求日志状态
     * </p>
     *
     * @param status
     * @param id
     * @return
     * @author ztl
     */
    int updateStatus(int status, long id);

    /**
     * <p>
     * 更新请求日志完成时间
     * </p>
     *
     * @param completedTime
     * @param id
     * @return
     * @author ztl
     */
    int updateCompletedTime(Date completedTime, long id);

    /**
     * <p>
     * 根据请求标识查询相应记录
     * </p>
     *
     * @param requestOrderNo
     * @return
     * @author ztl
     */
    List<PaymentCallBack> listPaymentCallBackByRequestOrderNo(String requestOrderNo);

    /**
     * 根据业务订单号和业务类型查询支付请求
     *
     * @param bizzOrderNo
     * @param bizzType
     * @return
     * @author ztl
     */
    PaymentRequest getPaymentRequest(String bizzOrderNo, int bizzType);
}
