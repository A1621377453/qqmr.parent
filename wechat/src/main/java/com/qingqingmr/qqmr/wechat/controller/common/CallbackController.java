package com.qingqingmr.qqmr.wechat.controller.common;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.PayWeiXinConstant;
import com.qingqingmr.qqmr.common.util.PayWeiXinUtil;
import com.qingqingmr.qqmr.service.payment.PaymentCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 回调控制器
 * 
 * @author ythu
 * @datetime 2018年7月24日17:39:22
 */
@Controller
@RequestMapping("/common/callback")
public class CallbackController {
	private final static Logger LOG = LoggerFactory.getLogger(CallbackController.class);

	@Autowired
	private PaymentCallbackService paymentCallbackService;

	/**
	 * 统一订单通知
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/unifiedordercallbackasyn", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "text/plain; charset=UTF-8" })
	@ResponseBody
	public String unifiedOrderCallbackAsyn(HttpServletRequest request) throws IOException {
		ServletInputStream inputStream = request.getInputStream();
		
		StringBuilder content = new StringBuilder();
		byte[] b = new byte[1024];
		int lens = -1;
		while ((lens = inputStream.read(b)) > 0) {
			content.append(new String(b, 0, lens));
		}
		String retXml = content.toString();// 内容	
		
		LOG.info("读取返回参数--retXml--【{}】", retXml);
		ResultInfo resultInfo = null;
		try {
			resultInfo = paymentCallbackService.unifiedOrderCallbackAsyn(retXml);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("通知执行异常--【{}】", e.getMessage());
			return PayWeiXinUtil.renderWechatAsyn(PayWeiXinConstant.ReturnCode.FAIL, e.getMessage());
		}
		LOG.info("通知执行结果--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
		if (resultInfo.getCode() < 1) {

			return PayWeiXinUtil.renderWechatAsyn(PayWeiXinConstant.ReturnCode.FAIL, resultInfo.getMsg());
		}

		return PayWeiXinUtil.renderWechatAsyn(PayWeiXinConstant.ReturnCode.SUCCESS, resultInfo.getMsg());
	}

}
