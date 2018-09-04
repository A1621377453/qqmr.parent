package com.qingqingmr.qqmr.wechat.handler;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.domain.entity.Order;
import com.qingqingmr.qqmr.domain.entity.OrderPayFor;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.service.OrderPayForService;
import com.qingqingmr.qqmr.service.OrderService;
import com.qingqingmr.qqmr.service.UserService;
import com.qingqingmr.qqmr.service.payment.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 微信支付
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月17日 下午1:59:25
 */
@Component
public class WxPaymentHandler {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderPayForService orderPayForService;
	@Autowired
	private SpringContextProperties springContextProperties;
	@Autowired
	private UserService userService;
	@Autowired
	private PaymentService paymentService;

	/**
	 * <p>
	 * 订单支付
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月17日 下午2:17:01
	 * @param parameters
	 * @param request
	 * @return
	 */
	public String paymentOrder(Map<String, String> parameters, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String orderId = parameters.get("orderId");
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "解析用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());
		if (StringUtils.isBlank(orderId)) {
			json.put("code", -1);
			json.put("msg", "支付订单信息有误");
			return FastJsonUtil.toJsonString(json);
		}
		Order order = orderService.getOrderById(Long.parseLong(orderId));
		if (order == null) {
			json.put("code", -1);
			json.put("msg", "暂无需要支付的订单");
			return FastJsonUtil.toJsonString(json);
		}
		if (order.getStatus() != Order.DealStatusEnum.OBLIGATION.code) {
			json.put("code", -1);
			json.put("msg", "该订单已经取消或者付款");
			return FastJsonUtil.toJsonString(json);
		}
		if (order.getUserId().longValue() != userId) {
			json.put("code", -1);
			json.put("msg", "用户订单信息有误");
			return FastJsonUtil.toJsonString(json);
		}

		User user = userService.getUserById(userId);
		// 封装第三方参数
		String bizzOrderNo = order.getOrderNo();
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("goodsName", order.getRemark());
		params.put("amount", order.getAmount());
		params.put("openId", user.getWxOpenId());

        long realUserId = user.getId();
        if (order.getPayFor()) {
            OrderPayFor orderPayFor = orderPayForService.getOrderPayForById(order.getPayForId());
            User payForUser = userService.getUserByMobile(orderPayFor.getMobile());
            realUserId = payForUser.getId();
        }
        int num = orderService.countUserBePaidByUserId(realUserId);
        if (num > 1) {
            json.put("code", -1);
            json.put("msg", "用户已经存在被代付订单!");
            return FastJsonUtil.toJsonString(json);
        }
		
		ResultInfo resultInfo = paymentService.unifiedOrder(bizzOrderNo, request, params);
		if (resultInfo.getCode() < 1) {
			json.put("code", -1);
			json.put("msg", resultInfo.getMsg());
			return FastJsonUtil.toJsonString(json);
		}

		json.put("respMap", resultInfo.getObj());
		json.put("code", 1);
		json.put("msg", "订单支付");
		return FastJsonUtil.toJsonString(json);
	}

}
