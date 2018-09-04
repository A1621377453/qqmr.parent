package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.dao.*;
import com.qingqingmr.qqmr.domain.bean.RightRoleVO;
import com.qingqingmr.qqmr.domain.bean.SupervisorCurrentVO;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.RightRoleService;
import com.qingqingmr.qqmr.service.SupervisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 权限角色业务
 *
 * @author ztl
 * @datetime 2018-07-05 17:53:13
 */
@Service
public class RightRoleServiceImpl implements RightRoleService {
    private static final Logger LOG = LoggerFactory.getLogger(RightRoleServiceImpl.class);

    @Autowired
    private RightMapper rightMapper;
    @Autowired
    private RightRoleMapper rightRoleMapper;
    @Autowired
    private RightRoleRightMapper rightRoleRightMapper;
    @Autowired
    private RightResourceMapper rightResourceMapper;
    @Autowired
    private RightModuleMapper rightModuleMapper;
    @Autowired
    private RightRoleSupervisorMapper rightRoleSupervisorMapper;
    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private SupervisorService supervisorService;


    /**
     * 保存角色信息
     *
     * @param name
     * @param description
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo saveRightRole(String name, String description, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();

        if (rightRoleMapper.saveRightRole(name, description) < 0) {
            throw new ServiceRuntimeException("保存角色信息失败", true);
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.ADD_SUPERVISOR_ROLE);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setInfo(1, "保存角色信息成功");
        result.setObj(result);
        return result;
    }

    /**
     * 保存管理员的权限角色
     *
     * @param id
     * @param roleId
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo saveRightRoleSupervisor(Long id, Long roleId) {
        ResultInfo resultInfo = new ResultInfo();
        if (rightRoleSupervisorMapper.saveRightRoleSupervisor(id, roleId) < 0) {
            throw new ServiceRuntimeException("管理员角色添加异常", true);
        }
        resultInfo.setInfo(1, "管理员角色添加成功");
        return resultInfo;
    }

    /**
     * 删除管理员的权限角色
     *
     * @param id
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo deleteRightRoleSupervisorById(Long id) {
        ResultInfo resultInfo = new ResultInfo();

        if (id == null) {
            resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return resultInfo;
        }

        if (rightRoleSupervisorMapper.deleteRightRoleSupervisorById(id) < 0) {
            throw new ServiceRuntimeException("管理员角色删除异常", true);
        }
        resultInfo.setInfo(1, "管理员角色删除成功");
        return resultInfo;
    }

    /**
     * 更新管理员的权限角色
     *
     * @param id
     * @param roleId
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo updateRightRoleSupervisorById(Long id, Long roleId) {
        ResultInfo resultInfo = new ResultInfo();

        if (id == null) {
            resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return resultInfo;
        }

        if (rightRoleSupervisorMapper.updateRightRoleSupervisorById(id, roleId) < 0) {
            throw new ServiceRuntimeException("管理员角色更新异常", true);
        }
        resultInfo.setInfo(1, "管理员角色更新成功");
        return resultInfo;
    }

    /**
     * 更新角色信息
     *
     * @param rightRole
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo updateRightRole(RightRole rightRole, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();

        if (rightRoleMapper.updateRightRoleById(rightRole) < 0) {
            throw new ServiceRuntimeException("更新角色信息失败", true);
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.EDIT_SUPERVISOR_ROLE);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setInfo(1, "更新角色信息成功");
        result.setObj(result);
        return result;
    }

    /**
     * 根据姓名查询角色信息
     *
     * @param name
     * @return
     * @author ztl
     */
    @Override
    public RightRole getRightRoleByName(String name) {
        return rightRoleMapper.getRightRoleByName(name);
    }

    /**
     * 根据id查询角色信息
     *
     * @param id
     * @return
     * @author ztl
     */
    @Override
    public RightRole getRightRoleById(Long id) {
        return rightRoleMapper.getRightRoleById(id);
    }

    /**
     * 查询模块对应的权限
     *
     * @return
     * @author ztl
     * @datetime 2018-7-5 19:28:24
     */
    @Override
    public List<Right> getAllRightByModule(Long moduleId) {
        return rightMapper.getAllRightByModule(moduleId);
    }

    /**
     * 查詢所有的模块
     *
     * @author ztl
     */
    @Override
    public List<Map<String, Object>> listAllModule() {

        return rightModuleMapper.listAllModule();
    }

    /**
     * 查所有的权限
     *
     * @author ztl
     */
    @Override
    public Map<String, List<Right>> getAllRight() {
        List<Map<String, Object>> modules = listAllModule();
        if (modules == null || modules.size() < 1)
            return null;

        Map<String, List<Right>> map = new HashMap<>();
        for (Map<String, Object> tmp : modules) {
            map.put((String) tmp.get("name"), getAllRightByModule((Long) tmp.get("moduleId")));
        }
        return map;
    }

