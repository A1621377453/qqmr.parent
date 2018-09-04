package com.qingqingmr.qqmr.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.bo.UserBonusSearchBO;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SettingKeyConstant;
import com.qingqingmr.qqmr.common.constant.SystemRedisKeyConstant;
import com.qingqingmr.qqmr.common.enums.AwardTypeEnum;
import com.qingqingmr.qqmr.common.enums.BizzTypeEnum;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.util.Arith;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.GenerateCode;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.dao.UserBonusDetailMapper;
import com.qingqingmr.qqmr.dao.UserBonusMapper;
import com.qingqingmr.qqmr.dao.UserInfoMapper;
import com.qingqingmr.qqmr.dao.UserMapper;
import com.qingqingmr.qqmr.domain.bean.UserBonusVO;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 用户奖金
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月6日 下午6:04:41
 */
@Service
public class UserBonusServiceImpl implements UserBonusService {
    private final static Logger LOG = LoggerFactory.getLogger(UserBonusServiceImpl.class);

    @Autowired
    private UserBonusMapper userBonusMapper;
    @Autowired
    private UserBonusDetailMapper userBonusDetailMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DealUserService dealUserService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private UserFundsService userFundsService;
    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private UserMapper userMapper;

    /**
     * <p>
     * 分页查询用户佣金明细
     * </p>
     *
     * @param currPage
     * @param pageSize
     * @param userId
     * @return
     * @author liujingjing
     * @datetime 2018年7月6日 下午6:03:38
     */
    @Override
    public PageResult<UserBonus> listUserBonusPage(int currPage, int pageSize, long userId) {
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<UserBonus> result = new PageResult<UserBonus>(currPage, pageSize);

        Page<UserBonus> page = PageHelper.startPage(currPage, pageSize);
        List<UserBonus> listOrdersPages = userBonusMapper.listUserBonusPage(userId);

        result.setPage(listOrdersPages);
        result.setTotalCount(page.getTotal());

        return result;
    }

