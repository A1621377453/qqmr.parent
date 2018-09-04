package com.qingqingmr.qqmr.wechat.handler;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.QRCodeUtil;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.domain.bean.VipPrivilegeDetailVo;
import com.qingqingmr.qqmr.domain.entity.MembershipCard;
import com.qingqingmr.qqmr.domain.entity.Order;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.service.GoodsService;
import com.qingqingmr.qqmr.service.MembershipCardService;
import com.qingqingmr.qqmr.service.OrderService;
import com.qingqingmr.qqmr.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * VIP特权
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午6:52:12
 */
@Component
public class VipPrivilegeHandler {
    private static final Logger LOG = LoggerFactory.getLogger(VipPrivilegeHandler.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private SpringContextProperties springContextProperties;
    @Autowired
    private MembershipCardService membershipCardService;

    /**
     * VIP特权详情页
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月10日 下午5:01:14
     */
    public String getVipPrivilegeDetail(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        if (parameters == null) {
            json.put("code", -1);
            json.put("msg", "输入参数有误!");
            return FastJsonUtil.toJsonString(json);
        }

        String signId = parameters.get("userId");
        if (signId == null) {
            json.put("code", -1);
            json.put("msg", "输入参数有误!");
            return FastJsonUtil.toJsonString(json);
        }
        ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
                springContextProperties.getAppDesKey());
        if (result.getCode() < 1) {
            json.put("code", ResultInfo.ERROR_100);
            json.put("msg", "用户id有误");
            return FastJsonUtil.toJsonString(json);
        }
        parameters.put("userId", result.getObj().toString());
        long userId = Long.parseLong(result.getObj().toString());
        int userType = userService.getUserById(userId).getRoleType();

        VipPrivilegeDetailVo vipPrivilegeDetailVo = goodsService.getVipPrivilegeDetail(parameters);
        if (vipPrivilegeDetailVo != null) {
            json.put("data", vipPrivilegeDetailVo);
        } else {
            json.put("userType", userType);
            json.put("code", -1);
            json.put("msg", "查询用户VIP详情失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("userType", userType);
        json.put("code", 1);
        json.put("msg", "查询成功");
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 新增订单信息
     *
     * @param parameters
     * @param request
     * @return
     * @author liujinjin
     * @datetime 2018年7月12日 上午11:05:14
     */
    public String addOrderInfo(Map<String, String> parameters, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        if (parameters == null) {
            json.put("code", -1);
            json.put("msg", "输入参数有误!");
            return FastJsonUtil.toJsonString(json);
        }

        String signId = parameters.get("userId");
        String mobile = parameters.get("mobile");
        String name = parameters.get("name");
        String goodsIdStr = parameters.get("goodsId");
        String payTypeStr = parameters.get("payType");

        ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
                springContextProperties.getAppDesKey());
        if (result.getCode() < 1) {
            json.put("code", ResultInfo.ERROR_100);
            json.put("msg", "用户id有误");
            return FastJsonUtil.toJsonString(json);
        }
        long userId = (long) result.getObj();

        boolean isPayFor = false;
        if (StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(name)) {
            if (!StrUtil.isMobileNum(mobile)) {
                json.put("code", -1);
                json.put("msg", "请输入正确的手机号格式");
                return FastJsonUtil.toJsonString(json);
            }
            if (StrUtil.isEmoji(name)) {
                json.put("code", -1);
                json.put("msg", "请输入正确的姓名");
                return FastJsonUtil.toJsonString(json);
            }
            // 判断代付的用户必须使用手机号在小程序里绑定
            User user = userService.getUserByMobile(mobile);
            if (user == null) {
                json.put("code", -1);
                json.put("msg", "代付手机号必须先绑定小程序!");
                return FastJsonUtil.toJsonString(json);
            }
            if (user.getRoleType() != RoleTypeEnum.CUSTOMER.code) {
                json.put("code", -1);
                json.put("msg", "该手机号不能进行代付!");
                return FastJsonUtil.toJsonString(json);
            }
            // 查询被代付人自己是否有未支付的订单
            int num = orderService.countUserNotFinishOrderByUserId(user.getId());
            if (num > 0) {
                json.put("code", -1);
                json.put("msg", "该代付手机号有未完成订单!");
                return FastJsonUtil.toJsonString(json);
            }
            // 查询被代付人是否存在被代付订单
            num = orderService.countUserBePaidByUserId(user.getId());
            if (num > 0) {
                json.put("code", -1);
                json.put("msg", "该代付手机号已经存在代付订单!");
                return FastJsonUtil.toJsonString(json);
            }
            isPayFor = true;
        }
        if (StringUtils.isBlank(goodsIdStr)) {
            json.put("code", -1);
            json.put("msg", "商品信息不能为空!");
            return FastJsonUtil.toJsonString(json);
        }
        long goodsId = Long.parseLong(goodsIdStr);
        // 目前只有一种，payType=1
        if (StringUtils.isBlank(payTypeStr)) {
            json.put("code", -1);
            json.put("msg", "请选择支付方式!");
            return FastJsonUtil.toJsonString(json);
        }
        int payType = Integer.parseInt(payTypeStr);

        ResultInfo resultInfo = new ResultInfo();
        if (!isPayFor) {
            User user = userService.getUserById(userId);
            if (user.getRoleType() != RoleTypeEnum.CUSTOMER.code) {
                json.put("code", -1);
                json.put("msg", user.getRoleType() == RoleTypeEnum.MEMBER.code ? "您已经是会员，请勿重复购买!"
                        : (user.getRoleType() == RoleTypeEnum.BEAUTICIAN.code ? "您是美容师，无须买卡!" : "您是顾问，无须买卡!"));
                return FastJsonUtil.toJsonString(json);
            }
        }
        // 查询用户是否有未完成的订单
        int num = orderService.countUserNotFinishOrderByUserId(userId);
        if (num > 0) {
            json.put("code", -1);
            json.put("msg", "请先完成已有的订单!");
            return FastJsonUtil.toJsonString(json);
        }
        if (!isPayFor) {
            // 查询用户是否存在被代付的订单
            num = orderService.countUserBePaidByUserId(userId);
            if (num > 0) {
                json.put("code", -1);
                json.put("msg", "你的手机号有未完成的被代付订单!");
                return FastJsonUtil.toJsonString(json);
            }
        }

        try {
            resultInfo = orderService.saveOrderInfo(userId, goodsId, payType, isPayFor, mobile, name);
        } catch (Exception e) {
            LOG.error("添加订单信息异常--{}", e.getMessage());
            json.put("code", -1);
            json.put("msg", "添加订单信息异常!");
            return FastJsonUtil.toJsonString(json);
        }
        if (resultInfo.getCode() == -1) {
            json.put("code", -1);
            json.put("msg", "添加订单信息失败!");
            return FastJsonUtil.toJsonString(json);
        }

        Order order = (Order) resultInfo.getObj();

        json.put("code", 1);
        json.put("msg", "添加订单成功");
        json.put("orderId", order.getId());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 生成会员卡二维码
     *
     * @param parameters
     * @param response
     * @return
     * @author liujinjin
     * @datetime 2018年7月12日 上午11:05:14
     */
    public String membershipCardQRCode(HttpServletResponse response, Map<String, String> parameters) throws Exception {
        JSONObject json = new JSONObject();
        String signId = parameters.get("userId");
        ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
                springContextProperties.getAppDesKey());
        if (result.getCode() < 1) {
            json.put("code", ResultInfo.ERROR_100);
            json.put("msg", "用户id有误");
            return FastJsonUtil.toJsonString(json);
        }
        long userId = Long.parseLong(result.getObj().toString());
        MembershipCard membershipCard = membershipCardService.getMembershipCard(userId);
        if (membershipCard == null) {
            json.put("code", -1);
            json.put("msg", "会员卡信息不存在");
            return FastJsonUtil.toJsonString(json);
        }
        Long id = membershipCard.getId();
        String cardNo = membershipCard.getCardNo();
        String url = "addService/addService";
        Map<String, String> params = new HashMap<>();
        params.put("id", id.toString());
        params.put("cardNo", cardNo);

        ByteArrayOutputStream outputStream = QRCodeUtil.generateQRCode(url, params);
        if (outputStream == null) {
            json.put("code", -1);
            json.put("msg", "二维码查看失败");
            ;
            return FastJsonUtil.toJsonString(json);
        }

        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();

        return "image";
    }

    /**
     * 顾问或美容师邀请会员时：校验用户手机号、姓名
     *
     * @param parameters
     * @param request
     * @return
     * @author liujinjin
     * @datetime 2018年7月12日 上午11:05:14
     */
    public String checkUserMobile(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        if (parameters == null) {
            json.put("code", -1);
            json.put("msg", "输入参数有误!");
            return FastJsonUtil.toJsonString(json);
        }

        String signId = parameters.get("userId");
        ResultInfo result = Security.decodeWXSign(signId, SystemConstant.ACTION_USER_ID_SIGN,
                springContextProperties.getAppDesKey());
        if (result.getCode() < 1) {
            json.put("code", ResultInfo.ERROR_100);
            json.put("msg", "用户id有误");
            return FastJsonUtil.toJsonString(json);
        }
        long userId = Long.parseLong(result.getObj().toString());
        String mobile = parameters.get("mobile");
        String name = parameters.get("name");
        if (StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(name)) {
            if (!StrUtil.isMobileNum(mobile)) {
                json.put("code", -1);
                json.put("msg", "请输入正确的手机号格式");
                return FastJsonUtil.toJsonString(json);
            }
            // 判断代付的用户必须使用手机号在小程序里绑定
            User user = userService.getUserByMobile(mobile);
            if (user == null) {
                json.put("code", -1);
                json.put("msg", "该手机号未绑定小程序，请先去绑定!");
                return FastJsonUtil.toJsonString(json);
            }
            long mobileUserId = user.getId();
            if (userId == mobileUserId) {
                json.put("code", -1);
                json.put("msg", "自己不能给自己代付!");
                return FastJsonUtil.toJsonString(json);
            }
            if (user.getRoleType() != RoleTypeEnum.CUSTOMER.code) {
                json.put("code", -1);
                json.put("msg", "该手机号不能进行代付!");
                return FastJsonUtil.toJsonString(json);
            }
        } else {
            json.put("code", -1);
            json.put("msg", "输入参数有问题!");
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", 1);
        json.put("msg", "校验成功");
        return FastJsonUtil.toJsonString(json);
    }
}