    /**
     * 查询角色的所有权限id
     *
     * @author ztl
     */
    @Override
    public List<Long> getRightRoleRightByRoleId(Long roleId) {
        if (roleId == null || roleId < 1)
            return null;
        return rightRoleRightMapper.getRightRoleRightByRoleId(roleId);
    }

    /**
     * 编辑角色的权限
     *
     * @author ztl
     */
    @Transactional
    @Override
    public ResultInfo updateRightRoleRight(Long roleId, List<Long> rights, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        if (roleId == null || roleId < 1) {
            result.setInfo(-1, "角色信息有误");
            return result;
        }

        List<Long> _rights = getRightRoleRightByRoleId(roleId);
        // 页面传递
        Set<Long> ids = new HashSet<>(rights);
        // 数据库保存
        Set<Long> _ids = new HashSet<>(_rights);

        // 新增
        Set<Long> set = new HashSet<Long>();
        set.addAll(ids);
        set.removeAll(_ids);
        RightRoleRight record = null;
        for (Iterator<Long> it = set.iterator(); it.hasNext(); ) {
            record = new RightRoleRight();
            record.setRoleId(roleId);
            record.setRightId(it.next());
            if (rightRoleRightMapper.saveRightRoleRight(record) < 0) {
                throw new ServiceRuntimeException("增加权限角色信息失败", true);
            }
        }

        set.clear();
        set.addAll(_ids);
        set.removeAll(ids);
        for (Iterator<Long> it = set.iterator(); it.hasNext(); ) {
            if (rightRoleRightMapper.deleteRightRoleRightByRoleId(it.next(), roleId) < 0) {
                throw new ServiceRuntimeException("删除权限角色信息失败", true);
            }
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.RIGHT_SUPERVISOR);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }


        SupervisorCurrentVO supervisorCurrent = supervisorService.getSupervisorCurrent(request);
        if (roleId == supervisorCurrent.getId()) {
            // 把权限重新设置到session中
            supervisorCurrent.setRightIds(rights);
            request.getSession().setAttribute(SystemConstant.SESSION_SUPERVISOR + request.getSession().getId(), supervisorCurrent);
        }
        result.setInfo(1, "编辑成功");
        return result;
    }

    /**
     * 根据资源查询对应的权限id
     *
     * @author ztl
     */
    @Override
    public List<Long> getRightByResource(String resource) {
        return rightResourceMapper.getRightByResource(resource);
    }

    /**
     * 根据角色id查询对应角色名称
     *
     * @author ztl
     */
    @Override
    public String getRoleNameByRoleId(Long roleId) {
        return rightRoleMapper.getRightRoleById(roleId).getName();
    }

    /**
     * 根据管理员id查询管理员的角色
     *
     * @param supervisorId
     * @return
     * @author ztl
     */
    @Override
    public RightRoleSupervisor getRightRoleSupervisorBySupervisorId(Long supervisorId) {
        return rightRoleSupervisorMapper.getRightRoleSupervisorBySupervisorId(supervisorId);
    }

    /**
     * 查询角色列表
     *
     * @param currPage
     * @param pageSize
     * @param request
     * @return
     * @author ztl
     */
    @Override
    public ResultInfo listRightRolePage(Integer currPage, Integer pageSize, HttpServletRequest request) {
        ResultInfo info = new ResultInfo();
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<RightRoleVO> result = new PageResult<RightRoleVO>(currPage, pageSize);

        Page<RightRoleVO> page = PageHelper.startPage(currPage, pageSize);
        List<RightRoleVO> listRightRole = new ArrayList<RightRoleVO>();


        List<RightRole> rightRoleList = rightRoleMapper.listRightRolePage();
        for (RightRole role : rightRoleList) {
            RightRoleVO rightRoleVO = new RightRoleVO();
            rightRoleVO.setId(role.getId());
            rightRoleVO.setName(role.getName());
            rightRoleVO.setDescription(role.getDescription());
            rightRoleVO.setTime(role.getTime());
            Integer number = rightRoleSupervisorMapper.countRightRoleSupervisorByRoleId(role.getId());
            rightRoleVO.setNumber(number);
            listRightRole.add(rightRoleVO);
        }

        result.setPage(listRightRole);
        result.setTotalCount(page.getTotal());

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.SHOW_SUPERVISOR_ROLE);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        info.setInfo(1, "查询成功");
        info.setObj(result);
        return info;
    }

    /**
     * 查询所有的角色信息
     *
     * @return
     * @author ztl
     */
    @Override
    public List<Map<String, Object>> listAllRole() {
        List<Map<String, Object>> listRole = rightRoleMapper.listAllRole();
        return listRole;
    }

}

