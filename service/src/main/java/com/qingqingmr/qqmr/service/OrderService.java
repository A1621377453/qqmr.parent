package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.base.bo.OrderSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.bean.OrderDetailVO;
import com.qingqingmr.qqmr.domain.bean.OrderVO;
import com.qingqingmr.qqmr.domain.entity.Order;
import com.qingqingmr.qqmr.domain.entity.OrderGoods;
import com.qingqingmr.qqmr.domain.entity.OrderPayFor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午8:09:42
 */
public interface OrderService {

    /**
     * <p>
     * 分页查询订单
     * </p>
     *
     * @param currPage
     * @param pageSize
     * @param userId
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午8:13:48
     */
    PageResult<OrderDetailVO> listOrdersPage(int currPage, int pageSize, long userId);

    /**
     * <p>
     * 通过订单Id查询订单详情
     * </p>
     *
     * @param orderId
     * @return
     * @author liujingjing
     * @datetime 2018年7月5日 下午2:57:57
     */
    OrderGoods getOrderGoodsById(long orderId);

    /**
     * <p>
     * 通过订单Id查询订单详情
     * </p>
     *
     * @param orderId
     * @return
     * @author liujingjing
     * @datetime 2018年7月5日 下午2:57:57
     */
    OrderDetailVO getOrderDetailVOByOrderId(long orderId);

    /**
     * <p>
     * 根据代付id查询代付信息
     * </p>
     *
     * @param id
     * @return
     * @author liujingjing
     * @datetime 2018年7月5日 下午4:47:46
     */
    OrderPayFor getOrderPayForInfo(long id);

    /**
     * <p>
     * 更新订单取消订单信息
     * </p>
     *
     * @param orderId
     * @param cancelType
     * @param status
     * @return
     * @author liujingjing
     * @datetime 2018年7月6日 下午3:16:15
     */
    ResultInfo updateOrderCancelInfo(long orderId, int cancelType, int status);

    /**
     * <p>
     * 通过订单id查询订单
     * </p>
     *
     * @param orderId
     * @return
     * @author liujingjing
     * @datetime 2018年7月6日 下午4:15:26
     */
    Order getOrderById(long orderId);

    /**
     * <p>
     * 通过时间查询订单
     * </p>
     *
     * @param date
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 下午1:50:57
     */
    List<Order> listOrderByTime(Date date);

    /**
     * 微信小程序：新增订单信息
     *
     * @param userId
     * @param goodsId
     * @param payType
     * @param isPayFor
     * @param mobile
     * @param name
     * @return
     * @author liujinjin
     */
    ResultInfo saveOrderInfo(long userId, long goodsId, int payType, boolean isPayFor, String mobile, String name);

    /**
     * 后台根据搜索条件获取平台所有订单列表
     *
     * @param orderSearchBO
     * @param request
     * @return
     */
    PageResult<OrderVO> listPlatformOrder(OrderSearchBO orderSearchBO, HttpServletRequest request);

    /**
     * 根据订单id获取订单详情信息
     *
     * @param id
     * @param request
     * @return
     */
    OrderDetailVO getOrderDetailByOrderId(Long id, HttpServletRequest request);

    /**
     * <p>
     * 支付订单成功
     * </p>
     *
     * @param orderNo
     * @param realAmount
     * @param transactionId
     * @param remark
     * @return
     * @author liujingjing
     * @datetime 2018年7月15日 下午2:53:13
     */
    ResultInfo payOrderSuccess(String orderNo, double realAmount, String transactionId, String remark);

    /**
     * 通过用户id查询用户未完成的订单记录
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    int countUserNotFinishOrderByUserId(Long userId);

    /**
     * 通过用户id被代付的未完成的订单记录
     *
     * @param userId
     * @return
     */
    int countUserBePaidByUserId(Long userId);

    /**
     * 手动取消订单
     *
     * @param id
     * @param code
     * @param code1
     * @param request
     * @return
     */
    ResultInfo cancelOrder(Long id, int code, int code1, HttpServletRequest request);

    /**
     * 自动取消订单
     *
     * @return
     */
    ResultInfo autoCancelOrder();

    /**
     * 支付成功更新第三方订单号
     *
     * @param transactionId
     * @param id
     * @return
     */
    int updateTransactionId(String transactionId, long id);
}
