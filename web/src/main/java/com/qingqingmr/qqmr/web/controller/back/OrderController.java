package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.bo.OrderSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.domain.bean.OrderDetailVO;
import com.qingqingmr.qqmr.domain.bean.OrderVO;
import com.qingqingmr.qqmr.domain.entity.Order;
import com.qingqingmr.qqmr.service.OrderService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单管理控制器
 *
 * @author crn
 * @datetime 2018-07-13 20:15:50
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping(value = "/back/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 根据搜索条件查询平台订单列表
     *
     * @param orderSearchBO
     * @return java.lang.String
     * @datetime 2018/7/14 11:20
     * @author crn
     */
    @RequestMapping(value = "/listplatformorder", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listPlatformOrder(OrderSearchBO orderSearchBO, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == orderSearchBO) {
            orderSearchBO = new OrderSearchBO();
        }
        PageResult<OrderVO> orderVOPageResult = orderService.listPlatformOrder(orderSearchBO, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", orderVOPageResult);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 根据订单id获取订单详情信息
     *
     * @param id
     * @return java.lang.String
     * @datetime 2018/7/15 15:08
     * @author crn
     */
    @RequestMapping(value = "/getorderdetailbyorderid", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getOrderDetailByOrderId(Long id, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == id || 0 == id) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }

        OrderDetailVO orderDetailVO = orderService.getOrderDetailByOrderId(id, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", orderDetailVO);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 取消订单
     *
     * @param id
     * @return java.lang.String
     * @datetime 2018/7/15 17:37
     * @author crn
     */
    @RequestMapping(value = "/cancelorder", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String cancelOrder(Long id, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == id || 0 == id) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }

        // 手动取消订单
        ResultInfo resultInfo = orderService.cancelOrder(id, Order.CancelTypeEnum.Manual.code, Order.DealStatusEnum.CLOSE.code, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }
}
