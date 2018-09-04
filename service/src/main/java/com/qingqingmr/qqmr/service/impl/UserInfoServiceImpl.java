package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.sms.SMSUtil;
import com.qingqingmr.qqmr.base.sms.SmsSceneEnum;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SettingKeyConstant;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.enums.MessageTypeEnum;
import com.qingqingmr.qqmr.common.enums.ProfileEnum;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.DateUtil;
import com.qingqingmr.qqmr.dao.EmployeeMapper;
import com.qingqingmr.qqmr.dao.TemplateNoticeMapper;
import com.qingqingmr.qqmr.dao.UserInfoMapper;
import com.qingqingmr.qqmr.dao.UserMapper;
import com.qingqingmr.qqmr.domain.bean.EmployeeVO;
import com.qingqingmr.qqmr.domain.bean.InviteMemberVO;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.domain.entity.TemplateNotice;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.domain.entity.UserInfo;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.SettingService;
import com.qingqingmr.qqmr.service.UserBonusService;
import com.qingqingmr.qqmr.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户信息
 *
 * @author liujinjin
 * @datetime 2018年7月5日 下午5:23:40
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SettingService settingService;
    @Autowired
    private SpringContextProperties springContextProperties;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private TemplateNoticeMapper templateNoticeMapper;
    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private UserBonusService userBonusService;

    /**
     * 微信小程序：添加微信用户的用户表记录
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int saveBindWXUserInfo(Map<String, String> parameters) {
        UserInfo userInfo = new UserInfo();
        try {
            User user = userMapper.getUserByMobile(parameters.get("mobile"));
            Long userId = user.getId();

            userInfo.setTime(new Date());//添加时间
            userInfo.setUserId(userId);//用户id
            userInfo.setStoreId(Long.parseLong(parameters.get("storeId")));//店面id
            userInfo.setMobile(parameters.get("mobile"));//手机号
            userInfo.setStoreId(Long.valueOf(parameters.get("storeId")));//店面id
            //邀请编码为空，邀请人id默认-1
            if ("".equals(parameters.get("inviterCode"))) {
                userInfo.setSpreadId(Long.valueOf("-1"));
            } else {
                User inviterUser = userMapper.getUserByMobile(parameters.get("inviterCode"));
                if (inviterUser != null) {
                    userInfo.setSpreadId(inviterUser.getId());//邀请人id
                    userInfo.setSpreadTime(new Date());
                }
            }
            userInfo.setBeauticianId(Long.valueOf("0"));//所属美容师id
            userInfo.setAdviserId(Long.valueOf("0"));//所属顾问id
            userInfoMapper.saveUserInfo(userInfo);
        } catch (Exception e) {
            LOG.error("新增用户时，出现异常--{}", e.getMessage());
            throw new ServiceRuntimeException("添加异常", true);
        }

        if (userInfo.getId() < 0) {
            return -1;
        }

        return 1;
    }

    /**
     * 更新用户邀请人信息
     *
     * @param inviteeUserId 被邀请人id
     * @param inviterUserId 邀请人id
     * @param type          指定用户角色2:会员  3:美容师  4:顾问
     * @param request
     * @return
     */
    @Transactional
    @Override
    public ResultInfo updateInviter(Long inviteeUserId, Long inviterUserId, Integer type, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();

        /* 修改被邀请人信息 */
        try {
            resultInfo = updateInviterInfo(inviteeUserId, inviterUserId, type);
        } catch (Exception e) {
            LOG.error("修改被邀请人信息失败--{}", e.getMessage());
            throw new ServiceRuntimeException("修改被邀请人信息", true);
        }
        if (resultInfo.getCode() < 1) {
            LOG.info("修改被邀请人信息--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException("修改被邀请人信息", true);
        }

        /* 添加邀请奖励 */
        try {
            resultInfo = userBonusService.rebate(inviteeUserId, 2);
        } catch (Exception e) {
            LOG.error("添加邀请奖励失败--{}", e.getMessage());
            throw new ServiceRuntimeException("添加邀请奖励失败", true);
        }
        if (resultInfo.getCode() < 1) {
            LOG.info("添加邀请奖励失败--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException("添加邀请奖励失败", true);
        }

        /* 查询短信验证码模板 */
        TemplateNotice notice = templateNoticeMapper.getBySceneType(SmsSceneEnum.NOTICE.code, MessageTypeEnum.SMS.getCode());
        if (null == notice) {
            resultInfo.setInfo(-1, "短信发送失败");
            LOG.info("查询短信验证码模板--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException("查询短信验证码模板", true);
        }
        String name = "";
        String mobile = "";
        if (RoleTypeEnum.MEMBER.code == type) {
            User user = userMapper.getUserById(inviterUserId);
            name = user.getNickName();
            mobile = user.getMobile();
        } else {
            EmployeeVO employee = employeeMapper.getEmployeeByUserId(inviterUserId);
            name = employee.getRealityName();
            mobile = employee.getMobile();
        }

        UserInfo userInfo = userInfoMapper.getUserInfoByUserId(inviteeUserId);
        User inviteeUser = userMapper.getUserById(inviteeUserId);
        String content = notice.getContent().replace("name", name).replace("date", DateUtil.dateToString(new Date(), SystemConstant.DATE_TIME_FORMAT)).replace("nickName", inviteeUser.getNickName()).replace("mobile", userInfo.getMobile());

        // 发送短信
        String finalMobile = mobile;
        new Thread(() -> sendNotice(content, finalMobile)).start();

        if (request != null) {
            try {
                eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.INVITE_EDIT);
            } catch (Exception e) {
                LOG.error("添加管理员操作日志异常--{}", e.getMessage());
            }
        }

        resultInfo.setInfo(1, "修改成功");
        return resultInfo;
    }

    /**
     * 发送通知
     *
     * @param content
     * @param mobile
     */
    private void sendNotice(String content, String mobile) {
        String smsProvider = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_SMS_PROVIDER);
        String smsAccount = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_SMS_ACCOUNT);
        String smsPassword = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_SMS_PASSWORD);
        // 是否发送验证码
        boolean isSend = !ProfileEnum.PROD.getValue().equals(springContextProperties.getProfile()) ? false : true;
        if (isSend) {
            SMSUtil.sendSMS(smsProvider, smsAccount, smsPassword, mobile, content);
        }
    }

    /**
     * 发送短信（发送短信验证码有缓存）
     *
     * @param mobile
     * @param scene
     * @param smsType 1短信  2语音
     * @param content 短信内容
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @datetime 2018/7/17 11:55
     * @author crn
     */
    @Override
    public ResultInfo sendCode(String mobile, String scene, int smsType, String content) {
        ResultInfo result = new ResultInfo();

        String smsProvider = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_SMS_PROVIDER);
        String smsAccount = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_SMS_ACCOUNT);
        String smsPassword = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_SMS_PASSWORD);

        /* 发送短信验证码 */
        SMSUtil.sendCode(smsProvider, smsAccount, smsPassword, mobile, content, SystemConstant.CACHE_TIME_MINUTE_30,
                scene, smsType, redisTemplate, ProfileEnum.getEnum(springContextProperties.getProfile()));

        result.setInfo(1, smsType == 1 ? "短信验证码发送成功" : "验证码已发送，请留意手机来电");
        return result;
    }

    /**
     * 修改被邀请人信息
     *
     * @param inviteeUserId 被邀请人id
     * @param inviterUserId 邀请人id
     * @param type          指定用户角色2:会员,3:美容师,4:顾问
     * @return
     */
    @Transactional
    @Override
    public ResultInfo updateInviterInfo(Long inviteeUserId, Long inviterUserId, int type) {
        ResultInfo resultInfo = new ResultInfo();

        if (inviteeUserId < 1) {
            resultInfo.setInfo(-1, "被邀请人错误");
            return resultInfo;
        }
        if (inviterUserId < 1) {
            resultInfo.setInfo(-1, "邀请人错误");
            return resultInfo;
        }
        if (type != 2 && type != 3 && type != 4) {
            resultInfo.setInfo(-1, "操作类型错误");
            return resultInfo;
        }

        UserInfo userInfo = userInfoMapper.getUserInfoByUserId(inviteeUserId);
        if (userInfo.getSpreadId() == inviterUserId) {
            resultInfo.setInfo(-1, "现直接邀请人和原直接邀请人不能相同");
            return resultInfo;
        }
        userInfo.setSpreadId(inviterUserId);
        userInfo.setSpreadTime(new Date());
        // 邀请人用户信息
        UserInfo inviterUser = userInfoMapper.getUserInfoByUserId(inviterUserId);
        if (RoleTypeEnum.MEMBER.code == type) {
            userInfo.setBeauticianId(inviterUser.getBeauticianId());
            userInfo.setAdviserId(inviterUser.getAdviserId());
        } else if (RoleTypeEnum.BEAUTICIAN.code == type) {
            userInfo.setBeauticianId(inviterUserId);
            userInfo.setAdviserId(inviterUser.getAdviserId());
        } else if (RoleTypeEnum.ADVISER.code == type) {
            userInfo.setBeauticianId(0L);
            userInfo.setAdviserId(inviterUserId);
        }

        int rows = -1;
        try {
            rows = userInfoMapper.updateUserInfoByIdSelective(userInfo);
        } catch (Exception e) {
            LOG.error("更新用户邀请人信息失败--{}", e.getMessage());
            throw new ServiceRuntimeException("更新用户邀请人信息失败", true);
        }
        if (rows < 1) {
            LOG.info("更新用户邀请人信息失败");
            throw new ServiceRuntimeException("更新用户邀请人信息失败", true);
        }

        try {
            updateInviterInfoExt(inviteeUserId, userInfo.getBeauticianId(), userInfo.getAdviserId());
        } catch (Exception e) {
            LOG.error("更新用户邀请人信息失败--{}", e.getMessage());
            throw new ServiceRuntimeException("更新用户邀请人信息失败", true);
        }

        resultInfo.setInfo(1, "修改成功");
        return resultInfo;
    }

    /**
     * 修改被邀请人信息扩展
     *
     * @param inviteeUserId
     * @param beauticianId
     * @param adviserId
     */
    @Transactional
    protected void updateInviterInfoExt(long inviteeUserId, long beauticianId, long adviserId) {
        List<UserInfo> userInfos = userInfoMapper.listUserInfoBySpreadId(inviteeUserId);
        if (userInfos == null || userInfos.size() < 1 || userInfos.isEmpty()) {
            return;
        }
        userInfos.forEach(x -> {
            x.setBeauticianId(beauticianId);
            x.setAdviserId(adviserId);
            int rows = -1;
            try {
                rows = userInfoMapper.updateUserInfoByIdSelective(x);
            } catch (Exception e) {
                LOG.error("修改被邀请人信息扩展失败--{}", e.getMessage());
                throw new ServiceRuntimeException("更新用户邀请人信息失败", true);
            }
            if (rows < 1) {
                LOG.info("修改被邀请人信息扩展失败");
                throw new ServiceRuntimeException("更新用户邀请人信息失败", true);
            }
            updateInviterInfoExt(x.getUserId(), beauticianId, adviserId);
        });
    }

    /**
     * 根据userId获取用户直接邀请人信息
     *
     * @param userId
     * @return
     */
    @Override
    public ResultInfo getInviterInfo(Long userId) {
        ResultInfo resultInfo = new ResultInfo();
        InviteMemberVO inviterInfo = userInfoMapper.getInviterInfo(userId);
        if (null != inviterInfo) {
            inviterInfo.setIsChange(true);
        }
        resultInfo.setInfo(1, "查询成功", inviterInfo);
        return resultInfo;
    }

    /**
     * 根据userId获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserInfo getUserInfoByUserId(Long userId) {

        return userInfoMapper.getUserInfoByUserId(userId);
    }
}
