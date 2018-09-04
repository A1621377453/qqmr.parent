package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.bo.UserSearchBO;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.constant.SystemRedisKeyConstant;
import com.qingqingmr.qqmr.common.enums.AwardTypeEnum;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.enums.SexEnum;
import com.qingqingmr.qqmr.common.util.DateUtil;
import com.qingqingmr.qqmr.common.util.Encrypt;
import com.qingqingmr.qqmr.common.util.GenerateCode;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.dao.*;
import com.qingqingmr.qqmr.domain.bean.*;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujingjing
 * @datetime 2018年7月5日 下午8:07:43
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private SpringContextProperties springContextProperties;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserBonusMapper userBonusMapper;
    @Autowired
    private UserFundsMapper userFundsMapper;
    @Autowired
    private MembershipCardMapper cardMapper;
    @Autowired
    private WxChatMapper wxChatMapper;
    @Autowired
    private StoreAreaMapper storeAreaMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private EventSupervisorService eventSupervisorService;

    /**
     * <p>
     * 根据id查询用户信息
     * </p>
     *
     * @param id
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午2:22:44
     */
    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    /**
     * <p>
     * 分页查询用户邀请会员列表
     * </p>
     *
     * @param currPage
     * @param pageSize
     * @param userId
     * @param type
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 上午10:18:31
     */
    @Override
    public PageResult<InviteMemberVO> listInviteMemberPage(int currPage, int pageSize, long userId, int type) {
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<InviteMemberVO> result = new PageResult<InviteMemberVO>(currPage, pageSize);

        Page<InviteMemberVO> page = PageHelper.startPage(currPage, pageSize);
        List<InviteMemberVO> listDealUserPages = userInfoMapper.listInviteMemberPage(userId, type);

        result.setPage(listDealUserPages);
        result.setTotalCount(page.getTotal());

        return result;
    }

    /**
     * 微信小程序：通过手机号查询用户信息
     *
     * @param mobile 手机号码
     * @return
     * @author liujinjin
     */
    @Override
    public User getUserByMobile(String mobile) {
        return userMapper.getUserByMobile(mobile);

    }

    /**
     * 微信小程序：新增微信用户信息
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public ResultInfo saveBindWXUser(Map<String, String> parameters) {
        ResultInfo resultInfo = new ResultInfo();
        int rows = 1;
        User user = userMapper.getUserByMobile(parameters.get("mobile"));
        try {
            String nickName = parameters.get("nickName");
            String photo = parameters.get("photo");
            String openId = parameters.get("openId");
            // 添加用户表里的记录
            if (user != null) {
                // 修改用户数据
                if ((user.getRoleType() == 3 || user.getRoleType() == 4) && user.getWxOpenId() == null) {
                    user.setNickName(parameters.get("nickName"));// 昵称
                    user.setPhoto(parameters.get("photo"));// 头像
                    user.setWxOpenId(parameters.get("openId"));// 微信openid

                    rows = userMapper.updateUserById(nickName, photo, openId, user.getId());
                    if (rows < 1) {
                        resultInfo.setInfo(-1, "修改用户信息失败");
                        LOG.info("绑定微信用户时:", resultInfo.getCode(), resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }
            } else {
                // 保存用户
                String userName = SystemConstant.NAME_PREFIX + GenerateCode.genCode(8);
                UserSaveVO userSaveVO = new UserSaveVO(null, new Date(), userName, parameters.get("mobile"), null,
                        Integer.valueOf(parameters.get("sex")), nickName, photo, RoleTypeEnum.CUSTOMER.code, -1L,
                        openId);

                rows = userMapper.saveUser(userSaveVO);
                if (rows < 0) {
                    resultInfo.setInfo(-1, "添加用户，保存用户表失败");
                    LOG.info("绑定微信用户时:", resultInfo.getCode(), resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }

                // 取用户id
                Long userId = userSaveVO.getId();

                // 添加用户信息表记录
                UserInfo userInfo = new UserInfo();
                userInfo.setTime(new Date());// 添加时间
                userInfo.setUserId(userId);// 用户id
                userInfo.setStoreId(Long.parseLong(parameters.get("storeId")));// 店面id
                userInfo.setMobile(parameters.get("mobile"));// 手机号
                userInfo.setStoreId(Long.valueOf(parameters.get("storeId")));// 店面id
                // 邀请编码为空，邀请人id默认-1
                if ("".equals(parameters.get("inviterCode"))) {
                    userInfo.setSpreadId(Long.valueOf("-1"));
                    userInfo.setBeauticianId(Long.valueOf("0"));// 所属美容师id
                    userInfo.setAdviserId(Long.valueOf("0"));// 所属顾问id
                } else {
                    User inviterUser = userMapper.getUserByMobile(parameters.get("inviterCode"));
                    userInfo.setSpreadTime(new Date());
                    if (inviterUser != null) {
                        userInfo.setSpreadId(inviterUser.getId());// 邀请人id
                        UserInfo userInfos = userInfoMapper.getUserInfoByMobile(parameters.get("inviterCode"));
                        if (inviterUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code) {
                            userInfo.setBeauticianId(inviterUser.getId());// 所属美容师id
                        } else {
                            userInfo.setBeauticianId(userInfos.getBeauticianId());// 所属美容师id
                        }
                        if (inviterUser.getRoleType() == RoleTypeEnum.ADVISER.code) {
                            userInfo.setAdviserId(inviterUser.getId());
                        } else {
                            userInfo.setAdviserId(userInfos.getAdviserId());// 所属顾问id
                        }

                    }
                }
                rows = userInfoMapper.saveUserInfo(userInfo);
                if (rows < 0) {
                    resultInfo.setInfo(-1, "添加用户信息，保存用户信息表失败");
                    LOG.info("绑定微信用户时:", resultInfo.getCode(), resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }

                // 添加用户资金表记录
                UserFunds userFunds = new UserFunds();
                userFunds.setUserId(userId);// 用户id
                userFunds.setTime(new Date());// 添加时间
                userFunds.setBalance(0.00);// 账户余额
                userFunds.setAvailableBalance(0.00);// 可用的账户余额
                userFunds.setFreeze(0.00);// 冻结金额
                String sign = Security.decodeFunds(userId, 0.00, 0.00, springContextProperties.getMd5Key());
                userFunds.setSign(sign);// 用户资金加密：MD5(user_id,balance,freeze,md5Sign)

                rows = userFundsMapper.saveUserFunds(userFunds);
                if (rows < 0) {
                    resultInfo.setInfo(-1, "添加用户资金信息，保存用户资金信息表失败");
                    LOG.info("绑定微信用户时:", resultInfo.getCode(), resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }

                // 添加微信绑定绑定表记录
                WxChat wxChat = new WxChat();
                if (wxChatMapper.getWxChatByOpenId(parameters.get("openId")) == null) {
                    wxChat.setTime(new Date());// 添加时间
                    wxChat.setOpenId(parameters.get("openId"));// openid
                    wxChat.setNickName(parameters.get("nickName"));// 昵称
                    wxChat.setImageUrl(parameters.get("photo"));// 头像
                    wxChat.setSex(Integer.valueOf(parameters.get("sex")));// 性别
                    wxChat.setWxInfo(parameters.get("wxInfo"));// 微信授权信息
                    rows = wxChatMapper.saveWxChat(wxChat);

                    if (rows < 0) {
                        resultInfo.setInfo(-1, "添加微信绑定信息，保存微信绑定信息表失败");
                        LOG.info("绑定微信用户时:", resultInfo.getCode(), resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceRuntimeException("添加异常", true);
        }

        resultInfo.setInfo(1, "添加微信绑定信息成功");
        return resultInfo;
    }

    /**
     * 微信小程序：通过手机号修改用户角色
     *
     * @param mobile 手机号码
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int updateUserRoleType(String mobile) {
        int num = userMapper.updateUserRoleType(mobile);
        if (num < 1) {
            LOG.info("通过手机号修改用户角色失败!");
            throw new ServiceRuntimeException("修改用户角色失败!", true);
        }
        return num;
    }

    /**
     * 微信小程序：通过用户id修改用户表里的角色
     *
     * @param roleTypeEnum 会员类型
     * @param id           用户id
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int updateUserRoleTypeById(RoleTypeEnum roleTypeEnum, long id) {
        int num = userMapper.updateUserRoleTypeById(roleTypeEnum.code, id);
        if (num < 1) {
            LOG.info("通过用户id修改用户表里的角色失败!");
            throw new ServiceRuntimeException("修改用户角色失败!", true);
        }
        return num;
    }

    /**
     * 微信小程序：昵称修改
     *
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int updateUserNickName(Long userId, String nickName) {
        int num = userMapper.updateUserNickName(userId, nickName);
        if (num < 1) {
            LOG.error("通过用户id修改昵称失败");
            throw new ServiceRuntimeException("通过用户id修改昵称失败", true);
        }
        return num;
    }

    /**
     * 微信小程序：性别修改
     *
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int updateUserSex(Long userId, String sex) {
        int num = userMapper.updateUserSex(userId, sex);
        if (num < 1) {
            LOG.error("通过用户id修改性别失败");
            throw new ServiceRuntimeException("通过用户id修改性别失败", true);
        }
        return num;
    }

    /**
     * 微信小程序：通过用户id修改手机号
     *
     * @param userId
     * @param mobile
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public ResultInfo updateUserMobile(Long userId, String mobile) {
        ResultInfo resultInfo = new ResultInfo();
        User user = userMapper.getUserById(userId);
        if (user == null) {
            resultInfo.setInfo(-1, "该用户不存在");
            return resultInfo;
        }
        int num = userMapper.updateUserMobile(userId, mobile);
        if (num < 1) {
            LOG.error("修改用户手机号失败");
            resultInfo.setInfo(-1, "修改用户手机号失败");
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        // 用户是顾问或者美容师的时候，修改员工列表里的手机号
        user = userMapper.getUserById(userId);
        int roleType = user.getRoleType();
        if (roleType == 3 || roleType == 4) {
            Long employeeId = user.getEmployeeId();
            Employee employee = new Employee();
            employee.setId(employeeId);
            employee.setMobile(mobile);
            num = employeeMapper.updateEmployeeMobile(employee);
            if (num < 1) {
                LOG.error("修改员工手机号失败");
                resultInfo.setInfo(-1, "修改员工手机号失败");
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }
        }

        // 修改用户信息表里的手机号
        num = userInfoMapper.updateUserMobile(userId, mobile);
        if (num < 1) {
            LOG.error("修改手机号失败");
            resultInfo.setInfo(-1, "修改手机号失败");
            throw new ServiceRuntimeException("修改手机号失败", true);
        }
        resultInfo.setInfo(1, "修改手机号成功");
        return resultInfo;
    }

    /**
     * 通过用户id修改交易密码
     *
     * @param userId
     * @param payPassword
     * @param oldPayPassword
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @datetime 2018/7/30 19:37
     * @author liujinjin
     */
    @Transactional
    @Override
    public ResultInfo updatePsyPasswordById(Long userId, String payPassword, String oldPayPassword) {
        int num = 0;
        ResultInfo resultInfo = new ResultInfo();
        // 旧交易密码为空，就是设置或者重置交易密码，修改用户表的交易密码
        if (oldPayPassword == null || "".equals(oldPayPassword)) {
            payPassword = Encrypt.decryptDES(payPassword, springContextProperties.getAppDesKey());
            if (payPassword.length() != 6) {
                resultInfo.setInfo(-1, "请设置6位的交易密码");
                return resultInfo;
            }
            payPassword = Encrypt.MD5(payPassword + springContextProperties.getMd5Key());
            num = userMapper.updateUserPayPassword(userId, payPassword);
            if (num < 1) {
                LOG.error("通过用户id修改交易密码失败");
                throw new ServiceRuntimeException("通过用户id修改交易密码失败", true);
            }
        } else {
            oldPayPassword = Encrypt.decryptDES(oldPayPassword, springContextProperties.getAppDesKey());
            payPassword = Encrypt.decryptDES(payPassword, springContextProperties.getAppDesKey());
            if (oldPayPassword.length() < 6) {
                resultInfo.setInfo(-1, "旧交易密码不正确");
                return resultInfo;
            }
            if (payPassword.length() < 6) {
                resultInfo.setInfo(-1, "请设置6位的交易密码");
                return resultInfo;
            }
            // MD5加密旧密码后与数据库比较
            String oldPayPasswords = Encrypt.MD5(oldPayPassword + springContextProperties.getMd5Key());

            User user = userMapper.getUserById(userId);
            String payPasswordData = user.getPayPassword();

            if (!oldPayPasswords.equals(payPasswordData)) {
                resultInfo.setInfo(-1, "旧交易密码不正确");
                return resultInfo;
            }

            if (oldPayPassword.equals(payPassword)) {
                resultInfo.setInfo(-1, "旧交易密码和新交易密码不能相同");
                return resultInfo;
            } else {
                String updatePayPassword = Encrypt.MD5(payPassword + springContextProperties.getMd5Key());
                num = userMapper.updateUserPayPassword(userId, updatePayPassword);
                if (num < 1) {
                    LOG.error("通过用户id修改交易密码失败");
                    throw new ServiceRuntimeException("通过用户id修改交易密码失败", true);
                }
            }
        }
        resultInfo.setInfo(1, "交易密码设置成功");
        return resultInfo;
    }

    /**
     * 获取用户列表
     *
     * @param userSearchBo
     * @param request
     * @return com.qingqingmr.qqmr.common.PageResult<com.qingqingmr.qqmr.domain.bean.UserVO>
     * @method listUser
     * @datetime 2018/7/10 11:32
     * @author crn
     */
    @Override
    public PageResult<UserVO> listUser(UserSearchBO userSearchBo, HttpServletRequest request) {

        Integer currPage = userSearchBo.getCurrPage();
        Integer pageSize = userSearchBo.getPageSize();
        currPage = null == currPage || currPage == 0 ? 1 : currPage;
        pageSize = null == pageSize || pageSize == 0 ? 10 : pageSize;
        PageResult<UserVO> result = new PageResult<UserVO>(currPage, pageSize);
        Page<UserVO> page = PageHelper.startPage(currPage, pageSize);
        String type = userSearchBo.getType();
        String typeStr = userSearchBo.getTypeStr();

        // 兼容搜索昵称是表情符号
        if (("2".equals(type) || "4".equals(type) || "6".equals(type)) && StringUtils.isNotBlank(typeStr)) {
            userSearchBo.setTypeStr(Security.encodeHexString(typeStr));
        }
        List<UserVO> userVOS = userMapper.listUser(userSearchBo);

        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request,
                    RoleTypeEnum.MEMBER.code == userSearchBo.getRoleType()
                            ? EventSupervisor.SupervisorEventEnum.SUPERVISOR_MEMBER_SHOW
                            : EventSupervisor.SupervisorEventEnum.SUPERVISOR_CUSTOMER_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        result.setPage(userVOS);
        result.setTotalCount(page.getTotal());
        return result;
    }

    /**
     * 获取用户详细信息
     *
     * @param type
     * @param userId
     * @param request
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method getUserDetail
     * @datetime 2018/7/10 17:27
     * @author crn
     */
    @Override
    public ResultInfo getUserDetail(Integer type, Long userId, HttpServletRequest request)
            throws UnsupportedEncodingException {

        ResultInfo resultInfo = new ResultInfo();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 获取个人信息
        User user = userMapper.getUserById(userId);
        resultMap.put("user", user);
        // 获取门店名称
        Store store = storeMapper.getStoreNameByUserId(userId);
        if (null == store) {
            LOG.error("获取会员详情，获取门店信息为空");
            throw new ServiceRuntimeException("获取会员详情，获取门店信息为空", true);
        }
        StoreArea storeArea = storeAreaMapper.getStoreAreaByStoreId(store.getId());
        resultMap.put("storeAreaName", storeArea.getName());
        resultMap.put("storeId", store.getId());
        resultMap.put("storeName", store.getName());

        // 获取收货地址
        List<UserAddressVO> listUserAddress = userAddressMapper.listUserAddress(userId);
        resultMap.put("listUserAddress", listUserAddress);

        UserVO userVO = userMapper.getUserinviteInfoByUserId(userId);
        // 直接邀请人id
        Long dirInviteId = userVO.getDirInviteId();
        // 直接邀请人角色
        String dirInviteRole = userVO.getDirInviteRole();
        // 间接邀请人id
        Long inDirInviteId = userVO.getInDirInviteId();
        // 间接邀请人角色
        String inDirInviteRole = userVO.getInDirInviteRole();
        // 会员卡id
        Long cardId = userVO.getCardId();

        // 获取直接邀请人信息
        if (null != dirInviteId && 0 != dirInviteId) {
            if (StringUtils.isNotBlank(dirInviteRole) && !"--".equals(dirInviteRole)) {
                InviteMemberVO memberVO = new InviteMemberVO();
                if (dirInviteRole.equals(RoleTypeEnum.BEAUTICIAN.value)
                        || dirInviteRole.equals(RoleTypeEnum.ADVISER.value)) {
                    User dirInviteUser = userMapper.getUserById(dirInviteId);
                    if (null == dirInviteUser) {
                        LOG.error("获取用户信息，获取直接邀请人信息失败");
                        resultInfo.setInfo(-1, "获取用户信息，获取直接邀请人信息失败");
                        return resultInfo;
                    }
                    EmployeeVO employeeVO = employeeMapper.getEmployeeById(dirInviteUser.getEmployeeId());

                    memberVO.setUserId(employeeVO.getId());
                    memberVO.setPhoto(employeeVO.getPhoto());
                    memberVO.setNickName(employeeVO.getRealityName());
                    memberVO.setMobile(employeeVO.getMobile());
                    memberVO.setSexStr(
                            employeeVO.getSex() == SexEnum.MAN.code ? SexEnum.MAN.value : SexEnum.WOMAN.value);
                    memberVO.setRoleTypeStr(employeeVO.getRoleType() == Employee.RoleTypeEnum.BEAUTICIAN.code
                            ? Employee.RoleTypeEnum.BEAUTICIAN.value
                            : Employee.RoleTypeEnum.COUNSELOR.value);
                } else {
                    User dirInviteUser = userMapper.getUserById(dirInviteId);
                    // 获取会员卡信息
                    MembershipCard cardInfo = cardMapper.getMembershipCardNoInfoByUserId(dirInviteId);

                    // 获取直接邀请人userinfo信息
                    UserInfo dirInviteUserInfo = userInfoMapper.getUserInfoByUserId(dirInviteId);
                    Long spreadId = dirInviteUserInfo.getSpreadId();
                    if (null == spreadId || 0 == spreadId || -1 == spreadId) {
                        memberVO.setDirInviteId(0L);
                        memberVO.setDirInviteRole("");
                        memberVO.setInDirInviteId(0L);
                        memberVO.setInDirInviteRole("");
                    } else {
                        User spreadIdUser = userMapper.getUserById(spreadId);
                        memberVO.setDirInviteId(spreadIdUser.getId());
                        memberVO.setDirInviteRole(
                                spreadIdUser.getRoleType() == RoleTypeEnum.MEMBER.code ? RoleTypeEnum.MEMBER.value
                                        : spreadIdUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code
                                        ? RoleTypeEnum.BEAUTICIAN.value
                                        : RoleTypeEnum.ADVISER.value);
                        Long beauticianId = dirInviteUserInfo.getBeauticianId();
                        Long adviserId = dirInviteUserInfo.getAdviserId();
                        if (spreadId != beauticianId && spreadId != adviserId) {
                            if (null != beauticianId && 0 != beauticianId && -1 != beauticianId) {
                                memberVO.setInDirInviteId(beauticianId);
                                memberVO.setInDirInviteRole(RoleTypeEnum.BEAUTICIAN.value);
                            } else {
                                memberVO.setInDirInviteId(adviserId);
                                memberVO.setInDirInviteRole(RoleTypeEnum.ADVISER.value);
                            }
                        }
                    }

                    memberVO.setUserId(dirInviteUser.getId());
                    memberVO.setPhoto(dirInviteUser.getPhoto());
                    memberVO.setNickName(dirInviteUser.getNickName());
                    memberVO.setMobile(dirInviteUser.getMobile());
                    memberVO.setSexStr(dirInviteUser.getSex() == SexEnum.UNKNOWN.code ? SexEnum.UNKNOWN.value
                            : dirInviteUser.getSex() == SexEnum.MAN.code ? SexEnum.MAN.value : SexEnum.WOMAN.value);
                    memberVO.setRoleTypeStr(dirInviteUser.getRoleType() == RoleTypeEnum.CUSTOMER.code
                            ? RoleTypeEnum.CUSTOMER.value
                            : dirInviteUser.getRoleType() == RoleTypeEnum.MEMBER.code ? RoleTypeEnum.MEMBER.value
                            : dirInviteUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code
                            ? RoleTypeEnum.BEAUTICIAN.value
                            : RoleTypeEnum.ADVISER.value);
                    if (null == cardInfo) {
                        memberVO.setCardId(0L);
                    } else {
                        memberVO.setCardId(cardInfo.getId());
                    }
                }

                // 直接邀请人是否可更改
                if (type == RoleTypeEnum.MEMBER.code) {
                    if (dirInviteRole.equals(RoleTypeEnum.BEAUTICIAN.value)
                            || dirInviteRole.equals(RoleTypeEnum.MEMBER.value)) {
                        memberVO.setIsChange(true);
                    } else {
                        memberVO.setIsChange(false);
                    }
                } else if (type == RoleTypeEnum.CUSTOMER.code) {
                    if (dirInviteRole.equals(RoleTypeEnum.CUSTOMER.value)) {
                        memberVO.setIsChange(false);
                    } else {
                        memberVO.setIsChange(true);
                    }
                }
                resultMap.put("memberVO", memberVO);
            }
        }
        // 获取间接邀请人信息
        if (null != inDirInviteId && 0 != inDirInviteId) {
            if (StringUtils.isNotBlank(inDirInviteRole) && !"--".equals(inDirInviteRole)) {
                InviteMemberVO inMemberVO = new InviteMemberVO();
                if (inDirInviteRole.equals(RoleTypeEnum.BEAUTICIAN.value)
                        || inDirInviteRole.equals(RoleTypeEnum.ADVISER.value)) {
                    User inDirInviteUser = userMapper.getUserById(inDirInviteId);
                    if (null == inDirInviteUser) {
                        LOG.error("获取用户信息，获取直接邀请人信息失败");
                        resultInfo.setInfo(-1, "获取用户信息，获取直接邀请人信息失败");
                        return resultInfo;
                    }
                    EmployeeVO employeeVO = employeeMapper.getEmployeeById(inDirInviteUser.getEmployeeId());

                    inMemberVO.setUserId(employeeVO.getId());
                    inMemberVO.setPhoto(employeeVO.getPhoto());
                    inMemberVO.setNickName(employeeVO.getRealityName());
                    inMemberVO.setMobile(employeeVO.getMobile());
                    inMemberVO.setSexStr(
                            employeeVO.getSex() == SexEnum.MAN.code ? SexEnum.MAN.value : SexEnum.WOMAN.value);
                    inMemberVO.setRoleTypeStr(employeeVO.getRoleType() == Employee.RoleTypeEnum.BEAUTICIAN.code
                            ? Employee.RoleTypeEnum.BEAUTICIAN.value
                            : Employee.RoleTypeEnum.COUNSELOR.value);
                } else {

                    User inDirInviteUser = userMapper.getUserById(inDirInviteId);
                    // 获取会员卡信息
                    MembershipCard cardInfo = cardMapper.getMembershipCardNoInfoByUserId(dirInviteId);

                    // 获取直接邀请人userinfo信息
                    UserInfo dirInviteUserInfo = userInfoMapper.getUserInfoByUserId(dirInviteId);
                    Long spreadId = dirInviteUserInfo.getSpreadId();
                    if (null == spreadId || 0 == spreadId || -1 == spreadId) {
                        inMemberVO.setDirInviteId(0L);
                        inMemberVO.setDirInviteRole("");
                        inMemberVO.setInDirInviteId(0L);
                        inMemberVO.setInDirInviteRole("");
                    } else {
                        User spreadIdUser = userMapper.getUserById(spreadId);
                        inMemberVO.setDirInviteId(spreadIdUser.getId());
                        inMemberVO.setDirInviteRole(
                                spreadIdUser.getRoleType() == RoleTypeEnum.MEMBER.code ? RoleTypeEnum.MEMBER.value
                                        : spreadIdUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code
                                        ? RoleTypeEnum.BEAUTICIAN.value
                                        : RoleTypeEnum.ADVISER.value);
                        Long beauticianId = dirInviteUserInfo.getBeauticianId();
                        Long adviserId = dirInviteUserInfo.getAdviserId();
                        if (spreadId != beauticianId && spreadId != adviserId) {
                            if (null != beauticianId && 0 != beauticianId && -1 != beauticianId) {
                                inMemberVO.setInDirInviteId(beauticianId);
                                inMemberVO.setInDirInviteRole(RoleTypeEnum.BEAUTICIAN.value);
                            } else {
                                inMemberVO.setInDirInviteId(adviserId);
                                inMemberVO.setInDirInviteRole(RoleTypeEnum.ADVISER.value);
                            }
                        }
                    }

                    inMemberVO.setUserId(inDirInviteUser.getId());
                    inMemberVO.setPhoto(inDirInviteUser.getPhoto());
                    inMemberVO.setNickName(inDirInviteUser.getNickName());
                    inMemberVO.setMobile(inDirInviteUser.getMobile());
                    inMemberVO.setSexStr(inDirInviteUser.getSex() == SexEnum.UNKNOWN.code ? SexEnum.UNKNOWN.value
                            : inDirInviteUser.getSex() == SexEnum.MAN.code ? SexEnum.MAN.value : SexEnum.WOMAN.value);
                    inMemberVO.setRoleTypeStr(inDirInviteUser.getRoleType() == RoleTypeEnum.CUSTOMER.code
                            ? RoleTypeEnum.CUSTOMER.value
                            : inDirInviteUser.getRoleType() == RoleTypeEnum.MEMBER.code ? RoleTypeEnum.MEMBER.value
                            : inDirInviteUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code
                            ? RoleTypeEnum.BEAUTICIAN.value
                            : RoleTypeEnum.ADVISER.value);
                    if (null == cardInfo) {
                        inMemberVO.setCardId(0L);
                    } else {
                        inMemberVO.setCardId(cardInfo.getId());
                    }
                }
                resultMap.put("inMemberVO", inMemberVO);
            }
        }

        boolean dirInviteFlag = null == dirInviteId || 0 == dirInviteId ? true : false;
        boolean inDirInviteFlag = null == inDirInviteId || 0 == inDirInviteId ? true : false;
        if (dirInviteFlag && inDirInviteFlag) {
            resultMap.put("addInvite", true);
        } else {
            resultMap.put("addInvite", false);
        }

        if (type == RoleTypeEnum.MEMBER.code) {

            // 会员卡信息
            MembershipCard card = null;
            if (null != cardId && 0 != cardId) {
                card = cardMapper.getMembershipCardById(cardId);
            }
            resultMap.put("card", card);

            // 获取用户资金信息
            UserFundsVO userFunds = userFundsMapper.getUserFundsByUserId(userId);
            resultMap.put("userFunds", userFunds);

            // 用户累计赚取金额
            Double userAmountByUserId = userBonusMapper.getUserAmountByUserId(userId);
            resultMap.put("userAmountByUserId", userAmountByUserId);

            // 获取用户本月直接赚取金额
            Double userCurMonDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                    RoleTypeEnum.MEMBER.code, AwardTypeEnum.ONE_AWARD.code);
            resultMap.put("userCurMonDirectAmount", userCurMonDirectAmount);

            // 获取用户本月间接赚取金额
            Double userCurMonInDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                    RoleTypeEnum.MEMBER.code, AwardTypeEnum.NO_DIRECT_AWARD.code);
            resultMap.put("userCurMonInDirectAmount", userCurMonInDirectAmount);

            // 获取用户累计直接邀请会员
            int userSpreadMember = userInfoMapper.getUserSpreadsByUserId(userId, RoleTypeEnum.MEMBER.code);
            resultMap.put("userSpreadMember", userSpreadMember);

            // 获取用户本月邀请会员
            int userCurMonSpreadMember = userInfoMapper.getUserCurMonSpreadsByUserId(userId, RoleTypeEnum.MEMBER.code);
            resultMap.put("userCurMonSpreadMember", userCurMonSpreadMember);

            // 获取用户本月邀请顾客
            int userCurMonSpreadCustomer = userInfoMapper.getUserCurMonSpreadsByUserId(userId,
                    RoleTypeEnum.CUSTOMER.code);
            resultMap.put("userCurMonSpreadCustomer", userCurMonSpreadCustomer);
        }

        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request,
                    RoleTypeEnum.MEMBER.code == type ? EventSupervisor.SupervisorEventEnum.SUPERVISOR_MEMBER_DETAIL
                            : EventSupervisor.SupervisorEventEnum.SUPERVISOR_CUSTOMER_SHOW_DETAIL);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;

    }

    /**
     * 获取用户当月佣金记录
     *
     * @param userId
     * @param currPage
     * @param pageSize
     * @method listUserCurMonBonus
     * @datetime 2018/7/10 17:55
     * @author crn
     */
    @Override
    public PageResult<Map<String, Object>> listUserCurMonBonus(Long userId, Integer currPage, Integer pageSize) {
        currPage = null == currPage || currPage == 0 ? 1 : currPage;
        pageSize = null == pageSize || pageSize == 0 ? 10 : pageSize;
        PageResult<Map<String, Object>> result = new PageResult<Map<String, Object>>(currPage, pageSize);
        Page<Map<String, Object>> page = PageHelper.startPage(currPage, pageSize);
        // 获取用户当月佣金列表信息
        List<Map<String, Object>> userCurMonBonus = employeeMapper.listUserCurMonBonus(userId);
        userCurMonBonus.forEach(x -> {
            x.forEach((k, v) -> {
                if ("time".equals(k)) {
                    x.put("time", DateUtil.dateToString((Date) v, SystemConstant.DATE_TIME_FORMAT));
                }
                if ("inviteeName".equals(k)) {
                    x.put("inviteeName", Security.decodeHex(String.valueOf(v)));
                }
            });
        });

        result.setPage(userCurMonBonus);
        result.setTotalCount(page.getTotal());
        return result;
    }

    /**
     * 根据门店id获取门店用户
     *
     * @param type    2:会员 3:美容师 4:顾问
     * @param storeId
     * @param key     姓名或手机号
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method listUserByStoreId
     * @datetime 2018/7/11 11:40
     * @author crn
     */
    @Override
    public ResultInfo listUserByStoreId(Integer type, Long storeId, String key) {
        ResultInfo resultInfo = new ResultInfo();

        List<Map<String, Object>> resultMap = null;
        if (RoleTypeEnum.MEMBER.code == type) {
            key = Security.encodeHexString(key);
            resultMap = userInfoMapper.listUserByStoreId(storeId, key);
            resultMap.stream().forEach(map -> map.forEach((k, v) -> {
                if ("name".equals(k)) {
                    map.put(k, Security.decodeHex(String.valueOf(v)));
                }
            }));
        } else if (RoleTypeEnum.BEAUTICIAN.code == type || RoleTypeEnum.ADVISER.code == type) {
            type = RoleTypeEnum.BEAUTICIAN.code == type ? 1 : 2;
            resultMap = employeeMapper.listEmployeeByStoreId(storeId, type, key);
        }
        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;
    }

    /**
     * 微信小程序：校验用户输入的交易密码是否正确
     *
     * @param userId
     * @param payPassword
     * @return
     * @author liujinjin
     */
    @Override
    public int checkPsyPassword(Long userId, String payPassword) {
        String inputPayPassword = Encrypt.MD5(payPassword + springContextProperties.getMd5Key());
        User user = userMapper.getUserById(userId);
        String payPassWord = user.getPayPassword();
        if (!inputPayPassword.equals(payPassWord)) {
            return -1;
        }
        return 1;
    }

    /**
     * 微信小程序：通过微信openId获取用户信息
     *
     * @param openId
     * @return
     * @author liujinjin
     */
    @Override
    public User getUserByWxOpenId(String openId) {
        return userMapper.getUserByWxOpenId(openId);
    }

    /**
     * 根据员工id获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public User getUserByEmployeeId(Long id) {
        return userMapper.getUserByEmployeeId(id);
    }

    /**
     * <p>
     * 验证验证码
     * </p>
     *
     * @param verificationCode
     * @param mobile
     * @param scene
     * @return
     * @author liujingjing
     * @datetime 2018年7月18日 上午11:34:20
     */
    @Override
    public ResultInfo verifyCode(String verificationCode, String mobile, String scene) {
        ResultInfo result = new ResultInfo();

        String key = springContextProperties.getProfile() + SystemRedisKeyConstant.MOBILE + mobile + "." + scene;
        Object cache = redisTemplate.opsForValue().get(key);
        if (cache == null) {
            result.setInfo(-1, "短信验证码已失效");
            return result;
        }

        String code = cache.toString();
        if (!code.equals(verificationCode)) {
            result.setInfo(-1, "验证码输入有误");
            return result;
        }

        result.setInfo(1, "验证成功");
        return result;
    }
}
