package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.bo.UserSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.domain.bean.UserVO;
import com.qingqingmr.qqmr.domain.entity.MembershipCardUseDetail;
import com.qingqingmr.qqmr.service.MembershipCardUseDetailService;
import com.qingqingmr.qqmr.service.UserInfoService;
import com.qingqingmr.qqmr.service.UserService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户管理控制器
 *
 * @author crn
 * @datetime 2018-07-09 15:03:09
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping(value = "/back/user")
public class UserManageController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeManageController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MembershipCardUseDetailService cardUseDetailService;

    /**
     * 会员列表
     *
     * @param userSearchBo
     * @return java.lang.String
     * @datetime 2018/7/13 11:27
     * @author crn
     */
    @RequestMapping(value = "/listmember", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listMember(UserSearchBO userSearchBo, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == userSearchBo) {
            userSearchBo = new UserSearchBO();
        }
        userSearchBo.setRoleType(RoleTypeEnum.MEMBER.code);
        PageResult<UserVO> listUser = userService.listUser(userSearchBo, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", listUser);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取会员详情
     *
     * @param type   1:顾客  2:会员
     * @param userId 用户id
     * @return java.lang.String
     * @method getUserDetail
     * @datetime 2018/7/10 17:28
     * @author crn
     */
    @RequestMapping(value = "/getmemberdetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getMemberDetail(Integer type, Long userId, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == userId || null == type || 0 == userId || 0 == type || RoleTypeEnum.MEMBER.code != type) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        ResultInfo resultInfo = null;
        try {
            resultInfo = userService.getUserDetail(type, userId, request);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("获取会员详情异常", e.getMessage());
            json.put("code", -1);
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取用户当月佣金列表信息
     *
     * @param userId
     * @param currPage
     * @param pageSize
     * @return java.lang.String
     * @method listUserCurMonBonus
     * @datetime 2018/7/10 17:47
     * @author crn
     */
    @RequestMapping(value = "/listusercurmonbonus", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listUserCurMonBonus(Long userId, Integer currPage, Integer pageSize) {
        JSONObject json = new JSONObject();

        if (null == userId || 0 == userId) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        PageResult<Map<String, Object>> userCurMonBonus = userService.listUserCurMonBonus(userId, currPage, pageSize);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", userCurMonBonus);
        return FastJsonUtil.toJsonString(json);

    }

    /**
     * 获取会员卡服务记录
     *
     * @param cardId
     * @return java.lang.String
     * @method listCardUse
     * @datetime 2018/7/10 17:39
     * @author crn
     */
    @RequestMapping(value = "/listcarduse", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listCardUse(Long cardId, Integer currPage, Integer pageSize) {
        JSONObject json = new JSONObject();

        if (null == cardId || 0 == cardId) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        PageResult<MembershipCardUseDetail> listCardUse = cardUseDetailService.listCardUse(cardId, currPage, pageSize);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", listCardUse);
        return FastJsonUtil.toJsonString(json);

    }

    /**
     * 根据门店id获取门店用户
     *
     * @param type    2:会员  3:美容师  4:顾问
     * @param storeId
     * @param key     姓名或手机号
     * @return java.lang.String
     * @datetime 2018/7/11 11:40
     * @author crn
     */
    @RequestMapping(value = "/listuserbystoreid", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listUserByStoreId(Integer type, Long storeId, String key) {
        JSONObject json = new JSONObject();

        if (null == type || null == storeId || 0 == type || 0 == storeId) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        ResultInfo resultInfo = userService.listUserByStoreId(type, storeId, key);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 修改会员邀请人信息
     *
     * @param inviterUserId 邀请人id
     * @param inviteeUserId 被邀请人id
     * @param type          邀请人类型 2:会员  3:美容师  4:顾问
     * @return java.lang.String
     * @datetime 2018/7/11 13:32
     * @author crn
     */
    @RequestMapping(value = "/updatememberinviter", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String updateMemberInviter(Long inviteeUserId, Long inviterUserId, Integer type, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        JSONObject json = new JSONObject();

        if (null == inviterUserId || null == inviteeUserId || 0 == inviterUserId || 0 == inviteeUserId) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        try {
            resultInfo = userInfoService.updateInviter(inviteeUserId, inviterUserId, type, request);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("修改会员邀请人信息异常--{}", e.getMessage());
            json.put("code", -1);
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 修改顾客邀请人信息
     *
     * @param inviterUserId 邀请人id
     * @param inviteeUserId 被邀请人id
     * @param type          邀请人类型 2:会员  3:美容师  4:顾问
     * @return java.lang.String
     * @datetime 2018/7/11 13:32
     * @author crn
     */
    @RequestMapping(value = "/updatecustomerinviter", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String updateCustomerInviter(Long inviteeUserId, Long inviterUserId, Integer type, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        JSONObject json = new JSONObject();

        if (null == inviterUserId || null == inviteeUserId || 0 == inviterUserId || 0 == inviteeUserId) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        try {
            resultInfo = userInfoService.updateInviter(inviteeUserId, inviterUserId, type, request);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("修改顾客邀请人信息异常--{}", e.getMessage());
            json.put("code", -1);
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 根据userId获取直接邀请人信息
     *
     * @param userId
     * @return java.lang.String
     * @datetime 2018/7/11 15:42
     * @author crn
     */
    @RequestMapping(value = "/getinviterinfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getInviterInfo(Long userId) {
        JSONObject json = new JSONObject();

        if (null == userId || 0 == userId) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        ResultInfo resultInfo = userInfoService.getInviterInfo(userId);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }


    /**
     * 顾客列表
     *
     * @param userSearchBo
     * @return java.lang.String
     * @datetime 2018/7/11 13:50
     * @author crn
     */
    @RequestMapping(value = "/listcustomer", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listCustomer(UserSearchBO userSearchBo, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == userSearchBo) {
            userSearchBo = new UserSearchBO();
        }
        userSearchBo.setRoleType(RoleTypeEnum.CUSTOMER.code);
        PageResult<UserVO> listUser = userService.listUser(userSearchBo, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", listUser);
        return FastJsonUtil.toJsonString(json);

    }

    /**
     * 获取顾客详情
     *
     * @param type   1:顾客  2:会员
     * @param userId
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @datetime 2018/7/11 14:18
     * @author crn
     */
    @RequestMapping(value = "/getcustomerdetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getCustomerDetail(Integer type, Long userId, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == userId || null == type || 0 == userId || 0 == type || RoleTypeEnum.CUSTOMER.code != type) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        ResultInfo resultInfo = null;
        try {
            resultInfo = userService.getUserDetail(type, userId, request);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("获取顾客详情异常", e.getMessage());
            json.put("code", -1);
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }
}
