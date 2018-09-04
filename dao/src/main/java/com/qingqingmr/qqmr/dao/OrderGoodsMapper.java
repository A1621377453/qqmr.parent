package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.bean.OrderDetailVO;
import com.qingqingmr.qqmr.domain.entity.OrderGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单商品详情mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 11:45:51
 */
public interface OrderGoodsMapper {
    /**
     * 根据id删除订单商品详情
     *
     * @param id
     * @return
     */
    int deleteOrderGoodsById(Long id);

    /**
     * 添加订单商品详情
     *
     * @param record
     * @return
     */
    int saveOrderGoods(OrderGoods record);

    /**
     * 根据id查询订单商品详情
     *
     * @param id
     * @return
     */
    OrderGoods getOrderGoodsById(Long id);

    /**
     * 根据id更新订单商品详情
     *
     * @param record
     * @return
     */
    int updateOrderGoodsById(OrderGoods record);

    /**
     * 根据订单id查询订单相关商品信息列表
     *
     * @param orderId
     * @return
     */
    List<OrderGoods> listOrderGoods(Long orderId);

    /**
     * <p>
     * 根据订单id查询订单
     * </p>
     *
     * @param orderId
     * @return
     * @author liujingjing
     * @datetime 2018年7月5日 下午4:36:10
     */
    OrderDetailVO getOrderDetailVOByOrderId(Long orderId);

    /**
     * 获取平台购买会员卡总金额
     *
     * @param type
     * @param dealStatus
     * @param timeBegin
     * @param timeEnd
     * @return
     */
    Double getPurchaseMemberCardAmount(@Param("type") Integer type, @Param("dealStatus") Integer dealStatus, @Param("timeBegin") String timeBegin, @Param("timeEnd") String timeEnd);
}