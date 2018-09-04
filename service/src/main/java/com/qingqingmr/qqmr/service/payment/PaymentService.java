package com.qingqingmr.qqmr.service.payment;

import com.qingqingmr.qqmr.common.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 支付接口
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-10 14:55:19
 */
public interface PaymentService {

    /**
     * 企业付款到余额
     *
     * @param bizzOrderNo
     * @param request
     * @param param
     * @return
     * @author ztl
     */
    ResultInfo pay(String bizzOrderNo, HttpServletRequest request, Map<String, Object> param);

    /**
     * 查询企业付款
     *
     * @param orderNo
     * @return
     * @author ztl
     */
    ResultInfo getPayInfo(String orderNo);
    
	/**
	 * <p>
	 * 统一下单
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月14日 下午5:45:32
	 * @param bizzOrderNo
	 * @param request
	 * @param param
	 * @return
	 */
	ResultInfo unifiedOrder(String bizzOrderNo, HttpServletRequest request, Map<String, Object> param);

	/**
	 * <p>
	 * 查询订单
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月14日 下午5:45:51
	 * @param orderNo
	 * @return
	 */
	ResultInfo orderQuery(String orderNo);
}
