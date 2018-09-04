package com.qingqingmr.qqmr.wechat.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.util.Arith;
import com.qingqingmr.qqmr.common.util.Encrypt;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.domain.bean.OrderDetailVO;
import com.qingqingmr.qqmr.domain.entity.DealUser;
import com.qingqingmr.qqmr.domain.entity.Order;
import com.qingqingmr.qqmr.domain.entity.OrderPayFor;
import com.qingqingmr.qqmr.domain.entity.UserFunds;
import com.qingqingmr.qqmr.domain.entity.WithdrawalUser;
import com.qingqingmr.qqmr.service.DealUserService;
import com.qingqingmr.qqmr.service.OrderService;
import com.qingqingmr.qqmr.service.UserFundsService;
import com.qingqingmr.qqmr.service.UserService;
import com.qingqingmr.qqmr.service.WithdrawalUserService;

/**
 * <p>
 * 我的（总资产、我的订单、资金流水）
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午6:13:50
 */
@Component
public class MyAccountHandler {
	private static final Logger LOG = LoggerFactory.getLogger(MyAccountHandler.class);
	@Autowired
	private DealUserService dealUserService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SpringContextProperties springContextProperties;
	@Autowired
	private UserFundsService userFundsService;
	@Autowired
	private WithdrawalUserService withdrawalUserService;
	@Autowired
	private UserService userService;

	/**
	 * <p>
	 * 分页查询交易记录（资金流水）
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午8:05:06
	 * @param parameters
	 * @return
	 */
	public String listDealUserPage(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		String currentPageStr = parameters.get("currPage");
		String pageNumStr = parameters.get("pageSize");

		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());

		if (!StrUtil.isNumeric(currentPageStr) || !StrUtil.isNumeric(pageNumStr)) {
			json.put("code", -1);
			json.put("msg", "分页参数不正确");
			return FastJsonUtil.toJsonString(json);
		}

		int currPage = Integer.parseInt(currentPageStr);
		int pageSize = Integer.parseInt(pageNumStr);

		currPage = currPage < 1 ? 1 : currPage;
		pageSize = pageSize < 1 ? 10 : pageSize;

		PageResult<DealUser> pageResult = dealUserService.listDealUserPage(currPage, pageSize, userId);
		json.put("code", 1);
		json.put("msg", "查询成功");
		json.put("page", pageResult.getPage());
		json.put("totalCount", pageResult.getTotalCount());
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * <p>
	 * 分页查询我的订单
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午8:07:18
	 * @param parameters
	 * @return
	 */
	public String listOrderPage(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		String currentPageStr = parameters.get("currPage");
		String pageNumStr = parameters.get("pageSize");

		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());
		if (!StrUtil.isNumeric(currentPageStr) || !StrUtil.isNumeric(pageNumStr)) {
			json.put("code", -1);
			json.put("msg", "分页参数不正确");
			return FastJsonUtil.toJsonString(json);
		}

		int currPage = Integer.parseInt(currentPageStr);
		int pageSize = Integer.parseInt(pageNumStr);

		currPage = currPage < 1 ? 1 : currPage;
		pageSize = pageSize < 1 ? 10 : pageSize;

