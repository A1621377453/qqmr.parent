package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.bo.EmployeeSearchBO;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.base.util.file.FileUtil;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.enums.AwardTypeEnum;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.common.enums.SexEnum;
import com.qingqingmr.qqmr.common.util.DateUtil;
import com.qingqingmr.qqmr.common.util.GenerateCode;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.dao.*;
import com.qingqingmr.qqmr.domain.bean.EmployeeVO;
import com.qingqingmr.qqmr.domain.bean.OrderDetailVO;
import com.qingqingmr.qqmr.domain.bean.UserFundsVO;
import com.qingqingmr.qqmr.domain.bean.UserSaveVO;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.EmployeeService;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工列表实现类
 *
 * @author crn
 * @datetime 2018-07-05 11:16:50
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserBonusMapper userBonusMapper;

    @Autowired
    private UserFundsMapper userFundsMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private EventSupervisorService eventSupervisorService;

    @Autowired
    private SpringContextProperties contextProperties;

    /**
     * 获取顾问列表
     *
     * @param employeeSearchBo
     * @param request
     * @return
     */
    @Override
    public PageResult<EmployeeVO> listCounselors(EmployeeSearchBO employeeSearchBo, HttpServletRequest request) {
        return getEmployeeVoPageResult(employeeSearchBo, request);
    }

    /**
     * 根据门店id获取最大员工编号
     *
     * @param storeId
     * @param storeFirstSpell
     * @return
     * @author crn
     */
    @Override
    public ResultInfo getMaxEmployeeSno(long storeId, String storeFirstSpell) {
        ResultInfo resultInfo = new ResultInfo();
        String maxEmployeeSno = employeeMapper.getMaxEmployeeSno(storeId);
        String sno = null;
        if (StringUtils.isBlank(maxEmployeeSno)) {
            // 拼接员工编号
            sno = storeFirstSpell + "-000001";
            resultInfo.setInfo(1, "查询成功", sno);
            return resultInfo;
        }
        String[] split = StringUtils.split(maxEmployeeSno, "-");
        Integer tempSno = Integer.parseInt(split[1]) + 1;
        if (tempSno >= 1 && tempSno <= 9) {
            sno = split[0] + "-00000" + tempSno;
        } else if (tempSno >= 10 && tempSno <= 99) {
            sno = split[0] + "-0000" + tempSno;
        } else if (tempSno >= 100 && tempSno <= 999) {
            sno = split[0] + "-000" + tempSno;
        } else if (tempSno >= 1000 && tempSno <= 9999) {
            sno = split[0] + "-00" + tempSno;
        } else if (tempSno >= 10000 && tempSno <= 99999) {
            sno = split[0] + "-0" + tempSno;
        } else if (tempSno == 100000) {
            sno = split[0] + "-" + tempSno;
        }
        resultInfo.setInfo(1, "查询成功", sno);
        return resultInfo;
    }

    /**
     * 根据员工id获取顾问详细信息
     *
     * @param id
     * @param request
     * @return
     * @author crn
     */
    @Override
    public ResultInfo getCounselorDetailById(Long id, HttpServletRequest request) {
        return getBaseEmployeeDetail(id, RoleTypeEnum.ADVISER.code, request);
    }

    /**
     * 获取员工信息
     *
     * @param id
     * @param role
     * @return
     * @author crn
     */
    private ResultInfo getBaseEmployeeDetail(Long id, Integer role, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 根据id查询员工详情
        EmployeeVO employeeDetail = employeeMapper.getEmployeeById(id);
        Integer roleType = employeeDetail.getRoleType();
        if (RoleTypeEnum.ADVISER.code == role) {
            if (roleType == Employee.RoleTypeEnum.COUNSELOR.code) {
                resultMap.put("employeeDetail", employeeDetail);
            } else {
                resultInfo.setInfo(-1, "id信息有误");
                return resultInfo;
            }
        } else if (RoleTypeEnum.BEAUTICIAN.code == role) {
            if (roleType == Employee.RoleTypeEnum.BEAUTICIAN.code) {
                resultMap.put("employeeDetail", employeeDetail);
            } else {
                resultInfo.setInfo(-1, "id信息有误");
                return resultInfo;
            }
        }
        User user = userMapper.getUserByEmployeeId(id);
        if (null == user) {
            LOG.error("用户信息为空");
            resultInfo.setInfo(-1, "用户信息为空");
            return resultInfo;
        }
        Long userId = user.getId();

        // 获取用户资金信息
        UserFundsVO userFunds = userFundsMapper.getUserFundsByUserId(userId);
        resultMap.put("userFunds", userFunds);

        // 用户累计赚取金额
        Double userAmountByUserId = userBonusMapper.getUserAmountByUserId(userId);
        resultMap.put("userAmountByUserId", userAmountByUserId);

        // 获取用户本月直接赚取金额
        Double userCurMonDirectAmount = 0.0;
        if (RoleTypeEnum.ADVISER.code == role) {
            userCurMonDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId, role,
                    AwardTypeEnum.DIRECT_AWARD.code);
        } else if (RoleTypeEnum.BEAUTICIAN.code == role) {
            userCurMonDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId, role,
                    AwardTypeEnum.ONE_AWARD.code);
        }
        resultMap.put("userCurMonDirectAmount", userCurMonDirectAmount);
        // 获取用户本月间接赚取金额
        Double userCurMonInDirectAmount = userBonusMapper.getUserCurMonAmountByUserId(userId, role,
                AwardTypeEnum.NO_DIRECT_AWARD.code);
        resultMap.put("userCurMonInDirectAmount", userCurMonInDirectAmount);

        if (RoleTypeEnum.ADVISER.code == role) {

            // 获取顾问累计邀请会员
            int counselorSpreadMember = userInfoMapper.getUserSpreadsByUserId(userId, RoleTypeEnum.MEMBER.code);
            resultMap.put("counselorSpreadMember", counselorSpreadMember);

            // 获取顾问本月邀请会员
            int counselorCurMonSpreadMember = userInfoMapper.getUserCurMonSpreadsByUserId(userId,
                    RoleTypeEnum.MEMBER.code);
            resultMap.put("counselorCurMonSpreadMember", counselorCurMonSpreadMember);

            // 获取顾问本月邀请顾问
            int counselorCurMonSpreadCustomer = userInfoMapper.getUserCurMonSpreadsByUserId(userId,
                    RoleTypeEnum.CUSTOMER.code);
            resultMap.put("counselorCurMonSpreadCustomer", counselorCurMonSpreadCustomer);
        } else if (RoleTypeEnum.BEAUTICIAN.code == role) {
            // 获取美容师累计邀请会员
            int beauticianSpreadMember = userInfoMapper.getUserSpreadsByUserId(userId, RoleTypeEnum.MEMBER.code);
            resultMap.put("beauticianSpreadMember", beauticianSpreadMember);

            // 获取美容师本月邀请会员
            int beauticianCurMonSpreadMember = userInfoMapper.getUserCurMonSpreadsByUserId(userId,
                    RoleTypeEnum.MEMBER.code);
            resultMap.put("beauticianCurMonSpreadMember", beauticianCurMonSpreadMember);

            // 获取美容师本月邀请顾客
            int beauticianCurMonSpreadCustomer = userInfoMapper.getUserCurMonSpreadsByUserId(userId,
                    RoleTypeEnum.CUSTOMER.code);
            resultMap.put("beauticianCurMonSpreadCustomer", beauticianCurMonSpreadCustomer);

            // 获取美容师所属顾问
            EmployeeVO counselorOfBeautician = employeeMapper.getCounselorOfBeautician(id);
            resultMap.put("counselorOfBeautician", counselorOfBeautician);
        }

        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request,
                    RoleTypeEnum.ADVISER.code == role
                            ? EventSupervisor.SupervisorEventEnum.SUPERVISOR_ADVISOR_SHOW_DETAIL
                            : EventSupervisor.SupervisorEventEnum.SUPERVISOR_BEAUTICIAN_SHOW_DETAIL);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        resultInfo.setInfo(1, "查询成功", resultMap);
        return resultInfo;
    }

    /**
     * 获取美容师详情
     *
     * @param id
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method getBeauticianDetailById
     * @datetime 2018/7/9 14:19
     * @author crn
     */
    @Override
    public ResultInfo getBeauticianDetailById(Long id, HttpServletRequest request) {
        return getBaseEmployeeDetail(id, RoleTypeEnum.BEAUTICIAN.code, request);

    }

    /**
     * 获取顾问当月佣金列表
     *
     * @param userId   用户id
     * @param currPage
     * @param pageSize
     * @return
     * @author crn
     */
    @Override
    public PageResult<Map<String, Object>> listCounselorCurMonBonus(Long userId, Integer currPage, Integer pageSize) {
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
                    x.put("inviteeName", Security.decodeHex((String) v));
                }
            });
        });
        result.setPage(userCurMonBonus);
        result.setTotalCount(page.getTotal());
        return result;
    }

    /**
     * 根据员工id获取员工信息
     *
     * @param id
     * @return
     * @author crn
     */
    @Override
    public ResultInfo getEmployeeInfo(Long id) {
        ResultInfo resultInfo = new ResultInfo();
        Employee employee = employeeMapper.getEmployeeBaseInfoById(id);
        resultInfo.setInfo(1, "查询成功", employee);
        return resultInfo;
    }

    /**
     * 上传员工头像
     *
     * @param img
     * @param id  员工id
     * @return
     */
    @Transactional
    @Override
    public ResultInfo imageUpload(MultipartFile img, Long id) {
        ResultInfo resultInfo = FileUtil.uploadImage(img);
        if (null == resultInfo || resultInfo.getCode() <= 0) {
            LOG.error("上传员工头像,上传失败");
            resultInfo.setInfo(-1, "上传失败");
            return resultInfo;
        }
        Map<String, Object> obj = (Map<String, Object>) resultInfo.getObj();
        if (null != id && 0 != id) {
            String photo = (String) obj.get("name");
            ResultInfo result = getEmployeeInfo(id);
            if (null == result || result.getCode() <= 0) {
                LOG.error("上传员工头像,获取员工信息失败,员工id--【{}】", id);
                resultInfo.setInfo(-1, "上传失败");
                return resultInfo;
            }
            Employee employee = (Employee) result.getObj();
            employee.setPhoto(photo);
            int update = employeeMapper.updateEmployeeByIdSelective(employee);
            if (update < 0) {
                LOG.error("上传员工头像,更新头像异常--【{}】", update);
                throw new ServiceRuntimeException("上传员工头像,更新头像异常", true);
            }
        }
        resultInfo.setInfo(1, "上传成功", obj);
        return resultInfo;
    }

    /**
     * 添加顾问
     *
     * @param employeeVO
     * @param request
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method saveCounselor
     * @datetime 2018/7/6 10:27
     * @author crn
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultInfo saveCounselor(EmployeeVO employeeVO, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = saveBaseEmployee(employeeVO, Employee.RoleTypeEnum.COUNSELOR.code, "save");
            eventSupervisorService.saveEventSupervisor(request,
                    EventSupervisor.SupervisorEventEnum.SUPERVISOR_ADVISOR_ADD);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("添加顾问失败--【{}】", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInfo.setInfo(-1, e.getMessage());
            return resultInfo;
        }
        return resultInfo;
    }

    /**
     * 添加美容师
     *
     * @param employeeVO
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultInfo saveBeautician(EmployeeVO employeeVO, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = saveBaseEmployee(employeeVO, Employee.RoleTypeEnum.BEAUTICIAN.code, "save");
            eventSupervisorService.saveEventSupervisor(request,
                    EventSupervisor.SupervisorEventEnum.SUPERVISOR_BEAUTICIAN_ADD);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("添加美容师失败--【{}】", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInfo.setInfo(-1, e.getMessage());
            return resultInfo;
        }
        return resultInfo;
    }

    /**
     * 保存员工
     *
     * @param employeeVO
     * @param role
     * @param type
     * @return
     * @throws ParseException
     * @author crn
     */
    @Transactional(rollbackFor = Exception.class)
    ResultInfo saveBaseEmployee(EmployeeVO employeeVO, int role, String type) throws ParseException {
        ResultInfo resultInfo = validateCounselorInfo(employeeVO, role, type);
        if (resultInfo.getCode() < 0) {
            return resultInfo;
        }

        Employee employee = (Employee) resultInfo.getObj();
        resultInfo.setObj(null);
        // 添加员工表必要信息
        if (role == Employee.RoleTypeEnum.BEAUTICIAN.code) {
            employee.setRoleType(Employee.RoleTypeEnum.BEAUTICIAN.code);
            employee.setParentId(employee.getParentId());
        } else if (role == Employee.RoleTypeEnum.COUNSELOR.code) {
            employee.setRoleType(Employee.RoleTypeEnum.COUNSELOR.code);
            employee.setParentId(-1L);
        }

        int saveEmployee = employeeMapper.saveEmployee(employee);
        if (saveEmployee > 0) {
            String mobile = employee.getMobile();
            User user = userMapper.getUserByMobile(mobile);
            int saveUser = 0;
            if (null == user) {
                String userName = SystemConstant.NAME_PREFIX + GenerateCode.genCode(8);
                UserSaveVO userSaveVO = new UserSaveVO(null, new Date(), userName, mobile, null, employee.getSex(),
                        null, null, 0, employee.getId(), null);
                if (role == Employee.RoleTypeEnum.BEAUTICIAN.code) {
                    userSaveVO.setRoleType(RoleTypeEnum.BEAUTICIAN.code);
                } else if (role == Employee.RoleTypeEnum.COUNSELOR.code) {
                    userSaveVO.setRoleType(RoleTypeEnum.ADVISER.code);
                }
                saveUser = userMapper.saveUser(userSaveVO);
                if (saveUser <= 0) {
                    LOG.error("添加顾问功能，保存用户表失败，code【{}】", saveUser);
                    throw new ServiceRuntimeException("添加顾问功能，保存用户表失败", true);
                }
                user = new User();
                user.setId(userSaveVO.getId());
            } else {
                Integer roleType = user.getRoleType();
                if (RoleTypeEnum.MEMBER.code == roleType || RoleTypeEnum.BEAUTICIAN.code == roleType || RoleTypeEnum.ADVISER.code == roleType) {
                    LOG.error("该手机号已使用");
                    throw new ServiceRuntimeException("该手机号已使用", true);
                } else if (RoleTypeEnum.CUSTOMER.code == roleType) {
                    // 关闭用户未付款的订单
                    List<OrderDetailVO> orderDetailVOS = orderMapper.listOrders(user.getId());
                    if (null != orderDetailVOS && orderDetailVOS.size() > 0) {
                        for (OrderDetailVO orderDetailVO : orderDetailVOS) {
                            if (null != orderDetailVO.getStatus()
                                    && Order.DealStatusEnum.OBLIGATION.code == orderDetailVO.getStatus()) {
                                resultInfo = orderService.updateOrderCancelInfo(orderDetailVO.getId(),
                                        Order.CancelTypeEnum.automatic.code, Order.DealStatusEnum.CLOSE.code);
                                if (null != resultInfo && resultInfo.getCode() < 0) {
                                    LOG.error("关闭用户未付款的订单--code【()】--msg【()】", resultInfo.getCode(), resultInfo.getMsg());
                                    throw new ServiceRuntimeException("关闭用户未付款的订单", true);
                                }
                            }
                        }
                    }
                    user.setMobile(mobile);
                    user.setSex(employee.getSex());
                    if (role == Employee.RoleTypeEnum.BEAUTICIAN.code) {
                        user.setRoleType(RoleTypeEnum.BEAUTICIAN.code);
                    } else if (role == Employee.RoleTypeEnum.COUNSELOR.code) {
                        user.setRoleType(RoleTypeEnum.ADVISER.code);
                    }
                    user.setEmployeeId(employee.getId());

                    saveUser = userMapper.updateUserByIdSelective(user);
                    if (saveUser <= 0) {
                        LOG.error("添加顾问功能，保存用户表失败，code【{}】", saveUser);
                        throw new ServiceRuntimeException("添加顾问功能，保存用户表失败", true);
                    }
                }
            }

            UserInfo userInfo = userInfoMapper.getUserInfoByMobile(mobile);
            int saveUserInfo = 0;
            if (null == userInfo) {
                userInfo = new UserInfo();
                userInfo.setTime(new Date());
                userInfo.setUserId(user.getId());
                userInfo.setMobile(mobile);
                userInfo.setStoreId(employee.getStoreId());
                userInfo.setSpreadId(-1L);
                userInfo.setSpreadTime(null);
                userInfo.setBeauticianId(0L);
                if (role == Employee.RoleTypeEnum.BEAUTICIAN.code) {
                    User parentUser = userMapper.getUserByEmployeeId(employee.getParentId());
                    if (null == parentUser) {
                        LOG.error("添加顾问功能，查询美容师所属顾问信息失败");
                        throw new ServiceRuntimeException("添加顾问功能，查询美容师所属顾问信息失败", true);
                    }
                    userInfo.setAdviserId(parentUser.getId());
                } else {
                    userInfo.setAdviserId(0L);
                }
                // 保存到用户信息表
                saveUserInfo = userInfoMapper.saveUserInfo(userInfo);
            } else {
                userInfo.setSpreadId(0L);
                userInfo.setSpreadTime(null);
                saveUserInfo = userInfoMapper.updateUserInfoByIdSelective(userInfo);
            }
            if (saveUserInfo <= 0) {
                LOG.error("添加顾问功能，保存用户信息失败，code【{}】", saveUserInfo);
                throw new ServiceRuntimeException("添加顾问功能，保存用户信息失败", true);
            }

            UserFunds userFunds = userFundsMapper.getUserFundsByUserId(user.getId());
            int saveUserFunds = 0;
            if (null == userFunds) {
                // 保存用户资金表信息
                userFunds = new UserFunds();
                userFunds.setTime(new Date());
                userFunds.setUserId(user.getId());
                userFunds.setBalance(0.00);
                userFunds.setAvailableBalance(0.00);
                userFunds.setFreeze(0.00);
                userFunds.setSign(Security.decodeFunds(user.getId(), 0.00, 0.00, contextProperties.getMd5Key()));

                saveUserFunds = userFundsMapper.saveUserFunds(userFunds);
                if (saveUserFunds <= 0) {
                    LOG.error("添加顾问功能，保存用户资金表信息失败，code【{}】", saveUserFunds);
                    throw new ServiceRuntimeException("添加顾问功能，保存用户资金表信息失败", true);
                }
            }

            resultInfo.setInfo(1, "添加成功");
            return resultInfo;
        }

        LOG.error("添加顾问失败，code【{}】", saveEmployee);
        throw new ServiceRuntimeException("添加顾问失败", true);
    }

    /**
     * 编辑更新顾问信息
     *
     * @param employeeVO
     * @param request
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method updateCounselor
     * @datetime 2018/7/6 20:37
     * @author crn
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultInfo updateCounselor(EmployeeVO employeeVO, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = updateBaseEmploee(employeeVO, RoleTypeEnum.ADVISER.code, "update");
            eventSupervisorService.saveEventSupervisor(request,
                    EventSupervisor.SupervisorEventEnum.SUPERVISOR_ADVISOR_PERSON_EDIT);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("编辑顾问信息--【{}】", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInfo.setInfo(-1, e.getMessage());
            return resultInfo;
        }
        return resultInfo;
    }

    /**
     * 编辑更新美容师信息
     *
     * @param employeeVO
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultInfo updateBeautician(EmployeeVO employeeVO, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = updateBaseEmploee(employeeVO, RoleTypeEnum.BEAUTICIAN.code, "update");
            eventSupervisorService.saveEventSupervisor(request,
                    EventSupervisor.SupervisorEventEnum.SUPERVISOR_BEAUTICIAN_PERSON_EDIT);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("编辑美容师信息--【{}】", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInfo.setInfo(-1, e.getMessage());
            return resultInfo;
        }
        return resultInfo;
    }

    /**
     * 编辑员工
     *
     * @param employeeVO
     * @return
     * @throws ParseException
     * @author crn
     */
    @Transactional(rollbackFor = Exception.class)
    ResultInfo updateBaseEmploee(EmployeeVO employeeVO, int role, String type) throws ParseException {
        ResultInfo resultInfo = validateCounselorInfo(employeeVO, role, type);
        if (resultInfo.getCode() < 0) {
            return resultInfo;
        }
        Employee employee = (Employee) resultInfo.getObj();
        resultInfo.setObj(null);
        // 添加员工表必要信息
        if (RoleTypeEnum.BEAUTICIAN.code == role) {
            employee.setParentId(employee.getParentId());
        }

        int updateEmployee = employeeMapper.updateEmployeeByIdSelective(employee);
        if (updateEmployee > 0) {
            User userByEmployeeId = userMapper.getUserByEmployeeId(employee.getId());
            User user = new User();
            user.setId(userByEmployeeId.getId());
            user.setMobile(employee.getMobile());
            user.setSex(employee.getSex());
            int updateUserById = userMapper.updateUserByIdSelective(user);
            if (updateUserById <= 0) {
                LOG.error("编辑顾问功能，更新用户表信息失败--code【{}】", updateUserById);
                throw new ServiceRuntimeException("编辑顾问功能，更新用户表信息失败", true);
            }

            UserInfo userInfoByUserId = userInfoMapper.getUserInfoByUserId(userByEmployeeId.getId());
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userInfoByUserId.getId());
            userInfo.setMobile(employee.getMobile());
            userInfo.setStoreId(employee.getStoreId());
            int updateUserInfo = userInfoMapper.updateUserInfoByIdSelective(userInfo);
            if (updateUserInfo <= 0) {
                LOG.error("编辑顾问功能，更新用户详细信息失败--code【{}】", updateUserInfo);
                throw new ServiceRuntimeException("编辑顾问功能，更新用户详细信息失败", true);
            }

            if (updateUserById * updateUserInfo > 0) {
                resultInfo.setInfo(1, "更新成功");
                return resultInfo;
            }
        }
        LOG.error("编辑顾问功能，更新" + RoleTypeEnum.getEnum(role).value + "信息失败--code【{}】", updateEmployee);
        throw new ServiceRuntimeException("更新" + RoleTypeEnum.getEnum(role).value + "失败", true);
    }

    /**
     * 获取美容师列表
     *
     * @param employeeSearchBo
     * @return com.qingqingmr.qqmr.common.PageResult<com.qingqingmr.qqmr.domain.bean.EmployeeVO>
     * @method listBeauticians
     * @datetime 2018/7/7 12:24
     * @author crn
     */
    @Override
    public PageResult<EmployeeVO> listBeauticians(EmployeeSearchBO employeeSearchBo, HttpServletRequest request) {

        return getEmployeeVoPageResult(employeeSearchBo, request);
    }

    /**
     * 根据门店id获取该门店顾问列表
     *
     * @param storeId
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method getCounselorsOfStore
     * @datetime 2018/7/7 12:42
     * @author crn
     */
    @Override
    public ResultInfo getCounselorsOfStore(Long storeId) {
        ResultInfo resultInfo = new ResultInfo();

        List<Employee> employees = employeeMapper.getCounselorsOfStore(storeId);
        resultInfo.setInfo(1, "查询成功", employees);
        return resultInfo;
    }

    /**
     * 根据条件查询员工信息（分页）
     *
     * @param employeeSearchBo
     * @return
     * @author crn
     */
    private PageResult<EmployeeVO> getEmployeeVoPageResult(EmployeeSearchBO employeeSearchBo,
                                                           HttpServletRequest request) {

        Integer currPage = employeeSearchBo.getCurrPage();
        Integer pageSize = employeeSearchBo.getPageSize();
        currPage = null == currPage || currPage == 0 ? 1 : currPage;
        pageSize = null == pageSize || pageSize == 0 ? 10 : pageSize;
        PageResult<EmployeeVO> result = new PageResult<EmployeeVO>(currPage, pageSize);
        Page<EmployeeVO> page = PageHelper.startPage(currPage, pageSize);
        List<EmployeeVO> employees = employeeMapper.listEmployees(employeeSearchBo);
        if (null != employees && employees.size() > 0) {
            for (EmployeeVO emp : employees) {
                EmployeeVO employeeVO = new EmployeeVO();
                BeanUtils.copyProperties(emp, employeeVO);
                Integer sex = emp.getSex();
                Integer roleType = emp.getRoleType();
                String sexStr = sex == SexEnum.UNKNOWN.code ? SexEnum.UNKNOWN.value
                        : sex == SexEnum.MAN.code ? SexEnum.MAN.value : SexEnum.WOMAN.value;
                String roleTypeStr = roleType == Employee.RoleTypeEnum.BEAUTICIAN.code
                        ? Employee.RoleTypeEnum.BEAUTICIAN.value
                        : Employee.RoleTypeEnum.COUNSELOR.value;
                emp.setSexStr(sexStr);
                emp.setRoleTypeStr(roleTypeStr);
                emp.setUserId(userMapper.getUserByEmployeeId(emp.getId()).getId());
            }

        }
        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request,
                    RoleTypeEnum.ADVISER.code == employeeSearchBo.getRoleType()
                            ? EventSupervisor.SupervisorEventEnum.SUPERVISOR_ADVISOR_SHOW
                            : EventSupervisor.SupervisorEventEnum.SUPERVISOR_BEAUTICIAN_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setPage(employees);
        result.setTotalCount(page.getTotal());
        return result;
    }

    /**
     * 校验添加的顾问信息
     *
     * @param employeeVO
     * @param role       Counselor 顾问 Beautician美容师
     * @param type       save:添加 update:更新
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method validateCounselorInfo
     * @datetime 2018/7/6 13:43
     * @author crn
     */
    private ResultInfo validateCounselorInfo(EmployeeVO employeeVO, int role, String type) throws ParseException {
        ResultInfo resultInfo = new ResultInfo();

        // 真实姓名
        String realityName = employeeVO.getRealityName();
        // 性别
        Integer sex = employeeVO.getSex();
        // 出生日期
        String birthDateStr = employeeVO.getBirthDateStr();
        // 手机号
        String mobile = employeeVO.getMobile();
        // 员工编号
        String sno = employeeVO.getSno();
        // 入职日期
        String entryDateStr = employeeVO.getEntryDateStr();
        // 所属门店id
        Long storeId = employeeVO.getStoreId();
        // 所属顾问（美容师添加）
        Long parentId = employeeVO.getParentId();

        if (StringUtils.isBlank(realityName) || null == sex || 0 == sex || StringUtils.isBlank(birthDateStr)
                || StringUtils.isBlank(mobile) || StringUtils.isBlank(sno) || StringUtils.isBlank(entryDateStr)
                || null == storeId || 0 == storeId) {
            resultInfo.setInfo(-1, "请完善信息");
            return resultInfo;
        }

        // 美容师添加判断所属顾问
        if (RoleTypeEnum.BEAUTICIAN.code == role) {
            if (null == parentId || 0 == parentId) {
                resultInfo.setInfo(-1, "请选择所属顾问");
                return resultInfo;
            }
            EmployeeVO parentEmployee = employeeMapper.getEmployeeById(parentId);
            if (parentEmployee.getStoreId() != storeId) {
                resultInfo.setInfo(-1, "所选顾问与门店不匹配");
                return resultInfo;
            }
        }

        // 校验真实姓名
        if (realityName.length() < 2 || realityName.length() > 16) {
            resultInfo.setInfo(-1, "请输入正确的姓名");
            return resultInfo;
        }

        // 校验电话
        if (!StrUtil.isMobileNum(mobile)) {
            resultInfo.setInfo(-1, "请输入正确的手机号格式");
            return resultInfo;
        }

        if ("save".equals(type)) {
            Employee employeeByMobile = employeeMapper.getEmployeeByMobile(mobile);
            if (null != employeeByMobile) {
                resultInfo.setInfo(-1, "手机号已存在");
                return resultInfo;
            }
        }
        // 校验邮箱
        if (StringUtils.isNotBlank(employeeVO.getEmail()) && !StrUtil.isEmail(employeeVO.getEmail())) {
            resultInfo.setInfo(-1, "请输入正确的邮箱格式");
            return resultInfo;
        }
        // 校验QQ
        String qq = employeeVO.getQq();
        if (StringUtils.isNotBlank(qq)) {
            if (qq.length() < 5 || qq.length() > 11) {
                resultInfo.setInfo(-1, "请输入5-11位QQ号");
                return resultInfo;
            }
        }

        String idNumber = employeeVO.getIdNumber();
        // 校验身份证号码
        if (StringUtils.isNotBlank(idNumber)) {
            if (!StrUtil.isIdNumber(idNumber)) {
                resultInfo.setInfo(-1, "请输入正确的身份证号格式");
                return resultInfo;
            }
            if ("save".equals(type)) {
                boolean existIdNumber = isEmpIdNumber(idNumber);
                if (existIdNumber) {
                    resultInfo.setInfo(-1, "身份证号已存在");
                    return resultInfo;
                }
            }
            int tempSex = 0;
            // 判断性别
            if (Integer.parseInt(idNumber.substring(16).substring(0, 1)) % 2 == 0) {
                tempSex = 2;
            } else {
                tempSex = 1;
            }
            if (tempSex != sex) {
                resultInfo.setInfo(-1, "身份证号与性别不匹配");
                return resultInfo;
            }
        }

        // 查询编号是否存在
        boolean existSno = isEmpSno(sno);
        if (existSno && "save".equals(type)) {
            resultInfo.setInfo(-1, "员工编号已存在");
            return resultInfo;
        }

        // 字符串转时间
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeVO, employee);
        Date birthDate = DateUtil.strToDate(birthDateStr, "yyyy-MM-dd");
        Date entryDate = DateUtil.strToDate(entryDateStr, "yyyy-MM-dd");
        employee.setBirthDate(birthDate);
        employee.setEntryDate(entryDate);
        String photo = employeeVO.getPhoto();
        if (StringUtils.isBlank(photo)) {
            employee.setPhoto("/img/default.png");
        }

        resultInfo.setInfo(1, "校验成功", employee);
        return resultInfo;
    }

    /**
     * 判断身份证是否存在
     *
     * @param idNumber
     * @return boolean
     * @method isEmpIdNumber
     * @datetime 2018/7/6 11:55
     * @author crn
     */
    private boolean isEmpIdNumber(String idNumber) {

        // 根据身份证号获取员工数量
        int employeeSize = employeeMapper.countEmployeeByidNumber(idNumber);
        if (0 == employeeSize) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否存在员工编号
     *
     * @param sno
     * @return boolean
     * @method isEmpSno
     * @datetime 2018/7/6 11:16
     * @author crn
     */
    private boolean isEmpSno(String sno) {

        // 根据员工编号获取员工数量
        int employeeSize = employeeMapper.countEmployeeBySno(sno);
        if (0 == employeeSize) {
            return false;
        }
        return true;
    }


}
