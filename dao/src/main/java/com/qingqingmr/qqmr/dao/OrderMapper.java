package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.base.bo.OrderSearchBO;
import com.qingqingmr.qqmr.domain.bean.OrderDetailVO;
import com.qingqingmr.qqmr.domain.bean.OrderVO;
import com.qingqingmr.qqmr.domain.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 订单mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 13:52:25
 */
public interface OrderMapper {
    /**
     * 根据id删除订单
     *
     * @param id
     * @return
     */
    int deleteOrderById(Long id);

    /**
     * 添加订单
     *
     * @param record
     * @return
     */
    int saveOrder(Order record);

    /**
     * 根据id获取订单
     *
     * @param id
     * @return
     */
    Order getOrderById(Long id);

    /**
     * 根据用户id查询所有订单列表（生成时间倒序）
     *
     * @param userId
     * @return
     */
    List<OrderDetailVO> listOrders(Long userId);

    /**
     * <p>
     * 更新订单取消订单信息
     * </p>
     *
     * @param status
     * @param cancelType
     * @param cancelTime
     * @param id
     * @return
     * @author liujingjing
     * @datetime 2018年7月6日 下午3:16:15
     */
    int updateOrderCancelInfo(@Param("status") int status, @Param("cancelType") int cancelType,
                              @Param("cancelTime") Date cancelTime, @Param("id") long id);

    /**
     * <p>
     * 根据指定时间查询订单集合
     * </p>
     *
     * @param date
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 下午1:58:32
     */
    List<Order> listOrderByTime(Date date);

    /**
     * 微信小程序：查询支付成功的订单总数
     *
     * @param
     * @return
     * @author liujinjin
     */
    Integer getOrderPaySuccessCount();

    /**
     * 微信小程序：通过用户id查询已经交易成功的订单信息
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    Order getOrderCodeByUserId(Long userId);

    /**
     * 后台根据搜索条件获取平台所有订单列表
     *
     * @param orderSearchBO
     * @return
     */
    List<OrderVO> listPlatformOrder(OrderSearchBO orderSearchBO);

    /**
     * 根据订单id获取订单详情信息
     *
     * @param id
     * @return
     */
    OrderDetailVO getOrderDetailByOrderId(Long id);

    /**
     * <p>
     * 根据订单号查询订单信息
     * </p>
     *
     * @param orderNo
     * @return
     * @author liujingjing
     * @datetime 2018年7月15日 下午3:00:15
     */
    Order getOrderByOrderNo(String orderNo);

    /**
     * <p>
     * 更新订单的状态和支付时间
     * </p>
     *
     * @param payTime
     * @param realAmount
     * @param id
     * @return
     * @author liujingjing
     * @datetime 2018年7月15日 下午4:09:17
     */
    int updateOrderPaySuccessInfo(@Param("payTime") Date payTime, @Param("realAmount") Double realAmount,
                                  @Param("id") long id);

    /**
     * 通过用户id获取用户未完成的订单记录
     *
     * @param userId
     * @return
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
     * 支付成功更新第三方订单号
     *
     * @param transactionId
     * @param id
     * @return
     */
    int updateTransactionId(@Param("transactionId") String transactionId, @Param("id") long id);
}