package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.bo.OrderSearchBO;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.enums.BizzTypeEnum;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.GenerateCode;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.dao.GoodsMapper;
import com.qingqingmr.qqmr.dao.OrderGoodsMapper;
import com.qingqingmr.qqmr.dao.OrderMapper;
import com.qingqingmr.qqmr.dao.OrderPayForMapper;
import com.qingqingmr.qqmr.domain.bean.OrderDetailVO;
import com.qingqingmr.qqmr.domain.bean.OrderVO;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.domain.entity.Order.DealStatusEnum;
import com.qingqingmr.qqmr.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午8:14:49
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderPayForMapper orderPayForMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private DealUserService dealUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderPayForService orderPayForService;
    @Autowired
    private MembershipCardService membershipCardService;
    @Autowired
    private UserBonusService userBonusService;
    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private UserInfoService userInfoService;

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
    @Override
    public PageResult<OrderDetailVO> listOrdersPage(int currPage, int pageSize, long userId) {
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<OrderDetailVO> result = new PageResult<OrderDetailVO>(currPage, pageSize);

        Page<OrderDetailVO> page = PageHelper.startPage(currPage, pageSize);
        List<OrderDetailVO> listOrdersPages = orderMapper.listOrders(userId);

        result.setPage(listOrdersPages);
        result.setTotalCount(page.getTotal());

        return result;
    }

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
    @Override
    public OrderGoods getOrderGoodsById(long orderId) {
        return orderGoodsMapper.getOrderGoodsById(orderId);
    }

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
    @Override
    public OrderDetailVO getOrderDetailVOByOrderId(long orderId) {
        return orderGoodsMapper.getOrderDetailVOByOrderId(orderId);
    }

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
    @Override
    public OrderPayFor getOrderPayForInfo(long id) {
        return orderPayForMapper.getOrderPayForById(id);
    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultInfo updateOrderCancelInfo(long orderId, int cancelType, int status) {
        ResultInfo result = new ResultInfo();
        Order order = orderMapper.getOrderById(orderId);
        if (order == null) {
            result.setInfo(-1, "订单信息有误");
            return result;
        }
        Date now = new Date();
        if (cancelType == 0) {
            now = null;
        } else {
            if (order.getStatus() == Order.DealStatusEnum.CLOSE.code) {
                result.setInfo(-1, "订单已取消");
                return result;
            }
        }
        int row = orderMapper.updateOrderCancelInfo(status, cancelType, now, orderId);
        if (row < 1) {
            LOG.error("更新订单取消订单信息失败");
            throw new ServiceRuntimeException("取消订单失败", true);
        }
        result.setInfo(1, "订单更新成功");
        return result;
    }

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
    @Override
    public Order getOrderById(long orderId) {
        return orderMapper.getOrderById(orderId);
    }

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
    @Override
    public List<Order> listOrderByTime(Date date) {
        return orderMapper.listOrderByTime(date);
    }

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
    @Transactional
    @Override
    public ResultInfo saveOrderInfo(long userId, long goodsId, int payType, boolean isPayFor, String mobile,
                                    String name) {
        ResultInfo resultInfo = new ResultInfo();

        if (userId < 1) {
            resultInfo.setInfo(-1, "用户id无效");
            return resultInfo;
        }
        if (goodsId < 1) {
            resultInfo.setInfo(-1, "商品id无效");
            return resultInfo;
        }

        try {
            Goods goods = goodsMapper.getGoodsById(goodsId);
            if (goods == null) {
                resultInfo.setInfo(-1, "商品id无效");
                return resultInfo;
            }

            int rows = -1;
            long payForId = 0;
            if (isPayFor) {
                OrderPayFor orderPayFor = new OrderPayFor(new Date(), name, mobile, null, userId, 0);
                rows = orderPayForMapper.saveOrderPayFor(orderPayFor);
                if (rows < 1) {
                    resultInfo.setInfo(-1, "保存代付信息失败");
                    LOG.info("新增订单时:", resultInfo.getCode(), resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
                payForId = orderPayFor.getId();
            }

            Order order = new Order();
            order.setTime(new Date());
            order.setUserId(userId);
            order.setOrderNo(GenerateCode.getOrderNo(BizzTypeEnum.PURCHASE_GOODS.getPrefix()));
            order.setAmount(goods.getPrice());
            order.setRealAmount(0.00);
            order.setPayTime(null);
            order.setStatus(DealStatusEnum.OBLIGATION.code);
            order.setPayType(payType);
            order.setCancelType(0);
            if (payForId > 0) {
                order.setPayFor(true);
                order.setPayForId(payForId);
                order.setRemark("代购买" + goods.getName());
            } else {
                order.setPayFor(false);
                order.setPayForId(payForId);
                order.setRemark("购买" + goods.getName());
            }

            rows = orderMapper.saveOrder(order);
            if (rows < 1) {
                resultInfo.setInfo(-1, "保存订单信息失败");
                LOG.info("新增订单时:", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }

            OrderGoods orderGoods = new OrderGoods(new Date(), goodsId, goods.getPrice(), 1, goods.getValidityPeriod(),
                    goods.getValidityPeriodUnit(), order.getId());
            rows = orderGoodsMapper.saveOrderGoods(orderGoods);
            if (rows < 1) {
                resultInfo.setInfo(-1, "保存订单详细信息失败");
                LOG.info("新增订单时:", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }
            resultInfo.setInfo(1, "保存订单成功", order);
        } catch (Exception e) {
            LOG.error("新增订单时出现异常--{}", e.getMessage());
            throw new ServiceRuntimeException("新增订单出现异常", true);
        }

        return resultInfo;
    }

    /**
     * 后台根据搜索条件获取平台所有订单列表
     *
     * @param orderSearchBO
     * @param request
     * @return com.qingqingmr.qqmr.common.PageResult<com.qingqingmr.qqmr.domain.bean.OrderVO>
     * @datetime 2018/7/14 10:19
     * @author crn
     */
    @Override
    public PageResult<OrderVO> listPlatformOrder(OrderSearchBO orderSearchBO, HttpServletRequest request) {

        Integer currPage = orderSearchBO.getCurrPage();
        Integer pageSize = orderSearchBO.getPageSize();
        currPage = null == currPage || currPage == 0 ? 1 : currPage;
        pageSize = null == pageSize || pageSize == 0 ? 10 : pageSize;
        PageResult<OrderVO> result = new PageResult<OrderVO>(currPage, pageSize);
        Page<OrderVO> page = PageHelper.startPage(currPage, pageSize);
        String type = orderSearchBO.getType();
        String typeStr = orderSearchBO.getTypeStr();

        //兼容搜索昵称是表情符号
        if ("2".equals(type) && StringUtils.isNotBlank(typeStr)) {
            orderSearchBO.setTypeStr(Security.encodeHexString(typeStr));
        }

        List<OrderVO> orderVOS = orderMapper.listPlatformOrder(orderSearchBO);

        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.ORDER_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        result.setPage(orderVOS);
        result.setTotalCount(page.getTotal());
        return result;
    }

    /**
     * 根据订单id获取订单详情信息
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public OrderDetailVO getOrderDetailByOrderId(Long id, HttpServletRequest request) {
        OrderDetailVO orderDetailByOrderId = null;
        try {
            orderDetailByOrderId = orderMapper.getOrderDetailByOrderId(id);
            if (null != orderDetailByOrderId && StringUtils.isNotBlank(orderDetailByOrderId.getPayForNickName())) {
                orderDetailByOrderId.setPayForNickName(Security.decodeHex(orderDetailByOrderId.getPayForNickName()));
            }
            // 添加管理员操作事件
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.ORDER_SHOW_DETAIL);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        return orderDetailByOrderId;
    }

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
    @Override
    @Transactional
    public ResultInfo payOrderSuccess(String orderNo, double realAmount, String transactionId, String remark) {
        ResultInfo resultInfo = new ResultInfo();

        Order order = orderMapper.getOrderByOrderNo(orderNo);
        if (order == null) {
            resultInfo.setInfo(-1, "查询订单为空");
            return resultInfo;
        }

        if (realAmount < 0.01) {
            resultInfo.setInfo(-1, "实际支付金额有误");
            return resultInfo;
        }

        if (DealStatusEnum.SUCCESS.code == order.getStatus()) {
            resultInfo.setInfo(ResultInfo.ALREADY_RUN, "订单已成功");
            return resultInfo;
        }

        // 查询用户信息
        User user = userService.getUserById(order.getUserId());

        // 1.判断是否已经自动取消
        if (DealStatusEnum.CLOSE.code == order.getStatus()) {
            if (order.getCancelType() == 1) {
                resultInfo = updateOrderCancelInfo(order.getId(), 0, 1);
                if (resultInfo.getCode() < 1) {
                    LOG.info("更新已经取消了的订单信息失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
            }
        }

        // 2.更新订单状态、付款时间、实际支付金额
        int rows = orderMapper.updateOrderPaySuccessInfo(new Date(), realAmount, order.getId());
        if (rows < 1) {
            resultInfo.setInfo(-1, "更新订单失败");
            LOG.info("更新订单的状态和支付时间信息失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }
        if (StringUtils.isNotBlank(transactionId)) {
            rows = updateTransactionId(transactionId, order.getId());
            if (rows < 1) {
                resultInfo.setInfo(-1, "更新订单失败");
                LOG.info("更新订单的第三方订单号信息失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }
        }

        // 3.添加交易记录
        DealUser dealUser = new DealUser(order.getUserId(), orderNo, BizzTypeEnum.REPAYMENT.getDealType(),
                BizzTypeEnum.REPAYMENT.getCode(), realAmount, remark);
        rows = dealUserService.saveDealUser(dealUser);
        if (rows < 1) {
            resultInfo.setInfo(-1, "添加交易记录失败");
            LOG.info("添加交易记录失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        // 4.判断是否代付
        String cardNo = membershipCardService.getMembershipCardCode(order.getUserId());
        long cardUserId = order.getUserId();
        if (order.getPayFor()) {
            OrderPayFor payFor = orderPayForService.getOrderPayForById(order.getPayForId());
            User forUser = userService.getUserByMobile(payFor.getMobile());
            cardUserId = forUser.getId();
            // 更新代付信息
            rows = orderPayForService.updateOrderPayForCardNo(payFor.getId(), cardNo);
            if (rows < 1) {
                resultInfo.setInfo(-1, "修改订单代付信息失败");
                LOG.info("修改订单代付信息失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }

            // 更改邀请关系
            UserInfo cardUserInfo = userInfoService.getUserInfoByUserId(cardUserId);
            if (cardUserInfo.getSpreadId() == null || cardUserInfo.getSpreadId() < 1 || cardUserInfo.getSpreadId() != order.getUserId()) {
                try {
                    resultInfo = userInfoService.updateInviterInfo(cardUserId, order.getUserId(), user.getRoleType());
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("更改邀请关系异常--{}", e.getMessage());
                    throw new ServiceRuntimeException(e.getMessage(), true);
                }
                if (resultInfo.getCode() < 1) {
                    LOG.info("更改邀请关系--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
            } else {
                LOG.info("被代付人已经是代付者邀请，无需修改邀请关系");
            }
        }

        // 5.保存会员卡信息
        rows = membershipCardService.saveMembershipCard(cardUserId, cardNo, order.getPayFor(), order.getId(), remark);
        if (rows < 1) {
            resultInfo.setInfo(-1, "保存会员卡信息失败");
            LOG.info("保存会员卡信息失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        // 6.更新用户是否是会员
        rows = userService.updateUserRoleTypeById(RoleTypeEnum.MEMBER, cardUserId);
        if (rows < 1) {
            resultInfo.setInfo(-1, "更新用户会员信息失败");
            LOG.info("更新用户会员信息失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        // 7.计算佣金
        try {
            resultInfo = userBonusService.rebate(cardUserId, 1);
        } catch (Exception e) {
            resultInfo.setInfo(-1, e.getMessage());
            LOG.error("发放返佣异常失败--【{}】--【{}】", resultInfo.getCode(), e.getMessage());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }
        if (resultInfo.getCode() < 1) {
            resultInfo.setInfo(-1, "发放返佣失败");
            LOG.info("发放返佣失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        resultInfo.setInfo(1, "处理成功");
        return resultInfo;
    }

    /**
     * 通过用户id查询用户未完成的订单记录
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    @Override
    public int countUserNotFinishOrderByUserId(Long userId) {
        return orderMapper.countUserNotFinishOrderByUserId(userId);
    }

    /**
     * 通过用户id被代付的未完成的订单记录
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    @Override
    public int countUserBePaidByUserId(Long userId) {
        return orderMapper.countUserBePaidByUserId(userId);
    }

    /**
     * 后台手动取消订单
     *
     * @param id         订单id
     * @param cancelType 取消类型
     * @param status
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultInfo cancelOrder(Long id, int cancelType, int status, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = updateOrderCancelInfo(id, cancelType, status);
        } catch (Exception e) {
            LOG.error("手动取消订单异常--【{}】", e.getMessage());
            resultInfo.setInfo(-1, "取消订单失败");
            return resultInfo;
        }

        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.ORDER_QUIT);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        return resultInfo;
    }

    /**
     * 自动取消订单
     *
     * @return
     */
    @Transactional
    @Override
    public ResultInfo autoCancelOrder() {
        ResultInfo resultInfo = new ResultInfo();

        List<Order> orders = listOrderByTime(new Date());
        if (orders == null || orders.size() < 1) {
            resultInfo.setInfo(1, "暂无订单数据处理");
            return resultInfo;
        }

        orders.forEach(x -> {
            ResultInfo info = new ResultInfo();
            try {
                info = updateOrderCancelInfo(x.getId(), 1, Order.DealStatusEnum.CLOSE.code);
            } catch (Exception e) {
                LOG.error("执行结果：code={}，msg={}", -1, e.getMessage());
                throw e;
            }
            if (info.getCode() < 1) {
                LOG.info("自动取消订单--【{}】--【{}】", info.getCode(), info.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }
        });

        resultInfo.setInfo(1, "执行成功");
        return resultInfo;
    }

    /**
     * 支付成功更新第三方订单号
     *
     * @param transactionId
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int updateTransactionId(String transactionId, long id) {
        return orderMapper.updateTransactionId(transactionId, id);
    }
}
