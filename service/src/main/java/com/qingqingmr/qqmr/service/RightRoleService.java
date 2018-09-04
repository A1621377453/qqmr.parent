package com.qingqingmr.qqmr.service;


import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.Right;
import com.qingqingmr.qqmr.domain.entity.RightRole;
import com.qingqingmr.qqmr.domain.entity.RightRoleSupervisor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 角色权限业务
 *
 * @author ztl
 * @datetime 2018-07-05 17:50:22
 */
public interface RightRoleService {
    /**
     * 保存角色信息
     *
     * @param name
     * @param description
     * @author ztl
     */
    ResultInfo saveRightRole(String name, String description, HttpServletRequest request);

    /**
     * 保存管理员的权限角色
     *
     * @param id
     * @param roleId
     * @return
     * @author ztl
     */
    ResultInfo saveRightRoleSupervisor(Long id, Long roleId);

    /**
     * 删除管理员的权限角色
     *
     * @param id
     * @return
     * @author ztl
     */
    ResultInfo deleteRightRoleSupervisorById(Long id);

    /**
     * 更新管理员的权限角色
     *
     * @param id
     * @param roleId
     * @return
     * @author ztl
     */
    ResultInfo updateRightRoleSupervisorById(Long id, Long roleId);

    /**
     * 更新角色信息
     *
     * @param rightRole
     * @return
     * @author ztl
     */
    ResultInfo updateRightRole(RightRole rightRole, HttpServletRequest request);

    /**
     * 根据姓名查询角色信息
     *
     * @param name
     * @return
     * @author ztl
     */
    RightRole getRightRoleByName(String name);

    /**
     * 根据id查询角色信息
     *
     * @param id
     * @return
     * @author ztl
     */
    RightRole getRightRoleById(Long id);

    /**
     * 查询所有的权限
     *
     * @return
     * @author ztl
     * @datetime 2018-7-5 19:28:24
     */
    List<Right> getAllRightByModule(Long moduleId);

    /**
     * 查詢所有的模块
     *
     * @author ztl
     */
    List<Map<String, Object>> listAllModule();

    /**
     * 查询所有权限
     *
     * @return
     * @author ztl
     */
    Map<String, List<Right>> getAllRight();

    /**
     * 查询角色的所有权限id
     *
     * @author ztl
     */
    List<Long> getRightRoleRightByRoleId(Long roleId);

    /**
     * 编辑角色的权限
     *
     * @author ztl
     */
    ResultInfo updateRightRoleRight(Long roleId, List<Long> rights, HttpServletRequest request);

    /**
     * 根据资源查询对应的权限id
     *
     * @author ztl
     */
    List<Long> getRightByResource(String resource);

    /**
     * 根据角色id查询对应角色名称
     *
     * @author ztl
     */
    String getRoleNameByRoleId(Long roleId);

    /**
     * 根据管理员id查询管理员的角色
     *
     * @param supervisorId
     * @return
     * @author ztl
     */
    RightRoleSupervisor getRightRoleSupervisorBySupervisorId(Long supervisorId);

    /**
     * 查询角色列表
     *
     * @param currPage
     * @param pageSize
     * @param request
     * @return
     * @author ztl
     */
    ResultInfo listRightRolePage(Integer currPage, Integer pageSize, HttpServletRequest request);

    /**
     * 查询所有的角色信息
     *
     * @return
     * @author ztl
     */
    List<Map<String, Object>> listAllRole();
}
