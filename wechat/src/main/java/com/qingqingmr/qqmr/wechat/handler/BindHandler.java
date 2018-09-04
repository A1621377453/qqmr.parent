package com.qingqingmr.qqmr.wechat.handler;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.sms.SmsSceneEnum;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.constant.SystemRedisKeyConstant;
import com.qingqingmr.qqmr.common.enums.MessageTypeEnum;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.Encrypt;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.dao.UserInfoMapper;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.*;
import com.qingqingmr.qqmr.wechat.common.WechatProperties;
import com.qingqingmr.qqmr.wechat.common.util.WechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 绑定
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午6:35:11
 */
@Component
public class BindHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(BindHandler.class);
    @Autowired
    private StoreAreaService storeAreaService;
    @Autowired
    private StoreService StoreService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFundsService userFundsService;
    @Autowired
    private WxChatService wxChatService;
    @Autowired
    private SpringContextProperties springContextProperties;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TemplateNoticeService templateNoticeService;
    @Autowired
    private WechatProperties wechatProperties;
    @Autowired
    private UserInfoMapper userInfoMapper;


    /**
     * 微信用户进入小程序
     *
     * @param parameters
     * @return
     * @author liujinjin
     * @datetime 2018年7月9日 上午10:52:37
     */
    public String getBindWXUser(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        String openCode = parameters.get("openCode");
        String openId = parameters.get("openId");
        if (StringUtils.isBlank(openId)) {
            // 调用微信后台接口获取openId
            openId = WechatUtil.getOpenId(wechatProperties.getJsCodeSessionUrl(), openCode, springContextProperties.getMchAppId(), springContextProperties.getAppSecret(), wechatProperties.getGrantType());
            if (StringUtils.isBlank(openId)) {
                json.put("code", -1);
                json.put("msg", "微信授权失败");
                return FastJsonUtil.toJsonString(json);
            }
        }
        User user = userService.getUserByWxOpenId(openId);
        JSONObject data = new JSONObject();
        if (user == null) {
            if (openId == null) {
                json.put("code", -1);
                json.put("msg", "查询微信用户openid失败");
            } else {
                data.put("bindState", "未绑定");
                data.put("openId", openId);
                json.put("code", 1);
                json.put("msg", "查询成功");
                json.put("data", data);
            }
        } else {
            String storeName = StoreService.getStoreNameByUserId(user.getId());
            ResultInfo resultInfo = userFundsService.getTotalAssets(user.getId());
            Object obj = resultInfo.getObj();
            JSONObject job = JSONObject.parseObject(FastJsonUtil.toJsonString(obj));
            String sumAssets = job.getString("totalAmount");
            data.put("bindState", "已绑定");
            data.put("storeName", storeName);
            if (StringUtils.isBlank(sumAssets)) {
                data.put("sumAssets", "0.00");
            } else {
                data.put("sumAssets", sumAssets);
            }
            Long id = user.getId();
            String uId = Security.addWXSign(id, SystemConstant.ACTION_USER_ID_SIGN,
                    springContextProperties.getAppDesKey());
            data.put("userId", uId);
            data.put("openId", user.getWxOpenId());
            data.put("roleType", user.getRoleType());
            if (StringUtils.isNotBlank(user.getPayPassword())) {
                data.put("payPassword", 0);
            } else {
                data.put("payPassword", 1);
            }
            data.put("mobile", user.getMobile());
            json.put("code", 1);
            json.put("msg", "查询成功");
            json.put("data", data);
        }
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 小程序绑定页面，门店区域查询
     *
     * @param
     * @return List<StoreArea> 门店区域列表
     * @author liujinjin
     * @datetime 2018年7月5日 上午11:15:51
     */
    public String getStoreArea(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        List<StoreArea> storeAreaList = storeAreaService.listStoreAreas();
        json.put("code", 1);
        json.put("msg", "查询成功！");
        json.put("data", storeAreaList);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 小程序绑定页面，根据区域id查询关联的所有门店
     *
     * @param parameters
     * @return list<Store> 门店区域关联下的所有门店列表
     * @author liujinjin
     * @datetime 2018年7月5日 上午11:17:52
     */
    public String getStoreBysAreaId(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        String areaId = parameters.get("areaId");
        if (StringUtils.isBlank(areaId)) {
            json.put("code", -1);
            json.put("msg", "输入参数为空!");
            return FastJsonUtil.toJsonString(json);
        }

        List<Store> storeList = StoreService.listStores(Long.valueOf(areaId));
        json.put("code", 1);
        json.put("msg", "查询成功！");
        json.put("data", storeList);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 绑定微信用户
     *
     * @param parameters
     * @param request
     * @return
     * @author liujinjin
     * @datetime 2018年7月5日 下午1:52:37
     */
    public String bindWXUser(Map<String, String> parameters, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String mobile = parameters.get("mobile");
        String smsCodeSign = parameters.get("smsCodeSign");
        String scene = parameters.get("scene");
        String inviterCode = parameters.get("inviterCode");

        if (!StrUtil.isMobileNum(mobile)) {
            json.put("code", -1);
            json.put("msg", "请输入正确的手机号格式");
            return FastJsonUtil.toJsonString(json);
        }

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

        if (StringUtils.isNotBlank(inviterCode)) {
            if (!StrUtil.isMobileNum(inviterCode)) {
                json.put("code", -1);
                json.put("msg", "请输入11位数字邀请码");
                return FastJsonUtil.toJsonString(json);
            } 
            User user = userService.getUserByMobile(parameters.get("inviterCode"));
			if (user != null) {
				int role = user.getRoleType();
				if (role == RoleTypeEnum.CUSTOMER.code) {
					json.put("code", -1);
					json.put("msg", "该邀请码不存在");
					return FastJsonUtil.toJsonString(json);
				}
			}
			if (user == null) {
				json.put("code", -1);
				json.put("msg", "该邀请码不存在");
				return FastJsonUtil.toJsonString(json);
			}
			UserInfo userInfos = userInfoMapper.getUserInfoByMobile(parameters.get("inviterCode"));
			if (userInfos == null) {
				json.put("code", -1);
				json.put("msg", "该邀请码不存在");
				return FastJsonUtil.toJsonString(json);
			}
            Long storeId = userInfos.getStoreId();

            if (storeId != Long.parseLong(parameters.get("storeId"))) {
                json.put("code", -1);
                json.put("msg", "邀请码与所选美容店不匹配");
                return FastJsonUtil.toJsonString(json);
            }
        }

        try {
			String nickName = Security.encodeHexString(parameters.get("nickName"));
			parameters.put("nickName", nickName);
			JSONObject parseObj = FastJsonUtil.parseObj(parameters.get("wxInfo"), JSONObject.class);
			parseObj.put("nickName", nickName);
			parameters.put("wxInfo", FastJsonUtil.toJsonString(parseObj));
			User user = userService.getUserByMobile(mobile);
            if (user != null) {
                if (StringUtils.isNotBlank(user.getWxOpenId())) {
                    json.put("code", -1);
                    json.put("msg", "该手机号已使用，请重新输入");
                    return FastJsonUtil.toJsonString(json);
                }

                // 如果是后台录入的美容师、顾问，绑定的时候更新用户表里的信息，添加微信绑定表里的记录
                ResultInfo resultInfo = userService.saveBindWXUser(parameters);
                if (resultInfo.getCode() == -1) {
                    json.put("code", resultInfo.getCode());
                    json.put("msg", resultInfo.getMsg());
                    return FastJsonUtil.toJsonString(json);
                }
                resultInfo = wxChatService.saveBindWXChat(parameters);
                if (resultInfo.getCode() == -1) {
                    json.put("code", -1);
                    json.put("msg", "绑定失败!");
                    return FastJsonUtil.toJsonString(json);
                }

                json.put("code", 1);
                json.put("msg", "绑定成功");
                return FastJsonUtil.toJsonString(json);
            }

            // 添加用户表记录
            ResultInfo resultInfo = userService.saveBindWXUser(parameters);
            if (resultInfo.getCode() == -1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
        	LOG.error("用户绑定时出现异常--{}", e.getMessage());
            json.put("code", -1);
            json.put("msg", "绑定时出现异常");
            return FastJsonUtil.toJsonString(json);
        }

        // 清除缓存
        redisTemplate.delete(key);
        key = springContextProperties.getProfile() + "." + mobile + "." + scene;
        redisTemplate.delete(key);

        json.put("code", 1);
        json.put("msg", "绑定手机号成功");
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * <p>
     * 发送验证码
     * </p>
     *
     * @param parameters
     * @return
     * @author liujingjing
     * @datetime 2018年7月17日 下午7:17:39
     */
    public String sendCode(Map<String, String> parameters) {
        JSONObject json = new JSONObject();
        String mobile = parameters.get("mobile");
        String scene = parameters.get("scene");// 场景
        String smsType = parameters.get("smsType");// 类型（文本 1 or语音)

        /* 验证手机号是否符合规范 */
        if (!StrUtil.isMobileNum(mobile)) {
            json.put("code", -1);
            json.put("msg", "请输入正确的手机号格式");
            return FastJsonUtil.toJsonString(json);
        }

        if ((String.valueOf(SmsSceneEnum.BIND.code)).equals(scene)) {
        	User user = userService.getUserByMobile(mobile);
            if (user != null) {
            	if(user.getRoleType() == RoleTypeEnum.CUSTOMER.code || user.getRoleType() == RoleTypeEnum.MEMBER.code) {
            		json.put("code", -1);
            		json.put("msg", "该手机号已使用，请重新输入");
            		return FastJsonUtil.toJsonString(json);
            	}
            }
        } else if ((String.valueOf(SmsSceneEnum.FORGET_PWD.code)).equals(scene)) {
            if (userService.getUserByMobile(mobile) == null) {
                json.put("code", -1);
                json.put("msg", "手机号未绑定");
                return FastJsonUtil.toJsonString(json);
            }
        } else if ((String.valueOf(SmsSceneEnum.UPDATE_MOB.code)).equals(scene)) {
            if (userService.getUserByMobile(mobile) != null) {
                json.put("code", -1);
                json.put("msg", "该手机号已使用，请重新输入");
                return FastJsonUtil.toJsonString(json);
            }
        }  else {
            json.put("code", -1);
            json.put("msg", "场景类型不符合规范");
            return FastJsonUtil.toJsonString(json);
        }

        /* 查询短信验证码模板 */
        TemplateNotice notice = new TemplateNotice();
        if ("2".equals(scene)) {// 绑定小程序
            notice = templateNoticeService.getBySceneType(SmsSceneEnum.BIND.code, MessageTypeEnum.SMS.getCode());
        } else if ("3".equals(scene)) {// 忘记交易密码
            notice = templateNoticeService.getBySceneType(SmsSceneEnum.FORGET_PWD.code, MessageTypeEnum.SMS.getCode());
        } else if ("4".equals(scene)) {// 更改手机号
            notice = templateNoticeService.getBySceneType(SmsSceneEnum.UPDATE_MOB.code, MessageTypeEnum.SMS.getCode());
        }
        if (null == notice) {
            json.put("code", -1);
            json.put("msg", "短信发送失败!");
        }
        String content = notice.getContent();

        smsType = StringUtils.isBlank(smsType) ? "1" : smsType;

        String key = springContextProperties.getProfile() + "." + mobile + "." + scene;
        Object cache = redisTemplate.opsForValue().get(key);
        if (null == cache) {
            redisTemplate.opsForValue().set(key, mobile, SystemConstant.CACHE_TIME_SECOND_60, TimeUnit.SECONDS);
        } else {
            json.put("code", -1);
            json.put("msg", "短信已发送");
            return FastJsonUtil.toJsonString(json);
        }

        ResultInfo resultInfo = userInfoService.sendCode(mobile, scene, Integer.parseInt(smsType), content);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());

        return FastJsonUtil.toJsonString(json);
    }

    /**
     * <p>
     * 验证验证码
     * </p>
     *
     * @param parameters
     * @param request
     * @return
     * @author liujingjing
     * @datetime 2018年7月18日 上午11:30:20
     */
    public String verifyCode(Map<String, String> parameters, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        String verificationCode = parameters.get("verificationCode");
        String mobile = parameters.get("mobile");
        String scene = parameters.get("scene");

        if (StringUtils.isBlank(verificationCode)) {
            json.put("code", -1);
            json.put("msg", "请输入验证码");
            return FastJsonUtil.toJsonString(json);
        }
        if (StringUtils.isBlank(mobile)) {
            json.put("code", -1);
            json.put("msg", "请输入手机号");
            return FastJsonUtil.toJsonString(json);
        }

		if (!(String.valueOf(SmsSceneEnum.BIND.code)).equals(scene)
				&& !(String.valueOf(SmsSceneEnum.FORGET_PWD.code)).equals(scene)
				&& !(String.valueOf(SmsSceneEnum.UPDATE_MOB.code).equals(scene))) {
			json.put("code", -1);
			json.put("msg", "参数scene错误!");
			return FastJsonUtil.toJsonString(json);
		}
		verificationCode = Encrypt.decryptDES(verificationCode, springContextProperties.getAppDesKey());// 解密验证码
		if (StringUtils.isBlank(verificationCode)) {
			json.put("code", -1);
			json.put("msg", "短信验证码签名无效");
			return FastJsonUtil.toJsonString(json);
		}
		ResultInfo result = userService.verifyCode(verificationCode, mobile, scene);
		if (result.getCode() < 1) {
			json.put("code", result.getCode());
			json.put("msg", result.getMsg());
			return FastJsonUtil.toJsonString(json);
		}
		json.put("code", result.getCode());
		json.put("msg", result.getMsg());
		json.put("scene", scene);
		json.put("smsCodeSign", Encrypt.encryptDES(verificationCode, springContextProperties.getAppDesKey()));
		return FastJsonUtil.toJsonString(json);
	}

}
