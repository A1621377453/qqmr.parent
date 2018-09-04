package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.enums.BizzTypeEnum;
import com.qingqingmr.qqmr.dao.PaymentCallBackMapper;
import com.qingqingmr.qqmr.dao.PaymentRequestMapper;
import com.qingqingmr.qqmr.domain.entity.PaymentCallBack;
import com.qingqingmr.qqmr.domain.entity.PaymentRequest;
import com.qingqingmr.qqmr.service.PaymentRequestService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 支付请求日志实现
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-10 16:30:01
 */
@Service
public class PaymentRequestServiceImpl implements PaymentRequestService {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentRequestServiceImpl.class);

    @Autowired
    private PaymentRequestMapper requestMapper;
    @Autowired
    private PaymentCallBackMapper callBackMapper;

    /**
     * 请求日志(新开启一个事务)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean savePaymentRequestSelective(long userId, String bizzOrderNo, BizzTypeEnum bizzType, String orderNo, int payType,
                                               String aynsUrl, String reqParams) {
        LOG.info("保存请求日志开始...");
        LOG.info("userId = {}", userId);
        LOG.info("bizzOrderNo = {}", bizzOrderNo);
        LOG.info("bizzType = {}", bizzType);
        LOG.info("orderNo = {}", orderNo);
        LOG.info("payType = {}", payType);
        LOG.info("aynsUrl = {}", aynsUrl);
        LOG.info("reqParams = {}", reqParams);

        if (userId < 1 || StringUtils.isBlank(bizzOrderNo) || bizzType == null || StringUtils.isBlank(orderNo)
                || payType < 1 || StringUtils.isBlank(reqParams)) {
            LOG.error("请求日志数据不合法");
            return false;
        }

        PaymentRequest request = new PaymentRequest();
        request.setTime(new Date());
        request.setUserId(userId);
        request.setBizzOrderNo(bizzOrderNo);
        request.setBizzType(bizzType.getCode());
        request.setOrderNo(orderNo);
        request.setPayType(payType);
        request.setStatus(-1);
        request.setAynsUrl(aynsUrl);
        request.setReqParams(reqParams);

        int row = requestMapper.savePaymentRequestSelective(request);
        if (row < 1) {
            LOG.error("保存请求日志失败");
            throw new ServiceRuntimeException("保存请求日志失败", true);
        }

        LOG.info("保存请求日志成功");
        return true;
    }

    /**
     * 响应日志(新开启一个事务)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean savePaymentCallBackSelective(String requestOrderNo, String cbParams, int dataType) {
        LOG.info("保存响应日志开始...");
        LOG.info("requestOrderNo = {}", requestOrderNo);
        LOG.info("cbParams = {}", cbParams);
        LOG.info("dataType = {}", dataType);

        if (StringUtils.isBlank(requestOrderNo) || StringUtils.isBlank(cbParams) || dataType < 0) {
            LOG.error("响应日志数据不合法");
            return false;
        }

        PaymentCallBack callBack = new PaymentCallBack(new Date(), requestOrderNo, cbParams, dataType);

        int row = callBackMapper.savePaymentCallBackSelective(callBack);
        if (row < 1) {
            LOG.error("保存响应日志失败");
            throw new ServiceRuntimeException("保存响应日志失败", true);
        }

        LOG.info("保存响应日志成功");
        return true;
    }

    /**
     * 根据第三方交易流水号查询请求日志(新开启一个事务)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public PaymentRequest getPaymentRequestByOrderNo(String orderNo) {

        return requestMapper.getPaymentRequestByOrderNo(orderNo);
    }

    /**
     * 更新请求日志状态
     */
    @Transactional
    @Override
    public int updateStatus(int status, long id) {

        return requestMapper.updateStatus(status, id);
    }

    /**
     * 更新请求日志完成时间
     */
    @Transactional
    @Override
    public int updateCompletedTime(Date completedTime, long id) {

        return requestMapper.updateCompletedTime(completedTime, id);
    }

    /**
     * 根据请求标识查询相应记录
     */
    @Override
    public List<PaymentCallBack> listPaymentCallBackByRequestOrderNo(String requestOrderNo) {

        return callBackMapper.listPaymentCallBackByRequestOrderNo(requestOrderNo);
    }

    /**
     * 根据业务订单号和业务类型查询支付请求(新开启一个事务)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public PaymentRequest getPaymentRequest(String bizzOrderNo, int bizzType) {

        return requestMapper.getPaymentRequest(bizzOrderNo, bizzType);
    }
}
