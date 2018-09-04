package com.qingqingmr.qqmr.dao;


import com.qingqingmr.qqmr.domain.entity.PaymentCallBack;

import java.util.List;

/**
 * <p>
 * 支付第三方回调记录表
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-10 16:10:58
 */
public interface PaymentCallBackMapper {

	/**
	 * <p>
	 * 添加
	 * </p>
	 *
	 * @author ztl
	 * @param paymentCallBack
	 * @return
	 */
	int savePaymentCallBackSelective(PaymentCallBack paymentCallBack);

	/**
	 * <p>
	 * 根据请求标识查询记录
	 * </p>
	 *
	 * @author ztl
	 * @param requestOrderNo
	 * @return
	 */
	List<PaymentCallBack> listPaymentCallBackByRequestOrderNo(String requestOrderNo);
}
