package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.domain.entity.OrderPayFor;

/**
 * 订单代付业务接口
 *
 * @author liujinjin
 * @datetime 2018年7月5日  下午5:23:07
 */
public interface OrderPayForService {

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
    int updateOrderPayForCardNo(long id, String cardNo);

    /**
     * 根据id查询代付信息
     *
     * @param id
     * @return
     */
    OrderPayFor getOrderPayForById(long id);
}
