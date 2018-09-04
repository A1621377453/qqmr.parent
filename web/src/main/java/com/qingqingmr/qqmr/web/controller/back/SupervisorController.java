package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.domain.entity.RightRoleSupervisor;
import com.qingqingmr.qqmr.domain.entity.Supervisor;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.RightRoleService;
import com.qingqingmr.qqmr.service.SupervisorService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理-管理员
 *
 * @author ztl
 * @datetime 2018-7-5 13:49:44
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping("/back/supervisor")
public class SupervisorController {
    private static final Logger LOG = LoggerFactory.getLogger(SupervisorController.class);

    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private RightRoleService rightRoleService;


    /**
     * 后台-系统管理-管理员列表
     *
     * @author ztl
     */
    @RequestMapping(value = "/showsupervisor", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showSupervisor(Integer currPage, Integer pageSize, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = supervisorService.listSupervisorPage(currPage, pageSize, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());

        return FastJsonUtil.toJsonString(json);
    }


    /**
     * 添加管理员
     *
     * @param supervisor
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/addsupervisor", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addSupervisor(Supervisor supervisor, Long roleId, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        ResultInfo result = new ResultInfo();

        if (StringUtils.isBlank(supervisor.getName())) {
            json.put("code", -1);
            json.put("msg", "请输入用户名!");
            return FastJsonUtil.toJsonString(json);

        }

        if (!StrUtil.isValidUsername(supervisor.getName(), 4, 16)) {
            json.put("code", -1);
            json.put("msg", "请输入4-16位字母或字母数字组合的用户名!");
            return FastJsonUtil.toJsonString(json);
        }

        if (supervisorService.getSupervisorByName(supervisor.getName()) != null) {
            json.put("code", -1);
            json.put("msg", "该用户名已经存在，请重新输入!");
            return FastJsonUtil.toJsonString(json);
        }

        if (supervisorService.getSupervisorByMobile(supervisor.getMobile()) != null) {
            json.put("code", -1);
            json.put("msg", "该手机号码已经被使用，请重新输入!");
            return FastJsonUtil.toJsonString(json);
        }

        json = verifySupervisor(supervisor, roleId);
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        try {
            result = supervisorService.saveSupervisor(supervisor, roleId, request);
            if (result.getCode() < 1) {
                json.put("code", result.getCode());
                json.put("msg", result.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("添加管理员信息--{}", e.getMessage());
            json.put("code", result.getCode());
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", result.getCode());
        json.put("msg", result.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 删除管理员
     *
     * @param id
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/delsupervisor", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String delSupervisor(Long id, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        JSONObject json = new JSONObject();

        if (id == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        try {
            result = supervisorService.deleteSupervisorById(id, request);
        } catch (Exception e) {
            LOG.error("删除管理员信息--{}", e.getMessage());
            json.put("code", -1);
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", result.getCode());
        json.put("msg", result.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 查询要更新的管理员的信息
     *
     * @param id 管理员id
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/showsupervisordetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showSupervisorDetail(Long id) {
        JSONObject json = new JSONObject();

        Map<String, Object> map = new HashMap<String, Object>();

        if (id != null) {
            Supervisor supervisor = supervisorService.getSupervisorById(id);
            RightRoleSupervisor role = rightRoleService.getRightRoleSupervisorBySupervisorId(id);
            Long roleId = null;
            if (role != null) {
                roleId = role.getRoleId();
            }
            map.put("supervisor", supervisor);
            map.put("roleId", roleId);
        }
        List<Map<String, Object>> listRole = rightRoleService.listAllRole();
        map.put("listRole", listRole);

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", map);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑管理员信息
     *
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/editsupervisor", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editSupervisor(Supervisor supervisor, Long roleId, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        ResultInfo result = new ResultInfo();

        json = verifySupervisor(supervisor, roleId);
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        try {
            result = supervisorService.updateSupervisorById(supervisor, roleId, request);
        } catch (Exception e) {
            LOG.error("修改管理员信息--{}", e.getMessage());
            json.put("code", -1);
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", result.getCode());
        json.put("msg", result.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 查询操作日志列表
     *
     * @author ztl
     * @datetime 2018-7-5 15:45:38
     */
    @RequestMapping(value = "/showsupervisoreventlog", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showSupervisorEventLog(Integer currPage, Integer pageSize, @RequestParam Map<String, Object> map, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = eventSupervisorService.listSupervisorEventLogPage(currPage, pageSize, map, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());

        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加或更新管理员的验证
     *
     * @param supervisor
     * @param roleId
     * @return
     * @author ztl
     */
    public JSONObject verifySupervisor(Supervisor supervisor, Long roleId) {
        JSONObject json = new JSONObject();

        if (supervisor == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return json;
        }

        if (StringUtils.isBlank(supervisor.getRealityName())) {
            json.put("code", -1);
            json.put("msg", "请输入真实姓名!");
            return json;

        }

        if (!StrUtil.isValidAreaname(supervisor.getRealityName())) {
            json.put("code", -1);
            json.put("msg", "请输入正确的姓名!");
            return json;
        }

        if (StringUtils.isBlank(supervisor.getMobile())) {
            json.put("code", -1);
            json.put("msg", "请输入手机号!");
            return json;

        }

        if (!StrUtil.isMobileNum(supervisor.getMobile())) {
            json.put("code", -1);
            json.put("msg", "请输入正确的手机号格式!");
            return json;
        }

        if (roleId == null) {
            json.put("code", -1);
            json.put("msg", "请选择所属角色!");
            return json;
        }

        if (StringUtils.isBlank(supervisor.getPassword())) {
            json.put("code", -1);
            json.put("msg", "请输入密码!");
            return json;
        }

        if (!StrUtil.isValidPassword(supervisor.getPassword(), 6, 15)) {
            json.put("code", -1);
            json.put("msg", "请输入6-15位数字字母组合密码!");
            return json;
        }

        json.put("code", 1);
        json.put("msg", "验证成功!");
        return json;
    }

}
