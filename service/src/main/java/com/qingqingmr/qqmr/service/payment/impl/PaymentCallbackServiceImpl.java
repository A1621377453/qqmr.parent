package com.qingqingmr.qqmr.service.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.PayWeiXinConstant;
import com.qingqingmr.qqmr.common.util.Arith;
import com.qingqingmr.qqmr.common.util.PayWeiXinUtil;
import com.qingqingmr.qqmr.domain.entity.PaymentRequest;
import com.qingqingmr.qqmr.service.OrderService;
import com.qingqingmr.qqmr.service.PaymentRequestService;
import com.qingqingmr.qqmr.service.payment.PaymentCallbackService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月14日 下午6:05:30
 */
@Service
public class PaymentCallbackServiceImpl implements PaymentCallbackService {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentCallbackServiceImpl.class);

    @Autowired
    private SpringContextProperties contextProperties;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private OrderService orderService;

    /**
     * <p>
     * 统一订单通知
     * </p>
     *
     * @param retXml
     * @return
     * @author liujingjing
     * @datetime 2018年7月14日 下午6:08:12
     */
    @Transactional
    @Override
    public ResultInfo unifiedOrderCallbackAsyn(String retXml) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(retXml)) {
            LOG.info("统一订单通知参数为空");
            resultInfo.setInfo(-1, "回调参数为空");
        }

		JSONObject json = PayWeiXinUtil.parseXmlToJson(retXml);

		if (!json.containsKey("return_code")) {
			LOG.info("统一订单通知参数非法");
			resultInfo.setInfo(-1, "回调参数非法");
		}

        String returnCode = json.getString("return_code");
        String returnMsg = json.getString("return_msg");
        if (!PayWeiXinConstant.ReturnCode.SUCCESS.equals(returnCode)) {
            LOG.info("统一订单通知返回状态失败--【{}】--【{}】", returnCode, returnMsg);
            resultInfo.setInfo(-1, returnMsg);
        }

        String orderNo = json.getString("out_trade_no");
        boolean result = paymentRequestService.savePaymentCallBackSelective(orderNo, retXml, 0);
        if (!result) {
            LOG.error("保存响应日志失败");
            resultInfo.setInfo(-1, "保存数据失败");
            return resultInfo;
        }

        String resultCode = json.getString("result_code");
        if (!PayWeiXinConstant.ResultCode.SUCCESS.equals(resultCode)) {
            LOG.info("业务结果返回状态失败--【{}】--【{}】", json.getString("err_code"), json.getString("err_code_des"));
            resultInfo.setInfo(-1, json.getString("err_code_des"));
            return resultInfo;
        }

        Map<String, String> callbackData = new HashMap<>();
        json.forEach((k, v) -> callbackData.put(k, v.toString()));
        String checkSign = PayWeiXinUtil.sign(callbackData, contextProperties.getApiSign());
        if (!checkSign.equals(json.getString("sign"))) {
            LOG.info("第三方返回签名与本地签名--【{}】--【{}】", json.getString("sign"), checkSign);
            resultInfo.setInfo(-1, "校验签名失败");
            return resultInfo;
        }

        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequestByOrderNo(orderNo);
        if (paymentRequest == null) {
            LOG.error("请求记录为空");
            resultInfo.setInfo(-1, "请求记录为空");
            return resultInfo;
        }

        int rows = paymentRequestService.updateStatus(1, paymentRequest.getId());
        if (rows < 1) {
            LOG.info("更新状态失败：{}", paymentRequest.getOrderNo());
            resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        rows = paymentRequestService.updateCompletedTime(new Date(), paymentRequest.getId());
        if (rows < 1) {
            LOG.info("更新状态完成时间失败：{}", paymentRequest.getOrderNo());
            resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        // 执行业务逻辑
        try {
            resultInfo = orderService.payOrderSuccess(paymentRequest.getBizzOrderNo(), Arith.pointToLeft(Double.parseDouble(json.getString("total_fee"))),
                    json.getString("transaction_id"), "购买会员卡");
        } catch (Exception e) {
            LOG.error("执行业务逻辑异常--{}", e.getMessage());
            throw new ServiceRuntimeException("系统异常", true);
        }

        if (resultInfo.getCode() < 1) {
            LOG.info("执行业务逻辑异常--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        resultInfo.setInfo(1, "通知成功");
        return resultInfo;
    }

}
