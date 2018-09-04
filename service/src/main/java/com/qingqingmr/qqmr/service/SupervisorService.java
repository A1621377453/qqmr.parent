package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.bean.SupervisorCurrentVO;
import com.qingqingmr.qqmr.domain.entity.Supervisor;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员
 *
 * @author ztl
 * @datetime 2018-07-04 15:58:19
 */
public interface SupervisorService {
    /**
     * 保存管理员信息
     *
     * @param Supervisor
     * @author ztl
     */
    ResultInfo saveSupervisor(Supervisor Supervisor, Long roleId, HttpServletRequest request);

    /**
     * 删除管理员信息
     *
     * @param id
     * @author ztl
     */
    ResultInfo deleteSupervisorById(Long id, HttpServletRequest request);

    /**
     * 编辑管理员信息
     *
     * @author ztl
     */
    ResultInfo updateSupervisorById(Supervisor supervisor, Long roleId, HttpServletRequest request);

    /**
     * 查询管理员列表
     *
     * @return
     * @author ztl
     */
    ResultInfo listSupervisorPage(Integer currPage, Integer pageSize, HttpServletRequest request);

    /**
     * 根据name查询管理员信息
     *
     * @param name
     * @return
     * @author ztl
     */
    Supervisor getSupervisorByName(String name);

    /**
     * 根据mobile查询管理员信息
     *
     * @param mobile
     * @return
     * @author ztl
     */
    Supervisor getSupervisorByMobile(String mobile);

    /**
     * 获取登录账户
     *
     * @param request
     * @return
     * @author ztl
     */
    SupervisorCurrentVO getSupervisorCurrent(HttpServletRequest request);

    /**
     * 后台登录
     *
     * @param name
     * @param password
     * @param ip
     * @param request
     * @return
     */
    ResultInfo login(String name, String password, String ip, HttpServletRequest request);

    /**
     * 通过id查询管理员信息
     *
     * @return
     * @author ztl
     */
    Supervisor getSupervisorById(Long id);

    /**
     * 获取后台首页管理员信息
     *
     * @param request
     * @return
     */
    ResultInfo getSupervisorHomeInfo(HttpServletRequest request);
}
