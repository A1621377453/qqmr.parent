package com.qingqingmr.qqmr.wechat.gateway.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.gateway.Gateway;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.WechatConstant;
import com.qingqingmr.qqmr.wechat.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信网关实现
 *
 * @author ythu
 * @datetime 2018-07-11 20:05:04
 */
@Component
public class GatewayWechatImpl implements Gateway {
    private static final Logger LOG = LoggerFactory.getLogger(GatewayWechatImpl.class);

    @Autowired
    private MyAccountHandler myAccountHandler;
    @Autowired
    private BindHandler bindHandler;
    @Autowired
    private SettingHandler settingHandler;
    @Autowired
    private VipPrivilegeHandler vipPrivilegeHandler;
    @Autowired
    private InviteFriendHandler inviteFriendHandler;
    @Autowired
    private WxPaymentHandler wxPaymentHandler;

    /**
     * 处理请求
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    @Override
    public String processingRequest(HttpServletRequest request, HttpServletResponse response,
                                    Map<String, String> parameters) {
        String result = null;
        long timestamp = System.currentTimeMillis();
        LOG.info("客户端请求({}):【{}】", timestamp + "", JSON.toJSONString(parameters));
        switch (Integer.parseInt(parameters.get("OPT"))) {
            case WechatConstant.WECHAT_ENTER:
                try {
                    result = bindHandler.getBindWXUser(parameters);// 101
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("用户进入小程序时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_GET_STOREAREA:
                try {
                    result = bindHandler.getStoreArea(parameters);// 102
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("获取门店区域列表时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_GET_STORE:
                try {
                    result = bindHandler.getStoreBysAreaId(parameters);// 103
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("获取门店列表时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_BIND:
                try {
                    result = bindHandler.bindWXUser(parameters, request);// 104
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("用户进入绑定时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_VIP_PRIVILEGE:
                try {
                    result = vipPrivilegeHandler.getVipPrivilegeDetail(parameters);// 105
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("用户VIP特权页面信息查询时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ADD_ORDER:
                try {
                    result = vipPrivilegeHandler.addOrderInfo(parameters, request);// 106
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("支付后，添加订单时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ADD_WITHDRAWAL:
                try {
                    result = myAccountHandler.addWithdrawalUserInfo(parameters);// 107
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("添加提现记录时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ACCOUNT_SET:
                try {
                    result = settingHandler.getUserInfoById(parameters);// 201
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("用户进入账户设置获取用户信息时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_NICKNAME_UPDATE:
                try {
                    result = settingHandler.updateUserNickName(parameters);// 202
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("修改昵称时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_SEX_UPDATE:
                try {
                    result = settingHandler.updateUserSex(parameters);// 203
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("修改性别时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ADDRESS_SAVE:
                try {
                    result = settingHandler.addUserAddress(parameters);// 204
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("添加收货地址时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ADDRESS_UPDATE:
                try {
                    result = settingHandler.updateUserAddress(parameters);// 205
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("编辑收货地址时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_MOBILE_UPDATE:
                try {
                    result = settingHandler.updateUserMobile(parameters, request);// 206
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("修改手机号码时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_PAYPASSWORD_SAVE:
                try {
                    result = settingHandler.setPayPassword(parameters, request);// 207
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("设置交易密码时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_USER_ADDRESS:
                try {
                    result = settingHandler.listUserAddress(parameters);// 209
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("获取收货地址列表时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_TOTALASSETS_SELECT:
                try {
                    result = myAccountHandler.getTotalAssets(parameters);// 301
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询总资产：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_WITHDRAWAL_RECORD:
                try {
                    result = myAccountHandler.listWithdrawalUserPage(parameters);// 302
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询提现记录：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_INVITECODE_SELECT:
                try {
                    result = inviteFriendHandler.getInviteCode(parameters);// 303
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询邀请码：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_QRCODE:
                try {
                    result = inviteFriendHandler.inviteQRCode(response, parameters);// 304
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("我的邀请二维码：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_BONUS_RECORD:
                try {
                    result = inviteFriendHandler.listUserBonusPage(parameters);// 305
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询佣金明细：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_INVITEMEMBER_RECORD:
                try {
                    result = inviteFriendHandler.listInviteMemberPage(parameters);// 306
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询用户邀请会员列表：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_MYINVITE:
                try {
                    result = inviteFriendHandler.myInvite(parameters);// 307
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询邀请信息：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_WX_CODE_QRCODE:
                try {
                    result = inviteFriendHandler.wxCodeQRCode(response, parameters);// 308
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("微信二维码：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_DEAL_RECORD:
                try {
                    result = myAccountHandler.listDealUserPage(parameters);// 401
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询资金流水：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ORDER_RECORD:
                try {
                    result = myAccountHandler.listOrderPage(parameters);// 402
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询我的订单：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ORDER_DETAIL:
                try {
                    result = myAccountHandler.getOrderGoodsById(parameters);// 403
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询订单详情：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ORDER_CANCEL:
                try {
                    result = myAccountHandler.cancelOrder(parameters);// 404
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("取消订单：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_GET_AREA:
                try {
                    result = settingHandler.getDictAreaInfo(parameters);// 405
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("获取区域信息时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_GET_WITHDRAWAL:
                try {
                    result = myAccountHandler.getWithdrawalAmountInfo(parameters);// 406
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("获取提现页面金额信息时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_PAY_ORDER:
                try {
                    result = wxPaymentHandler.paymentOrder(parameters, request);// 407
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("支付订单时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_GET_NOTFINISH_ORDER:
                try {
                    result = myAccountHandler.countNotFinishOrder(parameters);// 408
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("查询当前用户是否有未完成的订单时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_ADD_SERVICE:
                try {
                    result = settingHandler.addServiceRecord(parameters);// 409
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("添加服务记录时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_MEMBERSHIPCARD_QRCODE:
                try {
                    result = vipPrivilegeHandler.membershipCardQRCode(response, parameters);// 410
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("生成会员卡二维码时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_SEND_CODE:
                try {
                    result = bindHandler.sendCode(parameters);// 411
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("发送验证码时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_VERIFYCODEE:
                try {
                    result = bindHandler.verifyCode(parameters, request);// 412
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("验证验证码时：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_CHECK_USERMOBILE:
                try {
                    result = vipPrivilegeHandler.checkUserMobile(parameters);// 413
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("添加订单时，验证用户手机号：{}", e.getMessage());
                    result = errorHandling();
                }
                break;
            case WechatConstant.WECHAT_DELETE_USERADDRESS:
                try {
                    result = settingHandler.deleteUserAddress(parameters);// 414
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("删除用户收货地址时：{}", e.getMessage());
                    result = errorHandling();
                }
        }

        LOG.info("服务器响应({}):【{}】", timestamp + "", result);
        return result;
    }

    /**
     * <p>
     * 程序异常 信息统一提示
     * </p>
     *
     * @return
     * @author liujingjing
     * @datetime 2018年7月13日 下午3:05:39
     */
    private String errorHandling() {
        JSONObject json = new JSONObject();
        json.put("code", ResultInfo.ERROR_500);
        json.put("msg", "系统繁忙，请稍后再试");
        return json.toString();
    }

}
