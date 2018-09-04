package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.domain.entity.Right;
import com.qingqingmr.qqmr.domain.entity.RightRole;
import com.qingqingmr.qqmr.service.RightRoleService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色权限的控制器
 *
 * @author ztl
 * @datetime 2018-07-05 17:45:16
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping("/back/role")
public class RightRoleController {
    @Autowired
    private RightRoleService rightRoleService;

    /**
     * 后台-系统管理-角色列表
     *
     * @author ztl
     */
    @RequestMapping(value = "/showrightrole", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showRightRole(Integer currPage, Integer pageSize, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = rightRoleService.listRightRolePage(currPage, pageSize, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());

        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加角色
     *
     * @param name 名称 escription 描述
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/addrightrole", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addRightRole(String name, String description, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        json = verifyRightRole(name, description);
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        if (rightRoleService.getRightRoleByName(name) != null) {
            json.put("code", -1);
            json.put("msg", "该角色已经存在，请重新输入!");
            return FastJsonUtil.toJsonString(json);
        }

        ResultInfo resultInfo = rightRoleService.saveRightRole(name, description, request);

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 查询要编辑的角色信息
     *
     * @param id
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/showrightroledetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showRightRoleDetail(Long id) {
        JSONObject json = new JSONObject();

        if (id == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        RightRole rightRole = rightRoleService.getRightRoleById(id);

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", rightRole);

        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑角色
     *
     * @param rightRole
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/editrightrole", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editRightRole(RightRole rightRole, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (rightRole == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }


        json = verifyRightRole(rightRole.getName(), rightRole.getDescription());
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        RightRole role = rightRoleService.getRightRoleById(rightRole.getId());
        if (!role.getName().equals(rightRole.getName())) {
            if (rightRoleService.getRightRoleByName(rightRole.getName()) != null) {
                json.put("code", -1);
                json.put("msg", "该角色已经存在，请重新输入!");
                return FastJsonUtil.toJsonString(json);
            }
        }

        ResultInfo resultInfo = rightRoleService.updateRightRole(rightRole, request);

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 员工管理-查找每个角色对应的权限
     *
     * @param roleId
     * @return
     * @author ztl
     * @datetime 2018-7-5 20:54:58
     */
    @RequestMapping(value = "/showright", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showRight(Long roleId) {
        JSONObject json = new JSONObject();

        if (roleId == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, List<Right>> allRight = rightRoleService.getAllRight();
        List<Long> rights = rightRoleService.getRightRoleRightByRoleId(roleId);
        String roleName = rightRoleService.getRoleNameByRoleId(roleId);
        map.put("allRight", allRight);
        map.put("rights", rights);
        map.put("roleName", roleName);
        map.put("roleId", roleId);

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", map);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * <p>
     * 角色-权限-修改
     * </p>
     *
     * @param roleId
     * @param rights
     * @return
     * @author ztl
     * @datetime 2018-7-5 21:01:37
     */
    @RequestMapping(value = "/editright", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editRight(Long roleId, String rights, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (roleId == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        if (rights == null) {
            json.put("code", -1);
            json.put("msg", "请勾选对应的权限!");
            return FastJsonUtil.toJsonString(json);
        }

        Long[] rightsArray = JSONArray.parseObject(rights, Long[].class);
        ResultInfo result = rightRoleService.updateRightRoleRight(roleId, Arrays.asList(rightsArray), request);

        json.put("code", result.getCode());
        json.put("msg", result.getMsg());
        return FastJsonUtil.toJsonString(json);

    }

    /**
     * 权限角色信息的验证
     *
     * @param name
     * @param description
     * @return
     * @author ztl
     */
    public JSONObject verifyRightRole(String name, String description) {
        JSONObject json = new JSONObject();

        if (StringUtils.isBlank(name)) {
            json.put("code", -1);
            json.put("msg", "请输入角色名称!");
            return json;
        }

        if (!StrUtil.isValidAreaname(name)) {
            json.put("code", -1);
            json.put("msg", "请输入正确的名称!");
            return json;
        }

        if (StringUtils.isNotBlank(description) && !StrUtil.isValidAreaname(description)) {
            json.put("code", -1);
            json.put("msg", "请输入正确的描述!");
            return json;
        }

        json.put("code", 1);
        json.put("msg", "验证成功!");
        return json;
    }

}
