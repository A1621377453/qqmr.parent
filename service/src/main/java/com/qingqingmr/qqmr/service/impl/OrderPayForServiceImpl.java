package com.qingqingmr.qqmr.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.dao.OrderPayForMapper;
import com.qingqingmr.qqmr.domain.entity.OrderPayFor;
import com.qingqingmr.qqmr.service.OrderPayForService;

/**
 * 订单代付业务接口实现
 *
 * @author liujinjin
 * @datetime 2018年7月5日 下午5:32:06
 */
@Service
public class OrderPayForServiceImpl implements OrderPayForService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderPayForServiceImpl.class);
    @Autowired
    private OrderPayForMapper orderPayForMapper;

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
    @Transactional
    @Override
    public int updateOrderPayForCardNo(long id, String cardNo) {
        int num = orderPayForMapper.updateOrderPayForCardNo(id, cardNo);
        if (num < 1) {
            LOG.error("更新订单代付表里的卡号态失败");
            throw new ServiceRuntimeException("更新代付卡号失败", true);
        }
        return num;
    }

    /**
     * 根据id查询代付信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderPayFor getOrderPayForById(long id) {

        return orderPayForMapper.getOrderPayForById(id);
    }
}