    /**
     * <p>
     * 查询我的邀请信息
     * </p>
     *
     * @param userId
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 下午4:02:17
     */
    @Override
    public ResultInfo myInvite(long userId) {
        ResultInfo resultInfo = new ResultInfo();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        User userById = userMapper.getUserById(userId);

        // 用户累计赚取金额
        Double userAmountByUserId = userBonusMapper.getUserAmountByUserId(userId);
        resultMap.put("userAmountByUserId", userAmountByUserId);

        // 获取用户本月直接赚取金额
        Integer roleType = userById.getRoleType();
        Double userCurMonDirectAmount = 0.0;
        if (roleType == RoleTypeEnum.ADVISER.code) {
            userCurMonDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                    roleType, AwardTypeEnum.DIRECT_AWARD.code);
        } else if (roleType == RoleTypeEnum.BEAUTICIAN.code || roleType == RoleTypeEnum.MEMBER.code) {
            userCurMonDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                    roleType, AwardTypeEnum.ONE_AWARD.code);
        }
        // 获取用户本月间接赚取金额
        Double userCurMonInDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId,
                roleType, AwardTypeEnum.NO_DIRECT_AWARD.code);
        // 获取本月累计赚取的金额
        Long totalAmountCurMon = (long) Arith.add(userCurMonDirectAmount, userCurMonInDirectAmount);
        resultMap.put("totalAmountCurMon", totalAmountCurMon);

        // 获取累计邀请会员
        int totalInviteMember = userInfoMapper.getUserInviteMember(userId, RoleTypeEnum.MEMBER.code);
        resultMap.put("totalInviteMember", totalInviteMember);

        // 获取本月邀请的会员
        int thisMonInviteMember = userInfoMapper.getUserInviteMemberThisMon(userId, RoleTypeEnum.MEMBER.code);
        resultMap.put("thisMonInviteMember", thisMonInviteMember);

        // 获取本月邀请的顾客
        int thisMonInviteCus = userInfoMapper.getUserInviteMemberThisMon(userId, RoleTypeEnum.CUSTOMER.code);
        resultMap.put("thisMonInviteCus", thisMonInviteCus);

        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;
    }

    /**
     * 根据条件查询获取佣金列表
     *
     * @param bonusSearchBO
     * @param request
     * @return
     * @author crn
     */
    @Override
    public PageResult<UserBonusVO> listBonus(UserBonusSearchBO bonusSearchBO, HttpServletRequest request) {
        Integer currPage = bonusSearchBO.getCurrPage();
        Integer pageSize = bonusSearchBO.getPageSize();
        currPage = null == currPage || currPage == 0 ? 1 : currPage;
        pageSize = null == pageSize || pageSize == 0 ? 10 : pageSize;
        PageResult<UserBonusVO> result = new PageResult<UserBonusVO>(currPage, pageSize);
        Page<UserBonusVO> page = PageHelper.startPage(currPage, pageSize);
        String type = bonusSearchBO.getType();
        String typeStr = bonusSearchBO.getTypeStr();

        //兼容搜索昵称是表情符号
        if ("2".equals(type) && StringUtils.isNotBlank(typeStr)) {
            bonusSearchBO.setTypeStr(Security.encodeHexString(typeStr));
        }
        List<UserBonusVO> userBonusVOS = userBonusMapper.listBonus(bonusSearchBO);

        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.AWARD_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setPage(userBonusVOS);
        result.setTotalCount(page.getTotal());
        return result;
    }

    /**
     * 保存佣金明细
     *
     * @param userBonusDetail
     * @return
     */
    @Transactional
    @Override
    public ResultInfo saveBonusDetail(UserBonusDetail userBonusDetail) {
        ResultInfo resultInfo = new ResultInfo();
        if (userBonusDetail == null) {
            resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return resultInfo;
        }

        int rows = 0;
        try {
            rows = userBonusDetailMapper.saveUserBonusDetail(userBonusDetail);
        } catch (Exception e) {
            LOG.error("保存佣金明细异常--{}", e.getMessage());
            // throw new ServiceRuntimeException("保存佣金明细异常", true);
            resultInfo.setInfo(-1, "保存佣金明细异常");
            return resultInfo;
        }
        if (rows < 1) {
            resultInfo.setInfo(-1, "保存失败");
            return resultInfo;
        }

        resultInfo.setInfo(1, "保存成功", userBonusDetail.getId());
        return resultInfo;
    }

    /**
     * 保存佣金
     *
     * @param userBonus
     * @return
     */
    @Transactional
    @Override
    public ResultInfo saveBonus(UserBonus userBonus) {
        ResultInfo resultInfo = new ResultInfo();
        if (userBonus == null) {
            resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return resultInfo;
        }

        Double amount = userBonus.getAmount();
        if (amount <= 0) {
            resultInfo.setInfo(-1, "佣金金额不能小于等于0");
            return resultInfo;
        }

        // 计算管理费
        String awardFee = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_AWARD_FEE);
        double fee = 0.00;
        if (StringUtils.isNotBlank(awardFee)) {
            fee = Arith.round(Arith.mul(userBonus.getAmount(), Arith.div(Double.parseDouble(awardFee), 100, 4)), 2);
        }
        userBonus.setFee(fee);

        // 保存佣金记录
        int rows = 0;
        try {
            rows = userBonusMapper.saveUserBonus(userBonus);
        } catch (Exception e) {
            LOG.error("保存佣金明细异常--{}", e.getMessage());
            // throw new ServiceRuntimeException("保存佣金明细异常", true);
            resultInfo.setInfo(-1, "保存佣金明细异常");
            return resultInfo;
        }
        if (rows < 1) {
            resultInfo.setInfo(-1, "保存失败");
            return resultInfo;
        }

        resultInfo = userFundsService.checkUserFund(userBonus.getUserId());
        if (resultInfo.getCode() < 0) {
            resultInfo.setInfo(-1, ResultInfo.STR_SIGN_ERROR);
            return resultInfo;
        }

        // 添加用户资金
        rows = userFundsService.updateAddBalance(userBonus.getUserId(), amount);
        if (rows < 1) {
            resultInfo.setInfo(-1, "更新用户资金失败");
            return resultInfo;
        }

        // 添加用户交易记录
        DealUser dealUser = new DealUser(userBonus.getUserId(), userBonus.getServiceOrderNo(), BizzTypeEnum.RAKE_BACK.getDealType(), BizzTypeEnum.RAKE_BACK.getCode(), amount, userBonus.getRemark());
        rows = dealUserService.saveDealUser(dealUser);
        if (rows < 1) {
            resultInfo.setInfo(-1, "保存交易记录失败");
            return resultInfo;
        }

        //  后台管理费和扣除用户的管理费
        if (fee > 0) {
            // 后台管理费
            String feeOrderNo = GenerateCode.getOrderNo(BizzTypeEnum.MANAGE_FEE.getPrefix());
            PlatformFee platformFee = new PlatformFee(new Date(), userBonus.getUserId(), feeOrderNo, fee, PlatformFee.FeeTypeEnum.COMMISSION.code, userBonus.getId(), PlatformFee.FeeTypeEnum.COMMISSION.value);
            rows = platformService.savePlatformFee(platformFee);
            if (rows < 1) {
                resultInfo.setInfo(-1, "保存管理费失败");
                return resultInfo;
            }

            // 减少用户资金
            rows = userFundsService.updateSubBalance(userBonus.getUserId(), fee);
            if (rows < 1) {
                resultInfo.setInfo(-1, "更新用户资金失败");
                return resultInfo;
            }

            // 添加用户交易记录
            dealUser = new DealUser(userBonus.getUserId(), feeOrderNo, BizzTypeEnum.MANAGE_FEE.getDealType(), BizzTypeEnum.MANAGE_FEE.getCode(), fee, "扣除佣金管理费");
            rows = dealUserService.saveDealUser(dealUser);
            if (rows < 1) {
                resultInfo.setInfo(-1, "保存交易记录失败");
                return resultInfo;
            }
        }

        // 更新签名
        rows = userFundsService.updateFundsSign(userBonus.getUserId());
        if (rows < 1) {
            resultInfo.setInfo(-1, "更新用户签名失败");
            return resultInfo;
        }

        resultInfo.setInfo(1, "保存成功");
        return resultInfo;
    }

    /**
     * 发放佣金
     *
     * @param userId
     * @param type   购买会员卡1，更改邀请关系2
     * @return
     */
    @Transactional
    @Override
    public ResultInfo rebate(long userId, int type) {
        ResultInfo resultInfo = new ResultInfo();

        if (type != 1 && type != 2) {
            resultInfo.setInfo(-1, "操作类型有误");
            return resultInfo;
        }

        User user = userService.getUserById(userId);
        UserInfo userInfo = userInfoMapper.getUserInfoByUserId(userId);
        if (userInfo == null) {
            resultInfo.setInfo(-1, "用户信息有误");
            return resultInfo;
        }
        if (user.getRoleType() == RoleTypeEnum.BEAUTICIAN.code || user.getRoleType() == RoleTypeEnum.ADVISER.code) {
            resultInfo.setInfo(1, "美容师或者顾问不产生返佣");
            return resultInfo;
        }
        if (userInfo.getSpreadId() == null || userInfo.getSpreadId() < 1) {
            resultInfo.setInfo(1, "一级会员没有邀请人");
            return resultInfo;
        }
        Map<String, String> distributionRule = settingService.listAllDistributionRule();
        User spreadFirstUser = userService.getUserById(userInfo.getSpreadId());
        if (spreadFirstUser.getRoleType() == RoleTypeEnum.ADVISER.code) {
            if (type == 1) {
                // 1.1顾问的直推奖
                UserBonusDetail userBonusDetail = new UserBonusDetail(new Date(), spreadFirstUser.getId(), userId, userInfo.getSpreadId(), AwardTypeEnum.DIRECT_AWARD.code);
                resultInfo = saveBonusDetail(userBonusDetail);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存顾问直推奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
                long detailId = (long) resultInfo.getObj();
                double amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "3:" + AwardTypeEnum.DIRECT_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                UserBonus userBonus = new UserBonus(new Date(), spreadFirstUser.getId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.DIRECT_AWARD.code, detailId, 0, false, null, "顾问直推奖");
                resultInfo = saveBonus(userBonus);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存顾问直推奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }

                resultInfo.setInfo(1, "返佣成功");
                return resultInfo;
            }
            if (type == 2) {
                try {
                    rebateExtOfUpdateInvite(userId);
                } catch (Exception e) {
                    LOG.error("顾问一级修改邀请关系人头奖发放失败--【{}】", e.getMessage());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
            }
        } else if (spreadFirstUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code) {
            UserBonusDetail userBonusDetail = null;
            long detailId = -1;
            double amount = 0.00;
            UserBonus userBonus = null;
            if (type == 1) {
                // 1.2美容师的一级奖
                userBonusDetail = new UserBonusDetail(new Date(), spreadFirstUser.getId(), userId, userInfo.getSpreadId(), AwardTypeEnum.ONE_AWARD.code);
                resultInfo = saveBonusDetail(userBonusDetail);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存美容师的一级奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
                detailId = (long) resultInfo.getObj();
                amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "2:" + AwardTypeEnum.ONE_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                userBonus = new UserBonus(new Date(), spreadFirstUser.getId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.ONE_AWARD.code, detailId, 0, false, null, "美容师一级奖");
                resultInfo = saveBonus(userBonus);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存美容师的一级奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
            }

            // 1.2.1顾问合作奖和人头奖
            UserInfo spreadFirstUserInfo = userInfoMapper.getUserInfoByUserId(spreadFirstUser.getId());
            if (spreadFirstUserInfo.getAdviserId() > 0) {
                if (type == 1) {
                    //合作奖
                    userBonusDetail = new UserBonusDetail(new Date(), spreadFirstUserInfo.getAdviserId(), userId, userInfo.getSpreadId(), AwardTypeEnum.COOPERATE_AWARD.code);
                    resultInfo = saveBonusDetail(userBonusDetail);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存顾问合作奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                    detailId = (long) resultInfo.getObj();
                    amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "3:" + AwardTypeEnum.COOPERATE_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                    userBonus = new UserBonus(new Date(), spreadFirstUserInfo.getAdviserId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.COOPERATE_AWARD.code, detailId, 0, false, null, "顾问合作奖");
                    resultInfo = saveBonus(userBonus);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存顾问合作奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }

                //人头奖
                userBonusDetail = new UserBonusDetail(new Date(), spreadFirstUserInfo.getAdviserId(), userId, userInfo.getSpreadId(), AwardTypeEnum.NUMBER_AWARD.code);
                resultInfo = saveBonusDetail(userBonusDetail);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存顾问人头奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
                if (type == 2) {
                    try {
                        rebateExtOfUpdateInvite(userId);
                    } catch (Exception e) {
                        LOG.error("一级修改邀请关系人头奖发放失败--【{}】", e.getMessage());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }
            }
        } else if (spreadFirstUser.getRoleType() == RoleTypeEnum.MEMBER.code) {
            UserBonusDetail userBonusDetail = null;
            long detailId = -1;
            double amount = 0.00;
            UserBonus userBonus = null;
            // 1.3会员的一级奖
            if (type == 1) {
                userBonusDetail = new UserBonusDetail(new Date(), spreadFirstUser.getId(), userId, userInfo.getSpreadId(), AwardTypeEnum.ONE_AWARD.code);
                resultInfo = saveBonusDetail(userBonusDetail);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存会员一级奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
                detailId = (long) resultInfo.getObj();
                amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "1:" + AwardTypeEnum.ONE_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                userBonus = new UserBonus(new Date(), spreadFirstUser.getId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.ONE_AWARD.code, detailId, 0, false, null, "会员一级奖");
                resultInfo = saveBonus(userBonus);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存会员一级奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
            }

            // 1.3.1会员的双人奖
            int num = userInfoMapper.getUserSpreadsByUserId(spreadFirstUser.getId(), RoleTypeEnum.MEMBER.code);
            if (num == 2) {
                userBonusDetail = new UserBonusDetail(new Date(), spreadFirstUser.getId(), userId, userInfo.getSpreadId(), AwardTypeEnum.DOUBLE_AWARD.code);
                resultInfo = saveBonusDetail(userBonusDetail);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存会员的双人奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
                detailId = (long) resultInfo.getObj();
                amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "1:" + AwardTypeEnum.DOUBLE_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                userBonus = new UserBonus(new Date(), spreadFirstUser.getId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.DOUBLE_AWARD.code, detailId, 0, false, null, "会员双人奖");
                resultInfo = saveBonus(userBonus);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存会员的双人奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
            }

            UserInfo spreadFirstUserInfo = userInfoMapper.getUserInfoByUserId(spreadFirstUser.getId());
            if (spreadFirstUserInfo.getSpreadId() < 1) {
                resultInfo.setInfo(1, "二级会员没有邀请人");
                return resultInfo;
            }

            User spreadSecondUser = userService.getUserById(spreadFirstUserInfo.getSpreadId());
            if (spreadSecondUser.getRoleType() == RoleTypeEnum.ADVISER.code) {
                if (type == 1) {
                    // 1.3.2顾问的二级奖
                    userBonusDetail = new UserBonusDetail(new Date(), spreadSecondUser.getId(), userId, userInfo.getSpreadId(), AwardTypeEnum.TWO_AWARD.code);
                    resultInfo = saveBonusDetail(userBonusDetail);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存顾问的二级奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                    detailId = (long) resultInfo.getObj();
                    amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "3:" + AwardTypeEnum.TWO_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                    userBonus = new UserBonus(new Date(), spreadSecondUser.getId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.TWO_AWARD.code, detailId, 0, false, null, "顾问二级奖");
                    resultInfo = saveBonus(userBonus);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存顾问的二级奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }
            } else if (spreadSecondUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code) {
                if (type == 1) {
                    // 1.3.3美容师的二级奖
                    userBonusDetail = new UserBonusDetail(new Date(), spreadSecondUser.getId(), userId, userInfo.getSpreadId(), AwardTypeEnum.TWO_AWARD.code);
                    resultInfo = saveBonusDetail(userBonusDetail);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存美容师的二级奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                    detailId = (long) resultInfo.getObj();
                    amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "2:" + AwardTypeEnum.TWO_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                    userBonus = new UserBonus(new Date(), spreadSecondUser.getId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.TWO_AWARD.code, detailId, 0, false, null, "美容师二级奖");
                    resultInfo = saveBonus(userBonus);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存美容师的二级奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }

                // 1.3.3.1顾问人头奖
                UserInfo spreadSecondUserInfo = userInfoMapper.getUserInfoByUserId(spreadFirstUser.getId());
                if (spreadSecondUserInfo.getAdviserId() > 0) {
                    //人头奖
                    userBonusDetail = new UserBonusDetail(new Date(), spreadSecondUserInfo.getAdviserId(), userId, userInfo.getSpreadId(), AwardTypeEnum.NUMBER_AWARD.code);
                    resultInfo = saveBonusDetail(userBonusDetail);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存顾问人头奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                    if (type == 2) {
                        try {
                            rebateExtOfUpdateInvite(userId);
                        } catch (Exception e) {
                            LOG.error("二级修改邀请关系人头奖发放失败--【{}】", e.getMessage());
                            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                        }
                    }
                }
            } else if (spreadSecondUser.getRoleType() == RoleTypeEnum.MEMBER.code) {
                if (type == 1) {
                    // 1.3.4会员二级奖
                    userBonusDetail = new UserBonusDetail(new Date(), spreadSecondUser.getId(), userId, userInfo.getSpreadId(), AwardTypeEnum.TWO_AWARD.code);
                    resultInfo = saveBonusDetail(userBonusDetail);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存会员的二级奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                    detailId = (long) resultInfo.getObj();
                    amount = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "1:" + AwardTypeEnum.TWO_AWARD.code), JSONArray.class).getJSONObject(0).getInteger("bonus").doubleValue();
                    userBonus = new UserBonus(new Date(), spreadSecondUser.getId(), GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.TWO_AWARD.code, detailId, 0, false, null, "会员二级奖");
                    resultInfo = saveBonus(userBonus);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存会员的二级奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }

                UserInfo spreadSecondUserInfo = userInfoMapper.getUserInfoByUserId(spreadSecondUser.getId());
                if (spreadSecondUserInfo.getSpreadId() < 1) {
                    resultInfo.setInfo(1, "三级会员没有邀请人");
                    return resultInfo;
                }

                if (type == 2) {
                    // 人头奖
                    if (spreadSecondUserInfo.getBeauticianId() > 0) {
                        userBonusDetail = new UserBonusDetail(new Date(), spreadSecondUserInfo.getBeauticianId(), userId, userInfo.getSpreadId(), AwardTypeEnum.NUMBER_AWARD.code);
                        resultInfo = saveBonusDetail(userBonusDetail);
                        if (resultInfo.getCode() < 1) {
                            LOG.info("保存美容师人头奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                            resultInfo.setInfo(-1, resultInfo.getMsg());
                            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                        }
                    }
                    if (spreadSecondUserInfo.getAdviserId() > 0) {
                        userBonusDetail = new UserBonusDetail(new Date(), spreadSecondUserInfo.getAdviserId(), userId, userInfo.getSpreadId(), AwardTypeEnum.NUMBER_AWARD.code);
                        resultInfo = saveBonusDetail(userBonusDetail);
                        if (resultInfo.getCode() < 1) {
                            LOG.info("保存顾问人头奖明细时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                            resultInfo.setInfo(-1, resultInfo.getMsg());
                            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                        }
                    }
                    try {
                        rebateExtOfUpdateInvite(userId);
                    } catch (Exception e) {
                        LOG.error("三级修改邀请关系人头奖发放失败--【{}】", e.getMessage());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                } else {
                    // 1.3.4美容师的人头奖和顾问的人头奖
                    try {
                        rebateExt(spreadSecondUserInfo.getUserId(), userId, userInfo.getSpreadId());
                    } catch (Exception e) {
                        LOG.error("美容师的人头奖和顾问的人头奖调用异常");
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                }
                resultInfo.setInfo(1, "返佣成功");
            } else {
                resultInfo.setInfo(1, "邀请人是顾客不产生返佣不产生返佣");
            }
        } else {
            resultInfo.setInfo(1, "邀请人是顾客不产生返佣不产生返佣");
        }
        resultInfo.setInfo(1, "不产生返佣，直接返回");
        return resultInfo;
    }

    /**
     * 计算人头奖
     *
     * @return
     */
    @Transactional
    @Override
    public ResultInfo calcNumberAward() {
        final ResultInfo resultInfo = new ResultInfo();

        List<Map<String, Long>> mapList = userBonusDetailMapper.listAllUserLastMonthBonusDetailGroupByType(AwardTypeEnum.NUMBER_AWARD.code);
        if (mapList == null || mapList.isEmpty() || mapList.size() < 1) {
            resultInfo.setInfo(1, "本月暂无数据");
            return resultInfo;
        }

        mapList.forEach(x -> {
            Long userId = x.get("userId");
            User user = userService.getUserById(userId);
            Integer number = x.get("number").intValue();
            double amount = 0.00;
            UserBonus userBonus = null;
            if (user.getRoleType() == RoleTypeEnum.ADVISER.code) {
                amount = calcNumberAwardAmount(number, RoleTypeEnum.ADVISER);
                userBonus = new UserBonus(new Date(), userId, GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.NUMBER_AWARD.code, -1L, number, false, null, "顾问人头奖");
            } else if (user.getRoleType() == RoleTypeEnum.BEAUTICIAN.code) {
                amount = calcNumberAwardAmount(number, RoleTypeEnum.BEAUTICIAN);
                userBonus = new UserBonus(new Date(), userId, GenerateCode.getOrderNo(BizzTypeEnum.RAKE_BACK.getPrefix()), amount, AwardTypeEnum.NUMBER_AWARD.code, -1L, number, false, null, "美容师人头奖");
            }
            if (userBonus != null && amount > 0) {
                ResultInfo info = saveBonus(userBonus);
                if (info.getCode() < 1) {
                    LOG.info("计算人头奖时保存人头奖--【{}】--【{}】", info.getCode(), info.getMsg());
                    throw new ServiceRuntimeException(info.getMsg(), true);
                }
            }
        });

        resultInfo.setInfo(1, "计算成功");
        return resultInfo;
    }

    /**
     * 返还到期奖金
     *
     * @return
     */
    @Transactional
    @Override
    public ResultInfo returnExpireAward() {
        ResultInfo resultInfo = new ResultInfo();

        String withdrawTime = settingService.getSettingValueByKey(SettingKeyConstant.WITHDRAW_TIME);
        List<Map<String, Object>> mapList = userBonusMapper.listReturnExpire(Integer.parseInt(withdrawTime));
        if (mapList == null || mapList.isEmpty() || mapList.size() < 1) {
            resultInfo.setInfo(1, "暂无数据");
            return resultInfo;
        }

        mapList.forEach(x -> {
            long userId = (Long) x.get("userId");
            double amount = ((BigDecimal) x.get("amount")).doubleValue();
            int rows = userFundsService.updateAddAvailableBalance(userId, amount);
            if (rows < 1) {
                LOG.info("返还到期奖金时更新可提现金额--【{}】--【{}】", "-1", "更新可提现金额失败");
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }
        });

        int rows = updateReturnExpire(Integer.parseInt(withdrawTime));
        if (rows < 1) {
            resultInfo.setInfo(-1, "更新所有到期佣金标识和时间失败");
            LOG.info("返还到期奖金--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
        }

        return resultInfo;
    }

    /**
     * 更新所有到期佣金标识和时间
     *
     * @param period
     * @return
     */
    @Transactional
    @Override
    public int updateReturnExpire(int period) {
        return userBonusMapper.updateReturnExpire(period);
    }

    /**
     * 计算人头奖金额
     *
     * @param number
     * @param roleTypeEnum
     * @return
     */
    private double calcNumberAwardAmount(int number, RoleTypeEnum roleTypeEnum) {
        double amount = 0.00;
        AtomicReference<Double> atomicReference = new AtomicReference<>(amount);
        JSONArray array = null;
        Map<String, String> distributionRule = settingService.listAllDistributionRule();
        if (roleTypeEnum == RoleTypeEnum.ADVISER) {
            array = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "3:" + AwardTypeEnum.NUMBER_AWARD.code), JSONArray.class);
        } else if (roleTypeEnum == RoleTypeEnum.BEAUTICIAN) {
            array = FastJsonUtil.parseObj(distributionRule.get(SystemRedisKeyConstant.SALE_RULE_PREFIX + "2:" + AwardTypeEnum.NUMBER_AWARD.code), JSONArray.class);
        }
        array.forEach(x -> {
            JSONObject json = (JSONObject) x;
            Integer inviteFrom = json.getInteger("inviteFrom");
            Integer inviteTo = json.getInteger("inviteTo");
            Integer from = json.getInteger("from");
            Integer to = json.getInteger("to");
            Double bonus = json.getDouble("bonus");
            if (inviteTo == -1) {
                inviteTo = number;
                to = number;
            }
            if (number > inviteTo) {
                for (int i = from; i <= to; i++) {
                    atomicReference.set(atomicReference.get() + bonus);
                }
            } else {
                for (int i = from; i <= number; i++) {
                    atomicReference.set(atomicReference.get() + bonus);
                }
            }
        });
        amount = atomicReference.get();
        return amount;
    }

    /**
     * 发放佣金扩展
     *
     * @param userId
     * @param relationUserId
     * @param relationUserSpreadId
     */
    @Transactional
    protected void rebateExt(long userId, long relationUserId, long relationUserSpreadId) {
        UserInfo userInfo = userInfoMapper.getUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getSpreadId() == null || userInfo.getSpreadId() < 1) {
            return;
        }
        User userFirst = userService.getUserById(userInfo.getSpreadId());
        if (userFirst.getRoleType() == RoleTypeEnum.ADVISER.code) {
            UserBonusDetail userBonusDetail = new UserBonusDetail(new Date(), userFirst.getId(), relationUserId, relationUserSpreadId, AwardTypeEnum.NUMBER_AWARD.code);
            ResultInfo resultInfo = saveBonusDetail(userBonusDetail);
            if (resultInfo.getCode() < 1) {
                LOG.info("保存顾问人头奖奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                resultInfo.setInfo(-1, resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }
        } else if (userFirst.getRoleType() == RoleTypeEnum.BEAUTICIAN.code) {
            UserBonusDetail userBonusDetail = new UserBonusDetail(new Date(), userFirst.getId(), relationUserId, relationUserSpreadId, AwardTypeEnum.NUMBER_AWARD.code);
            ResultInfo resultInfo = saveBonusDetail(userBonusDetail);
            if (resultInfo.getCode() < 1) {
                LOG.info("保存美容师人头奖奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                resultInfo.setInfo(-1, resultInfo.getMsg());
                throw new ServiceRuntimeException(resultInfo.getMsg(), true);
            }

            UserInfo userInfoFirst = userInfoMapper.getUserInfoByUserId(userFirst.getId());
            if (userInfoFirst.getAdviserId() > 0) {
                userBonusDetail = new UserBonusDetail(new Date(), userInfoFirst.getAdviserId(), relationUserId, relationUserSpreadId, AwardTypeEnum.NUMBER_AWARD.code);
                resultInfo = saveBonusDetail(userBonusDetail);
                if (resultInfo.getCode() < 1) {
                    LOG.info("保存顾问人头奖奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                    resultInfo.setInfo(-1, resultInfo.getMsg());
                    throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                }
            }
        } else {
            rebateExt(userInfo.getSpreadId(), relationUserId, relationUserSpreadId);
        }
    }

    /**
     * 发放佣金扩展--修改邀请关系
     *
     * @param userId
     */
    @Transactional
    protected void rebateExtOfUpdateInvite(long userId) {
        UserInfo userInfo = userInfoMapper.getUserInfoByUserId(userId);
        if (userInfo == null) {
            return;
        }
        List<UserInfo> userInfos = userInfoMapper.listUserInfoBySpreadId(userId);
        if (userInfos == null || userInfos.size() < 1 || userInfos.isEmpty()) {
            return;
        }
        User parentUser = userService.getUserById(userInfo.getSpreadId());
        if (parentUser.getRoleType() == RoleTypeEnum.ADVISER.code) {
            userInfos.forEach(x -> {
                User user = userService.getUserById(x.getUserId());
                if (user.getRoleType() != RoleTypeEnum.CUSTOMER.code) {
                    rebateExtOfUpdateInvite(x.getUserId());
                }
            });
        } else if (parentUser.getRoleType() == RoleTypeEnum.BEAUTICIAN.code) {
            userInfos.forEach(x -> {
                User user = userService.getUserById(x.getUserId());
                if (user.getRoleType() != RoleTypeEnum.CUSTOMER.code) {
                    UserBonusDetail userBonusDetail = new UserBonusDetail(new Date(), userInfo.getAdviserId(), x.getUserId(), x.getSpreadId(), AwardTypeEnum.NUMBER_AWARD.code);
                    ResultInfo resultInfo = saveBonusDetail(userBonusDetail);
                    if (resultInfo.getCode() < 1) {
                        LOG.info("保存二级邀请人为美容师的顾问人头奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                        resultInfo.setInfo(-1, resultInfo.getMsg());
                        throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                    }
                    rebateExtOfUpdateInvite(x.getUserId());
                }
            });
        } else {
            userInfos.forEach(x -> {
                User user = userService.getUserById(x.getUserId());
                if (user.getRoleType() != RoleTypeEnum.CUSTOMER.code) {
                    UserBonusDetail userBonusDetail = null;
                    ResultInfo resultInfo = null;
                    UserInfo info = userInfoMapper.getUserInfoByUserId(user.getId());
                    if (info.getBeauticianId() > 0) {
                        userBonusDetail = new UserBonusDetail(new Date(), userInfo.getBeauticianId(), x.getUserId(), x.getSpreadId(), AwardTypeEnum.NUMBER_AWARD.code);
                        resultInfo = saveBonusDetail(userBonusDetail);
                        if (resultInfo.getCode() < 1) {
                            LOG.info("保存三级邀请人为美容师的美容师人头奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                            resultInfo.setInfo(-1, resultInfo.getMsg());
                            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                        }
                    }
                    if (info.getAdviserId() > 0) {
                        userBonusDetail = new UserBonusDetail(new Date(), userInfo.getAdviserId(), x.getUserId(), x.getSpreadId(), AwardTypeEnum.NUMBER_AWARD.code);
                        resultInfo = saveBonusDetail(userBonusDetail);
                        if (resultInfo.getCode() < 1) {
                            LOG.info("保存三级邀请人为顾问的顾问人头奖时--【{}】--【{}】", resultInfo.getCode(), resultInfo.getMsg());
                            resultInfo.setInfo(-1, resultInfo.getMsg());
                            throw new ServiceRuntimeException(resultInfo.getMsg(), true);
                        }
                    }
                    rebateExtOfUpdateInvite(x.getUserId());
                }
            });
        }
    }
}
