package com.qingqingmr.qqmr.wechat.handler;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.constant.SystemRedisKeyConstant;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.Encrypt;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.dao.EmployeeMapper;
import com.qingqingmr.qqmr.domain.bean.UserAddressDetailVO;
import com.qingqingmr.qqmr.domain.entity.Employee;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 个人设置
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午6:36:31
 */
@Component
public class SettingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SettingHandler.class);

    @Autowired
    private UserService userService;
    @Autowired
    private WxChatService wxChatService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private SpringContextProperties springContextProperties;
    @Autowired
    private DictAreaService dictAreaService;
    @Autowired
    private MembershipCardUseDetailService membershipCardUseDetailService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询账户设置-个人信息
     *
     * @param parameters
     * @return
     * @throws UnsupportedEncodingException
     * @author liujinjin
     * @datetime 2018年7月9日 下午6:10:29
     */
    public String getUserInfoById(Map<String, String> parameters) throws UnsupportedEncodingException {
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
        User user = userService.getUserById(userId);
        if (user == null) {
            json.put("code", -1);
            json.put("msg", "查询的用户不存在");
            return FastJsonUtil.toJsonString(json);
        } else {
            if (user.getRoleType() == RoleTypeEnum.BEAUTICIAN.code || user.getRoleType() == RoleTypeEnum.ADVISER.code) {
                Employee employee = employeeMapper.getEmployeeByUserId(userId);
                if (employee == null) {
                    json.put("code", -1);
                    json.put("msg", "查询的用户真实姓名失败");
                    return FastJsonUtil.toJsonString(json);
                }
                user.setNickName(employee.getRealityName());
            }
        }

        json.put("code", 1);
        json.put("msg", "查询用户信息成功");
        json.put("data", user);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 修改昵称
     *
     * @param parameters
     * @return
     * @throws UnsupportedEncodingException
     * @author liujinjin
     * @datetime 2018年7月9日 下午2:20:33
     */
    public String updateUserNickName(Map<String, String> parameters) throws UnsupportedEncodingException {
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
        String nickName = parameters.get("nickName");
        nickName = Security.encodeHexString(nickName);
        String openId = parameters.get("openId");
        try {
            int num = userService.updateUserNickName(userId, nickName);
            int rows = wxChatService.updateUserNickName(openId, nickName);
            if (num < 1 || rows < 1) {
                json.put("code", -1);
                json.put("msg", "修改昵称失败");
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("修改昵称时，出现异常--{}", e.getMessage());
            json.put("code", result.getCode());
            json.put("msg", "修改昵称失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", 1);
        json.put("msg", "修改昵称成功");
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 修改性别
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 下午3:17:57
     */
    public String updateUserSex(Map<String, String> parameters) {
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
        String sex = parameters.get("sex");
        String openId = parameters.get("openId");
        try {
            int num = userService.updateUserSex(userId, sex);
            int rows = wxChatService.updateUserSex(openId, sex);
            if (num < 1 || rows < 1) {
                json.put("code", -1);
                json.put("msg", "修改性别失败");
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("修改性别时，出现异常--{}", e.getMessage());
            json.put("code", result.getCode());
            json.put("msg", "修改性别失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", 1);
        json.put("msg", "修改性别成功");
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 查询个人收货地址列表
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 下午4:20:51
     */
    public String listUserAddress(Map<String, String> parameters) {
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
        List<UserAddressDetailVO> userAddressList = userAddressService
                .listUserAddressByUserId(result.getObj().toString());
        if (userAddressList.size() > 0) {
            json.put("code", 1);
            json.put("msg", "查询收货地址成功");
            json.put("data", userAddressList);
        } else {
            json.put("code", 1);
            json.put("msg", "用户收货地址为空");
        }
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加个人收货地址
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 下午4:20:51
     */
    public String addUserAddress(Map<String, String> parameters) {
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
        parameters.put("userId", result.getObj().toString());

        String detail = parameters.get("detail");
        if (StrUtil.isEmoji(detail)) {
            json.put("code", -1);
            json.put("msg", "地址详情不能包含特殊字符");
            return FastJsonUtil.toJsonString(json);
        }

        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = userAddressService.saveUserAddress(parameters);
            if (resultInfo.getCode() < 1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("添加用户收货地址时，出现异常--{}", e.getMessage());
            json.put("code", result.getCode());
            json.put("msg", "添加用户收货地址失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑个人收货地址
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 下午5:06:27
     */
    public String updateUserAddress(Map<String, String> parameters) {
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
        parameters.put("userId", result.getObj().toString());

        String detail = parameters.get("detail");
        if (StrUtil.isEmoji(detail)) {
            json.put("code", -1);
            json.put("msg", "地址详情不能包含特殊字符");
            return FastJsonUtil.toJsonString(json);
        }

        try {
            int num = userAddressService.updateUserAddress(parameters);
            if (num < 1) {
                json.put("code", -1);
                json.put("msg", "编辑收货地址失败");
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("编辑收货地址时，出现异常--{}", e.getMessage());
            json.put("code", result.getCode());
            json.put("msg", "编辑收货地址失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", 1);
        json.put("msg", "编辑收货地址成功");
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 删除个人收货地址
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 下午5:06:27
     */
    public String deleteUserAddress(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        if (parameters == null) {
            json.put("code", -1);
            json.put("msg", "输入参数有误!");
            return FastJsonUtil.toJsonString(json);
        }

        long id = Long.parseLong(parameters.get("id"));
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = userAddressService.deleteUserAddress(id);
            if (resultInfo.getCode() < 1) {
                json.put("code", -1);
                json.put("msg", "删除收货地址失败");
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("删除收货地址时，出现异常--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", "删除收货地址失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 更换个人手机号
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 下午5:55:21
     */
    public String updateUserMobile(Map<String, String> parameters, HttpServletRequest request) {
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

        if (!StrUtil.isMobileNum(mobile)) {
            json.put("code", -1);
            json.put("msg", "手机号码输入有误!");
            return FastJsonUtil.toJsonString(json);
        }

        String smsCodeSign = parameters.get("smsCodeSign");
        String scene = parameters.get("scene");
        String smsCode = Encrypt.decryptDES(smsCodeSign, springContextProperties.getAppDesKey());
        if (StringUtils.isBlank(smsCode)) {
            json.put("code", -1);
            json.put("msg", "短信验证码签名无效");
            return FastJsonUtil.toJsonString(json);
        }
        String key = springContextProperties.getProfile() + SystemRedisKeyConstant.MOBILE + mobile + "." + scene;
        Object cache = redisTemplate.opsForValue().get(key);
        if (cache == null) {
            json.put("code", -1);
            json.put("msg", "短信验证码已失效");
            return FastJsonUtil.toJsonString(json);
        }
        if (!cache.toString().equals(smsCode)) {
            json.put("code", -1);
            json.put("msg", "验证码输入有误");
            return FastJsonUtil.toJsonString(json);
        }
        redisTemplate.delete(key);
        key = springContextProperties.getProfile() + "." + mobile + "." + request.getSession().getId() + "." + scene;
        redisTemplate.delete(key);
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = userService.updateUserMobile(userId, mobile);
            if (resultInfo.getCode() < 1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("更换手机号时，出现异常--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", "更换手机号失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 设置交易密码、忘记密码、修改交易密码通过短信验证后重置交易密码
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 下午6:37:51
     */
    public String setPayPassword(Map<String, String> parameters, HttpServletRequest request) {
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

        String smsCodeSign = parameters.get("smsCodeSign");
        String scene = parameters.get("scene");
        String mobile = parameters.get("mobile");

        if (StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(scene) && StringUtils.isNotBlank(smsCodeSign)) {
            String smsCode = Encrypt.decryptDES(smsCodeSign, springContextProperties.getAppDesKey());
            if (StringUtils.isBlank(smsCode)) {
                json.put("code", -1);
                json.put("msg", "短信验证码签名无效");
                return FastJsonUtil.toJsonString(json);
            }
            String key = springContextProperties.getProfile() + SystemRedisKeyConstant.MOBILE + mobile + "." + scene;
            Object cache = redisTemplate.opsForValue().get(key);
            if (cache == null) {
                json.put("code", -1);
                json.put("msg", "短信验证码已失效");
                return FastJsonUtil.toJsonString(json);
            }
            if (!cache.toString().equals(smsCode)) {
                json.put("code", -1);
                json.put("msg", "验证码输入有误");
                return FastJsonUtil.toJsonString(json);
            }
            redisTemplate.delete(key);
            key = springContextProperties.getProfile() + "." + mobile + "." + request.getSession().getId() + "." + scene;
            redisTemplate.delete(key);
        }

        long userId = Long.parseLong(result.getObj().toString());
        String payPassword = parameters.get("payPassword");
        String oldPayPassword = parameters.get("oldPayPassword");
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = userService.updatePsyPasswordById(userId, payPassword, oldPayPassword);
            if (resultInfo.getCode() < 1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("设置交易密码时，出现异常--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", "设置交易密码失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加收货地址时查询所有的省份编码、省份名称
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月14日 下午1:50:40
     */
    public String getDictAreaInfo(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        if (parameters == null) {
            json.put("code", -1);
            json.put("msg", "输入参数有误!");
            return FastJsonUtil.toJsonString(json);
        }
        if (StringUtils.isBlank(parameters.get("parentId"))) {
            parameters.put("parentId", "100000");
        }
        List<Map<String, Object>> list = dictAreaService.getDictAreaInfo(parameters);
        if (list.size() > 0) {
            json.put("code", 1);
            json.put("msg", "查询成功");
            json.put("data", list);
        } else {
            json.put("code", 1);
            json.put("msg", "未查出对应的区域信息");
        }
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加服务记录
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月17日 下午4:08:40
     */
    public String addServiceRecord(Map<String, String> parameters) {
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
        String content = parameters.get("content");
        String remark = parameters.get("remark");
        Long cardId = Long.parseLong(parameters.get("cardId"));

        if (StrUtil.isEmoji(content)) {
            json.put("code", -1);
            json.put("msg", "服务内容包含特殊字符");
            return FastJsonUtil.toJsonString(json);
        }
        if (StrUtil.isEmoji(remark)) {
            json.put("code", -1);
            json.put("msg", "备注包含特殊字符");
            return FastJsonUtil.toJsonString(json);
        }

        ResultInfo resultInfo = null;
        try {
            resultInfo = membershipCardUseDetailService.savaMembershipCardUseDetail(userId, content, remark, cardId);
            if (resultInfo.getCode() < 1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("添加服务记录--{}", e.getMessage());
            json.put("code", result.getCode());
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }
}