		PageResult<OrderDetailVO> pageResult = orderService.listOrdersPage(currPage, pageSize, userId);
		json.put("code", 1);
		json.put("msg", "查询成功");
		json.put("page", pageResult.getPage());
		json.put("totalCount", pageResult.getTotalCount());
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * <p>
	 * 根据订单Id查询订单详情
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月5日 下午2:42:17
	 * @param parameters
	 * @return
	 */
	public String getOrderGoodsById(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("orderId");
		long orderId = Long.parseLong(signId);
		// 查询订单详情
		OrderDetailVO orderDetailVO = orderService.getOrderDetailVOByOrderId(orderId);
		if (orderDetailVO == null) {
			json.put("code", -1);
			json.put("msg", "该订单不存在");
			return FastJsonUtil.toJsonString(json);
		}
		// 判断是否代付
		if (orderDetailVO.getIsPayFor() == true) {
			long id = orderDetailVO.getPayForId();
			OrderPayFor orderPayFor = orderService.getOrderPayForInfo(id);
			if (orderPayFor != null) {
				json.put("orderPayForName", orderPayFor.getName());
				json.put("orderPayForMobile", orderPayFor.getMobile());
			}
		}
		json.put("orderDetailVO", orderDetailVO);
		json.put("code", 1);
		json.put("msg", "用户订单详情查询成功");
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * <p>
	 * 取消订单（手动）
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月6日 下午2:59:57
	 * @param parameters
	 * @return
	 */
	public String cancelOrder(Map<String, String> parameters) {
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
			json.put("msg", "取消订单信息有误");
			return FastJsonUtil.toJsonString(json);
		}
		Order order = orderService.getOrderById(Long.parseLong(orderId));
		if (order == null) {
			json.put("code", -1);
			json.put("msg", "暂无取消订单");
			return FastJsonUtil.toJsonString(json);
		}
		if (order.getStatus() != 1) {
			json.put("code", -1);
			json.put("msg", "该订单已经取消或者付款");
			return FastJsonUtil.toJsonString(json);
		}
		if (order.getUserId().longValue() != userId) {
			json.put("code", -1);
			json.put("msg", "取消订单信息有误");
			return FastJsonUtil.toJsonString(json);
		}

		ResultInfo resultInfo = null;
		try {
			resultInfo = orderService.updateOrderCancelInfo(order.getId(), 2, Order.DealStatusEnum.CLOSE.code);
		} catch (Exception e) {
			LOG.error("取消订单信息时异常--{}", e.getMessage());
			json.put("code", -1);
			json.put("msg", e.getMessage());
			return FastJsonUtil.toJsonString(json);
		}
		if (resultInfo.getCode() < 1) {
			json.put("code", -1);
			json.put("msg", "取消订单失败");
			return FastJsonUtil.toJsonString(json);
		}

		json.put("code", 1);
		json.put("msg", "取消订单成功");
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * <p>
	 * 查询总资产
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月9日 下午8:13:43
	 * @param parameters
	 * @return
	 */
	public String getTotalAssets(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "解析用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());
		ResultInfo resultInfo = userFundsService.getTotalAssets(userId);
		json.put("resultInfo", resultInfo);
		json.put("code", 1);
		json.put("msg", "查询总资产成功");
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * 分页查询提现记录
	 * 
	 * @author liujinjin
	 * @datetime 2018年7月10日 下午1:55:06
	 * @param parameters
	 * @return
	 */
	public String listWithdrawalUserPage(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		String signId = parameters.get("userId");
		String currentPageStr = parameters.get("currPage");
		String pageNumStr = parameters.get("pageSize");

		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());

		if (!StrUtil.isNumeric(currentPageStr) || !StrUtil.isNumeric(pageNumStr)) {
			json.put("code", -1);
			json.put("msg", "分页参数不正确");
			return FastJsonUtil.toJsonString(json);
		}

		int currPage = Integer.parseInt(currentPageStr);
		int pageSize = Integer.parseInt(pageNumStr);

		currPage = currPage < 1 ? 1 : currPage;
		pageSize = pageSize < 1 ? 10 : pageSize;

		PageResult<WithdrawalUser> pageResult = withdrawalUserService.listWithdrawalUserPage(currPage, pageSize,
				userId);
		json.put("code", 1);
		json.put("msg", "查询成功");
		json.put("page", pageResult.getPage());
		json.put("totalCount", pageResult.getTotalCount());
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * 添加提现记录
	 * 
	 * @author liujinjin
	 * @datetime 2018年7月12日 下午6:55:06
	 * @param parameters
	 * @return
	 */
	public String addWithdrawalUserInfo(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		if (parameters == null) {
			json.put("code", -1);
			json.put("msg", "输入参数有误!");
			return FastJsonUtil.toJsonString(json);
		}
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		parameters.put("userId", result.getObj().toString());
		long userId = Long.parseLong(result.getObj().toString());

		if (StringUtils.isBlank(parameters.get("amount"))) {
			json.put("code", -1);
			json.put("msg", "提现金额不能为空");
			return FastJsonUtil.toJsonString(json);
		}
		Double amount = Double.parseDouble(parameters.get("amount"));// 提现金额
		Double availableBalance = Double.parseDouble(parameters.get("availableBalance"));// 可提现金额
		String payPassword = parameters.get("payPassword");
		payPassword = Encrypt.decryptDES(payPassword, springContextProperties.getAppDesKey());
		
		

		// 判断输入的交易密码是否正确
		int num = userService.checkPsyPassword(userId, payPassword);
		if (num == -1) {
			json.put("code", -1);
			json.put("msg", "交易密码不正确");
			return FastJsonUtil.toJsonString(json);
		}
		// 单笔单日、单人单日提现金额不能超过两万
		if (Arith.sub(amount, 20000.00) > 0) {
			json.put("code", -1);
			json.put("msg", "单笔单日提现金额不能超过20000");
			return FastJsonUtil.toJsonString(json);
		}
		Double sumAmount = withdrawalUserService.getWithdrawTotalByUserId(userId);
		if (sumAmount != null) {
			if (Arith.sub(sumAmount, 20000.00) >= 0) {
				json.put("code", -1);
				json.put("msg", "单人单日提现总额不能超过20000");
				return FastJsonUtil.toJsonString(json);
			}
		}
		UserFunds userFunds = userFundsService.getUserFundsByUserId(userId);
		if (userFunds == null) {
			json.put("code", -1);
			json.put("msg", "查询用户资金信息失败");
			return FastJsonUtil.toJsonString(json);
		}
		// 账户余额
		Double balance = userFunds.getBalance();
		if (Arith.sub(amount, balance) > 0) {
			json.put("code", -1);
			json.put("msg", "提现金额不能超过账户余额");
			return FastJsonUtil.toJsonString(json);
		}
		// 查询可提现金额
		Double availableAmount = userFunds.getAvailableBalance();
		if (Arith.sub(amount, availableAmount) > 0) {
			json.put("code", -1);
			json.put("msg", "提现金额不能超过可提现金额");
			return FastJsonUtil.toJsonString(json);
		}

		ResultInfo resultInfo = new ResultInfo();
		try {
			// 添加提现记录
			resultInfo = withdrawalUserService.saveWithdrawalUserInfo(userId, amount, availableBalance);
			if (resultInfo.getCode() < 1) {
				json.put("code", -1);
				json.put("msg", "新增提现记录失败！");
			}
		} catch (Exception e) {
			LOG.error("添加提现记录，出现异常--{}", e.getMessage());
			json.put("code", result.getCode());
			json.put("msg", "添加提现记录失败");
			return FastJsonUtil.toJsonString(json);
		}

		json.put("code", resultInfo.getCode());
		json.put("msg", resultInfo.getMsg());
		return FastJsonUtil.toJsonString(json);
	}

	/**
	 * 获取提现时的金额信息（账户余额、可提现金额）
	 * 
	 * @author liujinjin
	 * @datetime 2018年7月15日 上午11:42:06
	 * @param parameters
	 * @return
	 */
	public String getWithdrawalAmountInfo(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		if (parameters == null) {
			json.put("code", -1);
			json.put("msg", "输入参数有误!");
			return FastJsonUtil.toJsonString(json);
		}

		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());
		ResultInfo resultInfo = userFundsService.getWithdrawalAmountInfo(userId);
		json.put("code", -1);
		json.put("msg", "查询信息失败！");
		json.put("data", resultInfo.getObj());
		return FastJsonUtil.toJsonString(resultInfo);
	}

	/**
	 * 查询当前用户是否有未完成的订单
	 * 
	 * @author liujinjin
	 * @datetime 2018年7月16日 下午5:03:06
	 * @param parameters
	 * @return
	 */
	public String countNotFinishOrder(Map<String, String> parameters) {
		JSONObject json = new JSONObject();
		if (parameters == null) {
			json.put("code", -1);
			json.put("msg", "输入参数有误!");
			return FastJsonUtil.toJsonString(json);
		}
		String signId = parameters.get("userId");
		ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
				springContextProperties.getAppDesKey());
		if (result.getCode() < 1) {
			json.put("code", ResultInfo.ERROR_100);
			json.put("msg", "用户id有误");
			return FastJsonUtil.toJsonString(json);
		}
		long userId = Long.parseLong(result.getObj().toString());
		int num = orderService.countUserNotFinishOrderByUserId(userId);
		if (num > 0) {
			json.put("code", -1);
			json.put("msg", "请先完成已有的订单");
			return FastJsonUtil.toJsonString(json);
		}
		json.put("code", 1);
		json.put("msg", "查询成功");
		return FastJsonUtil.toJsonString(json);
	}
}
