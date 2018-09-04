package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.common.util.DateUtil;
import com.qingqingmr.qqmr.common.util.Encrypt;
import com.qingqingmr.qqmr.dao.EventSupervisorMapper;
import com.qingqingmr.qqmr.dao.SupervisorMapper;
import com.qingqingmr.qqmr.domain.bean.SupervisorCurrentVO;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.domain.entity.Supervisor;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.RightRoleService;
import com.qingqingmr.qqmr.service.SupervisorService;
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
 * @author ztl
 * @datetime 2018-07-04 16:04:27
 */
@Service
public class SupervisorServiceImpl implements SupervisorService {
    private static final Logger LOG = LoggerFactory.getLogger(SupervisorServiceImpl.class);

    @Autowired
    private SupervisorMapper supervisorMapper;
    @Autowired
    private RightRoleService rightRoleService;
    @Autowired
    private EventSupervisorMapper eventSupervisorMapper;
    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private SpringContextProperties springContextProperties;

    /**
     * 保存管理员信息
     *
     * @param supervisor
     * @author ztl
     */
    @Transactional
    @Override
    public ResultInfo saveSupervisor(Supervisor supervisor, Long roleId, HttpServletRequest request) {
        ResultInfo info = new ResultInfo();
        if (supervisor == null) {
            info.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return info;
        }

        supervisor.setTime(new Date());
        String _password = Encrypt.MD5(supervisor.getPassword() + springContextProperties.getMd5Key());
        supervisor.setPassword(_password);
        supervisor.setRemark("添加管理员");
        supervisor.setCreaterId(getSupervisorCurrent(request).getId());

        if (supervisorMapper.saveSupervisor(supervisor) < 0) {
            throw new ServiceRuntimeException("添加管理员异常", true);
        }

        try {
            info = rightRoleService.saveRightRoleSupervisor(supervisor.getId(), roleId);
        } catch (Exception e) {
            LOG.error("添加管理员角色信息--{}", e.getMessage());
            info.setInfo(-1, e.getMessage());
            return info;
        }


        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.ADD_SUPERVISOR);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        info.setInfo(1, "添加管理员信息成功");
        return info;
    }

    /**
     * 删除管理员信息
     *
     * @param id
     * @author ztl
     */
    @Transactional
    @Override
    public ResultInfo deleteSupervisorById(Long id, HttpServletRequest request) {
        ResultInfo info = new ResultInfo();

        if (id == null) {
            info.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return info;
        }

        if (supervisorMapper.deleteSupervisorById(id) < 0) {
            throw new ServiceRuntimeException("删除管理员异常", true);
        }

        try {
            info = rightRoleService.deleteRightRoleSupervisorById(id);
            if (info.getCode() < 1) {
                return info;
            }
        } catch (Exception e) {
            LOG.error("删除管理员角色信息--{}", e.getMessage());
            info.setInfo(-1, e.getMessage());
            return info;
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.DEL_SUPERVISOR);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        info.setInfo(1, "删除管理员成功");
        return info;
    }

    /**
     * 编辑管理员信息
     *
     * @author ztl
     */
    @Transactional
    @Override
    public ResultInfo updateSupervisorById(Supervisor supervisor, Long roleId, HttpServletRequest request) {
        ResultInfo info = new ResultInfo();

        supervisor.setCreaterId(getSupervisorCurrent(request).getId());
        supervisor.setRemark("更新管理员信息");
        String _password = Encrypt.MD5(supervisor.getPassword() + springContextProperties.getMd5Key());
        supervisor.setPassword(_password);

        if (supervisorMapper.updateSupervisorById(supervisor) < 0) {
            throw new ServiceRuntimeException("更新管理员异常", true);
        }

        try {
            info = rightRoleService.updateRightRoleSupervisorById(supervisor.getId(), roleId);
            if (info.getCode() < 1) {
                return info;
            }
        } catch (Exception e) {
            LOG.error("更新管理员角色信息--{}", e.getMessage());
            info.setInfo(-1, e.getMessage());
            return info;
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.EDIT_SUPERVISOR);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        info.setInfo(1, "编辑管理员信息成功");
        return info;
    }

    /**
     * 查询管理员列表
     *
     * @return
     * @author ztl
     */
    @Override
    public ResultInfo listSupervisorPage(Integer currPage, Integer pageSize, HttpServletRequest request) {
        ResultInfo info = new ResultInfo();
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<SupervisorCurrentVO> result = new PageResult<SupervisorCurrentVO>(currPage, pageSize);

        Page<SupervisorCurrentVO> page = PageHelper.startPage(currPage, pageSize);
        List<SupervisorCurrentVO> listSupervisorPage = supervisorMapper.listSupervisorPage();

        result.setPage(listSupervisorPage);
        result.setTotalCount(page.getTotal());

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.SHOW_SUPERVISOR);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        info.setInfo(1, "查询成功");
        info.setObj(result);
        return info;
    }

    /**
     * 根据name查询管理员信息
     *
     * @param name
     * @return
     * @author ztl
     */
    @Override
    public Supervisor getSupervisorByName(String name) {
        Supervisor supervisor = supervisorMapper.getSupervisorByName(name);
        return supervisor;
    }

    /**
     * 后台登录
     *
     * @param name     用户名
     * @param password 密码
     * @param ip       登录ip
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultInfo login(String name, String password, String ip, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        Supervisor supervisor = supervisorMapper.getSupervisorByName(name);
        if (null == supervisor) {
            /* 添加一次输入错误 */
            resultInfo.setInfo(-1, "用户名或者密码输入有误");
            return resultInfo;
        } else {
            /* 判断账号锁定状态 */
            if (supervisor.getLockStatus() == 1) {
                resultInfo.setInfo(-1, "用户名已停用");
                return resultInfo;
            }
            /* 判断账号是否密码锁定 */
            if (supervisor.getIsPasswordLocked() && supervisor.getLockStatus() == 0) {
                if (DateUtil.getMinutesDiffFloor(supervisor.getPasswordLockedTime(), new Date()) < SystemConstant.FAIL_COUNT_TIME) {
                    resultInfo.setInfo(-1, "连续错误次数超出请稍后再试");
                    return resultInfo;
                } else {
                    supervisor.setIsPasswordLocked(false);
                    supervisor.setPasswordContinueFails(0);
                    supervisor.setPasswordLockedTime(null);
                    if (supervisorMapper.updateSupervisorByIdSelective(supervisor) < 1) {
                        resultInfo.setInfo(ResultInfo.EXCEPTION_CODE_404, ResultInfo.EXCEPTION_MSG_404);
                        return resultInfo;
                    }
                }
            }
            // md5加密验证
            if (!StringUtils.equals(Encrypt.MD5(password + springContextProperties.getMd5Key()), supervisor.getPassword())) {
                /* 获取当前错误登录次数 */
                int currLoginFailCount = supervisor.getPasswordContinueFails();
                currLoginFailCount += 1;
                if (currLoginFailCount >= SystemConstant.CURR_LOGIN_FAIL_COUNT) {
                    /* 用户达到锁定账号条件，执行账号锁定 */
                    supervisor.setPasswordLockedTime(new Date());
                    supervisor.setIsPasswordLocked(true);
                    supervisor.setPasswordContinueFails(currLoginFailCount);
                    if (supervisorMapper.updateSupervisorByIdSelective(supervisor) < 1) {
                        resultInfo.setInfo(ResultInfo.EXCEPTION_CODE_404, ResultInfo.EXCEPTION_MSG_404);
                        return resultInfo;
                    }

                    resultInfo.setInfo(-1, "连续错误次数超出请稍后再试");
                    return resultInfo;
                }
                /* 添加一次输入错误 */
                supervisor.setPasswordContinueFails(currLoginFailCount);
                if (supervisorMapper.updateSupervisorByIdSelective(supervisor) < 1) {
                    resultInfo.setInfo(ResultInfo.EXCEPTION_CODE_404, ResultInfo.EXCEPTION_MSG_404);
                    return resultInfo;
                }
                resultInfo.setInfo(-1, "密码输入有误");
                return resultInfo;
            }
            // 查询上一次登录时间和ip
            EventSupervisor lastLoginInfo = eventSupervisorMapper.getLastLoginInfo(supervisor.getId(), EventSupervisor.SupervisorEventEnum.LOGIN.getValue());
            if (null == lastLoginInfo) {
                supervisor.setLastLoginTime(null);
                supervisor.setLastLoginIp("");
            } else {
                supervisor.setLastLoginTime(lastLoginInfo.getTime());
                supervisor.setLastLoginIp(lastLoginInfo.getIp());
            }

            /* 登录成功，修改登录信息 */
            supervisor.setLoginCount(supervisor.getLoginCount() + 1);
            supervisor.setPasswordContinueFails(0);

            if (supervisorMapper.updateSupervisorByIdSelective(supervisor) < 1) {
                LOG.error("登录，修改登录信息失败");
                throw new ServiceRuntimeException("登录，修改登录信息失败", true);
            }
            /* 查询当前登录管理员信息 */
            SupervisorCurrentVO supervisorCurrent = supervisorMapper.getSupervisorCurrentById(supervisor.getId());
            request.getSession().setAttribute(SystemConstant.SESSION_SUPERVISOR + request.getSession().getId(), supervisorCurrent);

            // 添加管理员操作事件
            try {
                eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.LOGIN);
            } catch (Exception e) {
                LOG.error("添加管理员操作事件--{}", e.getMessage());
                resultInfo.setInfo(-1, e.getMessage());
                return resultInfo;
            }

            resultInfo.setInfo(1, "登录成功");
        }
        return resultInfo;
    }

    /**
     * 通过id查询管理员信息
     *
     * @return
     * @author ztl
     */
    @Override
    public Supervisor getSupervisorById(Long id) {
        return supervisorMapper.getSupervisorById(id);
    }

    /**
     * 获取后台首页管理员信息
     *
     * @param request
     * @return
     * @author crn
     */
    @Override
    public ResultInfo getSupervisorHomeInfo(HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        SupervisorCurrentVO supervisorCurrent = getSupervisorCurrent(request);
        SupervisorCurrentVO currentVO = supervisorMapper.getSupervisorHomeInfo(supervisorCurrent.getId());
        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.HOME);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        resultInfo.setInfo(1, "查询成功", currentVO);
        return resultInfo;
    }

    /**
     * 根据mobile查询管理员信息
     *
     * @param mobile
     * @return
     * @author ztl
     */
    @Override
    public Supervisor getSupervisorByMobile(String mobile) {
        return supervisorMapper.getSupervisorByMobile(mobile);
    }

    /**
     * 获取登录账户
     *
     * @param request
     * @return
     * @author ztl
     */
    @Override
    public SupervisorCurrentVO getSupervisorCurrent(HttpServletRequest request) {
        if (request != null) {
            Object obj = request.getSession().getAttribute(SystemConstant.SESSION_SUPERVISOR + request.getSession().getId());

            if (obj != null) {
                return (SupervisorCurrentVO) obj;
            }
        }
        return null;
    }
}
