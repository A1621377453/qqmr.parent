package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.OrderPayFor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单代付信息mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 13:45:59
 */
public interface OrderPayForMapper {
    /**
     * 根据id删除订单代付信息
     *
     * @param id
     * @return
     */
    int deleteOrderPayForById(Long id);

    /**
     * 添加订单代付信息
     *
     * @param record
     * @return
     */
    int saveOrderPayFor(OrderPayFor record);

    /**
     * 根据id查询订单代付信息
     *
     * @param id
     * @return
     */
    OrderPayFor getOrderPayForById(Long id);

    /**
     * 更新订单代付信息
     *
     * @param record
     * @return
     */
    int updateOrderPayForById(OrderPayFor record);

    /**
     * 通过手机号码查询订单代付表信息
     *
     * @param mobile 手机号
     * @return
     * @author liujinjin
     */
    List<OrderPayFor> listOrderPayForByMobile(String mobile);

    /**
     * <p>
     * 更改卡号和关联状态
     * </p>
     *
     * @param id
     * @param cardNo
     * @return
     * @author liujingjing
     * @datetime 2018年7月15日 下午6:34:00
     */
    int updateOrderPayForCardNo(@Param("id") long id, @Param("cardNo") String cardNo);
}