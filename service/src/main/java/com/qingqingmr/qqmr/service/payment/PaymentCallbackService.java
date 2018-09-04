package com.qingqingmr.qqmr.service.payment;

import com.qingqingmr.qqmr.common.ResultInfo;

/**
 * <p>
 * 支付接口回调
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-10 14:59:30
 */
public interface PaymentCallbackService {

	/**
	 * <p>
	 * 统一订单通知
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月14日 下午6:08:12
	 * @param retXml
	 * @return
	 */
	ResultInfo unifiedOrderCallbackAsyn(String retXml);

}
