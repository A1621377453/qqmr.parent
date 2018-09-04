package com.qingqingmr.qqmr.service.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.base.util.BaseHttpUtil;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.PayWeiXinConstant;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.enums.BizzTypeEnum;
import com.qingqingmr.qqmr.common.util.*;
import com.qingqingmr.qqmr.domain.entity.PaymentRequest;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.service.OrderService;
import com.qingqingmr.qqmr.service.PaymentRequestService;
import com.qingqingmr.qqmr.service.UserService;
import com.qingqingmr.qqmr.service.WithdrawalUserService;
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
import java.util.Map;

/**
 * 支付接口实现
 *
 * @author ztl
 * @datetime 2018-07-10 15:02:57
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private SpringContextProperties contextProperties;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private WithdrawalUserService withdrawalUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * 企业付款到余额
     *
     * @param bizzOrderNo
     * @param request
     * @param param
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo pay(String bizzOrderNo, HttpServletRequest request, Map<String, Object> param) {
        ResultInfo resultInfo = new ResultInfo();

        if (param == null) {
            resultInfo.setInfo(-1, "参数为空");
            return resultInfo;
        }
        if (!param.containsKey("userId") || !param.containsKey("amount")) {
            resultInfo.setInfo(-1, "参数缺失");
            return resultInfo;
        }

        String amount = Arith.pointToRight((Double) param.get("amount"));

        long userId = (long) param.get("userId"); // 用户id
        User user = userService.getUserById(userId);

        Map<String, String> data = new HashMap<>();
        data.put("mch_appid", contextProperties.getMchAppId()); // 申请商户号的appid
        data.put("mchid", contextProperties.getMchntCd()); // 商户号
        // data.put("device_info",""); // 设备号
        data.put("nonce_str", GenerateCode.getOrderNoDigital()); // 随机字符串，不长于32位
        data.put("partner_trade_no", GenerateCode.getOrderNoDigital()); // 商户订单号
        data.put("openid", user.getWxOpenId()); // 用户openid
        data.put("check_name", PayWeiXinConstant.CHECK_NAME_1); // 校验用户姓名选项(不校验)
        // data.put("re_user_name",""); // 收款用户姓名
        data.put("amount", amount); // 金额
        data.put("desc", PayWeiXinConstant.DESC_1); // 企业付款描述信息
        data.put("spbill_create_ip", BaseHttpUtil.getIp(request)); // Ip地址

        String signature = PayWeiXinUtil.sign(data, contextProperties.getApiSign());
        data.put("sign", signature); // 签名

        String xml = PayWeiXinUtil.getRequestXml(data);
        LOG.info("企业付款请求组装xml：{}", xml);

        boolean result = paymentRequestService.savePaymentRequestSelective(userId, bizzOrderNo, BizzTypeEnum.MAKE_LOAN,
                data.get("partner_trade_no"), PayWeiXinConstant.PayType.PAY, "", FastJsonUtil.toJsonString(data));
        if (!result) {
            LOG.error("保存请求日志失败");
            resultInfo.setInfo(-1, "保存数据失败");
            return resultInfo;
        }

        String retXml = HttpUtil.wxHttpPost(contextProperties.getPayUrl(), xml, contextProperties.getCertUrl(), contextProperties.getMchntCd());
        LOG.info("企业付款请求返回xml : {}", retXml);

        if (StringUtils.isBlank(retXml)) {
            LOG.error("企业付款请求返回xml数据为空");
            resultInfo.setInfo(-1, "第三方响应失败");
            return resultInfo;
        }

        result = paymentRequestService.savePaymentCallBackSelective(data.get("partner_trade_no"), retXml, 0);
        if (!result) {
            LOG.error("保存响应日志失败");
            resultInfo.setInfo(-1, "保存数据失败");
            return resultInfo;
        }

        JSONObject json = PayWeiXinUtil.parseXmlToJson(retXml);
        LOG.info("响应结果解析json：{}", json);
        if (json == null) {
            LOG.error("响应结果解析json为空");
            resultInfo.setInfo(-1, "响应结果解析失败");
            return resultInfo;
        }

        String returnCode = json.getString("return_code");
        String returnMsg = json.getString("return_msg");
        String resultCode = json.getString("result_code");
        if (!PayWeiXinConstant.ReturnCode.SUCCESS.equals(returnCode)) {
            // 请求接口失败
            LOG.info("接口请求返回状态失败--【{}】--【{}】", returnCode, returnMsg);
            return getPayInfo(data.get("partner_trade_no"));
        }

        if (PayWeiXinConstant.ResultCode.FAIL.equals(resultCode)) {
            // 接口失败
            LOG.info("接口请求返回结果失败--【{}】--【{}】", resultCode, returnMsg);
            return getPayInfo(data.get("partner_trade_no"));
        }

        // 接口成功
        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequestByOrderNo(data.get("partner_trade_no"));
        int rows = paymentRequestService.updateStatus(1, paymentRequest.getId());
        if (rows < 1) {
            LOG.info("更新状态失败：{}", paymentRequest.getOrderNo());
            resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
            return resultInfo;
        }

        rows = paymentRequestService.updateCompletedTime(new Date(), paymentRequest.getId());
        if (rows < 1) {
            LOG.info("更新状态完成时间失败：{}", paymentRequest.getOrderNo());
            resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
            return resultInfo;
        }

        // 执行业务逻辑
        try {
            resultInfo = withdrawalUserService.transferSuccess(bizzOrderNo);
        } catch (Exception e) {
            LOG.error("付款成功--{}", e.getMessage());
            resultInfo.setInfo(-1, e.getMessage());
            return resultInfo;
        }

        resultInfo.setInfo(1, "付款成功");
        return resultInfo;
    }

    /**
     * 查询企业付款
     *
     * @param orderNo 第三方订单号
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo getPayInfo(String orderNo) {
        ResultInfo resultInfo = new ResultInfo();

        if (StringUtils.isBlank(orderNo)) {
            resultInfo.setInfo(-1, "第三方订单号为空");
            return resultInfo;
        }

        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequestByOrderNo(orderNo);
        if (paymentRequest == null) {
            resultInfo.setInfo(-1, "请求记录为空");
            return resultInfo;
        }

        Map<String, String> data = new HashMap<>();
        data.put("appid", contextProperties.getMchAppId()); // 申请商户号的appid
        data.put("mch_id", contextProperties.getMchntCd()); // 商户号
        data.put("nonce_str", GenerateCode.getOrderNoDigital()); // 随机字符串，不长于32位
        data.put("partner_trade_no", GenerateCode.getOrderNoDigital()); // 商户订单号

        String signature = PayWeiXinUtil.sign(data, contextProperties.getApiSign());
        data.put("sign", signature); // 签名

        String xml = PayWeiXinUtil.getRequestXml(data);
        LOG.info("查询企业付款请求组装xml：{}", xml);

        String retXml = HttpUtil.wxHttpPost(contextProperties.getPayUrl(), xml, contextProperties.getCertUrl(), contextProperties.getMchntCd());
        LOG.info("查询企业付款请求返回xml : {}", retXml);

        if (StringUtils.isBlank(retXml)) {
            LOG.error("查询企业付款请求返回xml数据为空");
            resultInfo.setInfo(-1, "第三方响应失败");
            return resultInfo;
        }

        boolean result = paymentRequestService.savePaymentCallBackSelective(orderNo, retXml, 1);
        if (!result) {
            LOG.error("保存响应日志失败");
            resultInfo.setInfo(-1, "保存数据失败");
            return resultInfo;
        }

        JSONObject json = PayWeiXinUtil.parseXmlToJson(retXml);
        LOG.info("响应结果解析json：{}", json);
        if (json == null) {
            LOG.error("响应结果解析json为空");
            resultInfo.setInfo(-1, "响应结果解析失败");
            return resultInfo;
        }

        String returnCode = json.getString("return_code");
        String returnMsg = json.getString("return_msg");
        if (!PayWeiXinConstant.ReturnCode.SUCCESS.equals(returnCode)) {
            // 接口失败
            LOG.info("接口请求返回状态失败--【{}】--【{}】", returnCode, returnMsg);
            resultInfo.setInfo(-1, returnMsg);
            return resultInfo;
        }

        // 接口成功
        String resultCode = json.getString("result_code");
        if (!PayWeiXinConstant.ResultCode.SUCCESS.equals(resultCode)) {
            LOG.info("业务结果返回状态失败--【{}】--【{}】", json.getString("err_code"), json.getString("err_code_des"));
            resultInfo.setInfo(-1, json.getString("err_code_des"));
            return resultInfo;
        }

        String status = json.getString("status");
        if (PayWeiXinConstant.TransferStatus.FAIL.equals(status)) {
            // 业务失败
            try {
                resultInfo = withdrawalUserService.transferFail(paymentRequest.getBizzOrderNo());
            } catch (Exception e) {
                LOG.error("付款失败--{}", e.getMessage());
                resultInfo.setInfo(-1, e.getMessage());
                return resultInfo;
            }
        } else if (PayWeiXinConstant.TransferStatus.PROCESSING.equals(status)) {
            // 业务处理中
            int rows = paymentRequestService.updateStatus(0, paymentRequest.getId());
            if (rows < 1) {
                LOG.info("更新状态失败：{}", paymentRequest.getOrderNo());
                resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
                return resultInfo;
            }

            rows = paymentRequestService.updateCompletedTime(new Date(), paymentRequest.getId());
            if (rows < 1) {
                LOG.info("更新状态完成时间失败：{}", paymentRequest.getOrderNo());
                resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
                return resultInfo;
            }

            resultInfo.setInfo(-1, "第三方付款处理中");
        } else {
            // 业务成功
            int rows = paymentRequestService.updateStatus(1, paymentRequest.getId());
            if (rows < 1) {
                LOG.info("更新状态失败：{}", paymentRequest.getOrderNo());
                resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
                return resultInfo;
            }

            rows = paymentRequestService.updateCompletedTime(new Date(), paymentRequest.getId());
            if (rows < 1) {
                LOG.info("更新状态完成时间失败：{}", paymentRequest.getOrderNo());
                resultInfo.setInfo(ResultInfo.ALREADY_RUN, "数据已经成功");
                return resultInfo;
            }

            try {
                resultInfo = withdrawalUserService.transferSuccess(paymentRequest.getBizzOrderNo());
            } catch (Exception e) {
                LOG.error("付款成功--{}", e.getMessage());
                resultInfo.setInfo(-1, e.getMessage());
                return resultInfo;
            }
        }

        return resultInfo;
    }

    /**
     * <p>
     * 统一下单-微信小程序先调用该接口，返回数据
     * </p>
     *
     * @param bizzOrderNo
     * @param request
     * @param param
     * @return
     * @author liujingjing
     * @datetime 2018年7月14日 下午5:45:32
     */
    @Override
    public ResultInfo unifiedOrder(String bizzOrderNo, HttpServletRequest request, Map<String, Object> param) {
        ResultInfo resultInfo = new ResultInfo();
        if (param == null) {
            resultInfo.setInfo(-1, "参数为空");
            return resultInfo;
        }
        if (!param.containsKey("userId") || !param.containsKey("amount") || !param.containsKey("goodsName")
                || !param.containsKey("openId")) {
            resultInfo.setInfo(-1, "参数缺失");
            return resultInfo;
        }
        long userId = (long) param.get("userId"); // 用户id

        double amount = (Double) param.get("amount");
        if (amount < 0.01) {
            resultInfo.setInfo(-1, "支付金额不得小于0.01元");
            return resultInfo;
        }
        String goodsName = param.get("goodsName").toString();
        if (StringUtils.isBlank(goodsName)) {
            resultInfo.setInfo(-1, "商品名称不能为空");
            return resultInfo;
        }
        String openId = param.get("openId").toString();
        if (StringUtils.isBlank(openId)) {
            resultInfo.setInfo(-1, "用户的openId不能为空");
            return resultInfo;
        }
        Date now = new Date(); // 当前时间

        Map<String, String> data = new HashMap<>();
        data.put("appid", contextProperties.getMchAppId()); // 微信分配的小程序ID
        data.put("mch_id", contextProperties.getMchntCd()); // 微信支付分配的商户号
        // data.put("device_info", ""); // 设备号
        data.put("nonce_str", GenerateCode.getOrderNoDigital()); // 随机字符串，不长于32位
        data.put("sign_type", PayWeiXinConstant.SIGN_TYPE_MD5); // 签名类型
        data.put("body", PayWeiXinConstant.BUSINESS_NAME + "-" + goodsName); // 商品简单描述，该字段请按照规范传递
        // data.put("detail", ""); // 商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传
        // data.put("attach", ""); // 附加数据，在查询API和支付通知中原样返回
        data.put("out_trade_no", GenerateCode.getOrderNoDigital()); // 商户订单号
        data.put("fee_type", PayWeiXinConstant.FEE_TYPE_CNY);// 标价币种
        data.put("total_fee", Arith.pointToRight(amount));// 标价金额
        data.put("spbill_create_ip", BaseHttpUtil.getIp(request));// 终端IP
        data.put("time_start", DateUtil.dateToString(now, SystemConstant.DATE_TIME_FORMAT_FULL));// 交易起始时间
        data.put("time_expire",
                DateUtil.dateToString(DateUtil.addMinutes(now, 30), SystemConstant.DATE_TIME_FORMAT_FULL)); // 交易结束时间
        // data.put("goods_tag", ""); // 订单优惠标记
        data.put("notify_url", contextProperties.getOrderCallbackAsynUrl()); // 通知地址
        data.put("trade_type", PayWeiXinConstant.TRADE_TYPE_01);// 交易类型
        // data.put("product_id", "");// 商品ID
        // data.put("limit_pay", "");// 指定支付方式
        data.put("openid", openId);// 用户标识

        String signature = PayWeiXinUtil.sign(data, contextProperties.getApiSign());
        data.put("sign", signature); // 签名

        String xml = PayWeiXinUtil.getRequestXml(data);
        LOG.info("微信支付统一下单请求组装xml：{}", xml);

        boolean result = paymentRequestService.savePaymentRequestSelective(userId, bizzOrderNo, BizzTypeEnum.REPAYMENT,
                data.get("out_trade_no"), PayWeiXinConstant.PayType.HOLD, data.get("notify_url"), FastJsonUtil.toJsonString(data));
        if (!result) {
            LOG.error("保存请求日志失败");
            resultInfo.setInfo(-1, "保存数据失败");
            return resultInfo;
        }

        String retXml = HttpUtil.requestPost(contextProperties.getOrderUrl(), xml);
        LOG.info("微信支付统一下单请求返回xml : {}", retXml);
        if (StringUtils.isBlank(retXml)) {
            LOG.error("微信支付请求返回xml数据为空");
            resultInfo.setInfo(-1, "第三方响应失败");
            return resultInfo;
        }

        result = paymentRequestService.savePaymentCallBackSelective(data.get("out_trade_no"), retXml, 0);
        if (!result) {
            LOG.error("保存响应日志失败");
            resultInfo.setInfo(-1, "保存数据失败");
            return resultInfo;
        }

        JSONObject json = PayWeiXinUtil.parseXmlToJson(retXml);
        LOG.info("响应结果解析json：{}", json);
        if (json == null) {
            LOG.error("响应结果解析json为空");
            resultInfo.setInfo(-1, "响应结果解析失败");
            return resultInfo;
        }

        // 解析结果
        String returnCode = json.getString("return_code");// 返回状态码
        String returnMsg = json.getString("return_msg");// 返回信息
        if (!PayWeiXinConstant.ReturnCode.SUCCESS.equals(returnCode)) {
            // 接口失败
            LOG.info("接口请求返回状态失败--【{}】--【{}】", returnCode, returnMsg);
            resultInfo.setInfo(-1, returnMsg);
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

        Map<String, String> respMap = new HashMap<>();
        respMap.put("appId", contextProperties.getMchAppId());
        respMap.put("nonceStr", GenerateCode.getOrderNoDigital());
        respMap.put("package", "prepay_id=" + json.getString("prepay_id")); // 返回的预付单信息
        respMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));// 这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
        respMap.put("signType", PayWeiXinConstant.SIGN_TYPE_MD5);
        String sign = PayWeiXinUtil.sign(respMap, contextProperties.getApiSign());
        respMap.put("paySign", sign);

        resultInfo.setInfo(1, "请求成功", respMap);
        return resultInfo;
    }

    /**
     * <p>
     * 查询订单
     * </p>
     *
     * @param orderNo
     * @return
     * @author liujingjing
     * @datetime 2018年7月14日 下午5:45:51
     */
    @Override
    public ResultInfo orderQuery(String orderNo) {
        ResultInfo resultInfo = new ResultInfo();

        if (StringUtils.isBlank(orderNo)) {
            resultInfo.setInfo(-1, "第三方订单号为空");
            return resultInfo;
        }

        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequestByOrderNo(orderNo);
        if (paymentRequest == null) {
            resultInfo.setInfo(-1, "请求记录为空");
            return resultInfo;
        }

        Map<String, String> data = new HashMap<>();
        data.put("appid", contextProperties.getMchAppId()); // 申请商户号的appid
        data.put("mch_id", contextProperties.getMchntCd()); // 商户号
        data.put("nonce_str", GenerateCode.getOrderNoDigital()); // 随机字符串，不长于32位
        data.put("out_trade_no", orderNo); // 商户订单号
        data.put("sign_type", PayWeiXinConstant.SIGN_TYPE_MD5); // 签名类型

        String signature = PayWeiXinUtil.sign(data, contextProperties.getApiSign());
        data.put("sign", signature); // 签名

        String xml = PayWeiXinUtil.getRequestXml(data);
        LOG.info("查询订单请求组装xml：{}", xml);

        String retXml = HttpUtil.requestPost(contextProperties.getOrderQueryUrl(), xml);
        LOG.info("查询订单请求返回xml : {}", retXml);

        if (StringUtils.isBlank(retXml)) {
            LOG.error("查询订单请求返回xml数据为空");
            resultInfo.setInfo(-1, "第三方响应失败");
            return resultInfo;
        }

        boolean result = paymentRequestService.savePaymentCallBackSelective(orderNo, retXml, 1);
        if (!result) {
            LOG.error("保存响应日志失败");
            resultInfo.setInfo(-1, "保存数据失败");
            return resultInfo;
        }

        JSONObject json = PayWeiXinUtil.parseXmlToJson(retXml);
        LOG.info("响应结果解析json：{}", json);
        if (json == null) {
            LOG.error("响应结果解析json为空");
            resultInfo.setInfo(-1, "响应结果解析失败");
            return resultInfo;
        }

        String returnCode = json.getString("return_code");
        String returnMsg = json.getString("return_msg");
        if (!PayWeiXinConstant.ReturnCode.SUCCESS.equals(returnCode)) {
            // 接口失败
            LOG.info("接口请求返回状态失败--【{}】--【{}】", returnCode, returnMsg);
            resultInfo.setInfo(-1, returnMsg);
            return resultInfo;
        }

        // 接口成功
        String resultCode = json.getString("result_code");
        if (!PayWeiXinConstant.ResultCode.SUCCESS.equals(resultCode)) {
            LOG.info("业务结果返回状态失败--【{}】--【{}】", json.getString("err_code"), json.getString("err_code_des"));
            resultInfo.setInfo(-1, json.getString("err_code_des"));
            return resultInfo;
        }

        String tradeState = json.getString("trade_state");
        if (PayWeiXinConstant.OrderTradeState.CLOSED.equals(tradeState)) {
            // 已关闭
            resultInfo.setInfo(-1, "交易已关闭");
        } else if (PayWeiXinConstant.OrderTradeState.REFUND.equals(tradeState)) {
            // 转入退款
            resultInfo.setInfo(-1, "交易转入退款");
        } else if (PayWeiXinConstant.OrderTradeState.NOTPAY.equals(tradeState)) {
            // 未支付
            resultInfo.setInfo(-1, "交易未支付");
        } else if (PayWeiXinConstant.OrderTradeState.REVOKED.equals(tradeState)) {
            // 已撤销（刷卡支付）
            resultInfo.setInfo(-1, "已撤销");
        } else if (PayWeiXinConstant.OrderTradeState.USERPAYING.equals(tradeState)) {
            // 用户支付中
            resultInfo.setInfo(-1, "用户支付中");
        } else if (PayWeiXinConstant.OrderTradeState.PAYERROR.equals(tradeState)) {
            // 支付失败
            resultInfo.setInfo(-1, "支付失败");
        } else {
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
                resultInfo = orderService.payOrderSuccess(orderNo, Double.parseDouble(json.getString("realAmount")),
                        json.getString("transaction_id"), "购买会员卡");
            } catch (Exception e) {
                LOG.error("执行业务逻辑异常--{}", e.getMessage());
                throw new ServiceRuntimeException("系统异常", true);
            }

            if (resultInfo.getCode() < 1) {
                LOG.info("执行业务逻辑异常--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }

            // 支付成功
            resultInfo.setInfo(1, "支付成功");
        }

        return resultInfo;
    }
}
