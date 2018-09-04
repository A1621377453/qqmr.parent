package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.bo.EmployeeSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.PinyinUtil;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.domain.bean.EmployeeVO;
import com.qingqingmr.qqmr.domain.entity.Employee;
import com.qingqingmr.qqmr.domain.entity.Store;
import com.qingqingmr.qqmr.domain.entity.StoreArea;
import com.qingqingmr.qqmr.domain.entity.User;
import com.qingqingmr.qqmr.service.EmployeeService;
import com.qingqingmr.qqmr.service.StoreAreaService;
import com.qingqingmr.qqmr.service.StoreService;
import com.qingqingmr.qqmr.service.UserService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理控制器
 *
 * @author crn
 * @datetime 2018-07-05 11:03:15
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping(value = "/back/employee")
public class EmployeeManageController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeManageController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreAreaService storeAreaService;

    @Autowired
    private UserService userService;

    /**
     * 获取门店以及区域列表
     *
     * @param
     * @datetime 2018/7/5 19:14
     * @author ztl
     */
    @RequestMapping(value = "/liststoreandareas", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listStoreAndAreas(HttpServletRequest request) {
        JSONObject json = new JSONObject();

        Map<String, List<Store>> map = storeAreaService.listStoreAndAreas(request);

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", map);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取顾问列表
     *
     * @param employeeSearchBo
     * @return java.lang.String
     * @datetime 2018/7/12 17:51
     * @author crn
     */
    @RequestMapping(value = "/listcounselors", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listCounselors(EmployeeSearchBO employeeSearchBo, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == employeeSearchBo) {
            employeeSearchBo = new EmployeeSearchBO();
        }
        employeeSearchBo.setRoleType(Employee.RoleTypeEnum.COUNSELOR.code);
        // 查询顾问列表
        PageResult<EmployeeVO> employees = employeeService.listCounselors(employeeSearchBo, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", employees);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取美容师列表
     *
     * @param employeeSearchBo
     * @return java.lang.String
     * @datetime 2018/7/12 18:19
     * @author crn
     */
    @RequestMapping(value = "/listbeauticians", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listBeauticians(EmployeeSearchBO employeeSearchBo, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == employeeSearchBo) {
            employeeSearchBo = new EmployeeSearchBO();
        }
        employeeSearchBo.setRoleType(Employee.RoleTypeEnum.BEAUTICIAN.code);
        // 查询美容师列表
        PageResult<EmployeeVO> employees = employeeService.listBeauticians(employeeSearchBo, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", employees);
        return FastJsonUtil.toJsonString(json);
    }


    /**
     * 获取门店区域列表
     *
     * @param
     * @return java.lang.String
     * @datetime 2018/7/12 18:25
     * @author crn
     */
    @RequestMapping(value = "/liststoreareas", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listStoreAreas() {
        JSONObject json = new JSONObject();

        List<StoreArea> storeAreas = storeAreaService.listStoreAreas();
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", storeAreas);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 根据区域id查询门店列表
     *
     * @param areaId
     * @return java.lang.String
     * @datetime 2018/7/12 18:33
     * @author crn
     */
    @RequestMapping(value = "/liststores", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listStores(Long areaId) {
        JSONObject json = new JSONObject();

        List<Store> store = storeService.listStores(areaId);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", store);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加区域
     *
     * @param name 名称
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/addstorearea", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addStoreArea(String name, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        JSONObject json = new JSONObject();

        json = verifyStoreArea(name);
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        try {
            resultInfo = storeAreaService.saveStoreArea(name, request);
        } catch (Exception e) {
            LOG.error("添加区域--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 查询要编辑的区域信息
     *
     * @param id
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/showstoreareadetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showStoreAreaDetail(Long id) {
        JSONObject json = new JSONObject();

        if (id == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        StoreArea storeArea = storeAreaService.getStoreAreaById(id);

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", storeArea);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑区域
     *
     * @param name 名称
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/editstorearea", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editStoreArea(Long id, String name, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        JSONObject json = new JSONObject();

        if (id == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        json = verifyStoreArea(name);
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        try {
            resultInfo = storeAreaService.updateStoreArea(id, name, request);
        } catch (Exception e) {
            LOG.error("更新区域--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加美容店
     *
     * @param store
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/addstore", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addStore(Store store, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        JSONObject json = new JSONObject();

        json = verifyStore(store);
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        try {
            resultInfo = storeService.saveStore(store, request);
        } catch (Exception e) {
            LOG.error("添加美容店--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 查询要编辑的美容店信息
     *
     * @param id
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/showstoredetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showStoreDetail(Long id) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();

        if (id != null) {
            Store store = storeService.getStoreById(id);
            map.put("store", store);
        }

        List<StoreArea> storeAreas = storeAreaService.listStoreAreas();
        map.put("storeAreas", storeAreas);

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", map);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑美容店
     *
     * @param store
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/editstore", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editStore(Store store, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        JSONObject json = new JSONObject();

        json = verifyStore(store);
        if ("-1".equals(json.getString("code"))) {
            return FastJsonUtil.toJsonString(json);
        }

        try {
            resultInfo = storeService.updateStore(store, request);
        } catch (Exception e) {
            LOG.error("编辑美容店--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取员工最大编号
     *
     * @param storeId
     * @return java.lang.String
     * @datetime 2018/7/12 18:56
     * @author crn
     */
    @RequestMapping(value = "/getemployeemaxsno", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getEmployeeMaxSno(Long storeId) {
        JSONObject json = new JSONObject();

        if (null == storeId) {
            json.put("code", -1);
            json.put("msg", "请选择门店");
            return FastJsonUtil.toJsonString(json);
        }

        // 根据门店id查询门店信息
        Store store = storeService.getStoreById(storeId);
        if (null == store) {
            json.put("code", -1);
            json.put("msg", "门店信息错误");
            return FastJsonUtil.toJsonString(json);
        }
        String storeName = store.getName();
        storeName = PinyinUtil.cleanChar(storeName);
        String storeFirstSpell = PinyinUtil.converterToFirstSpell(storeName);

        // 根据门店id查询员工最大编号
        ResultInfo resultInfo = employeeService.getMaxEmployeeSno(storeId, storeFirstSpell);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加顾问
     *
     * @param employeeVO
     * @return java.lang.String
     * @datetime 2018/7/12 19:09
     * @author crn
     */
    @RequestMapping(value = "/savecounselor", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String saveCounselor(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = employeeService.saveCounselor(employeeVO, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加美容师
     *
     * @param employeeVO
     * @return java.lang.String
     * @method saveBeautician
     * @datetime 2018/7/12 19:56
     * @author crn
     */
    @RequestMapping(value = "/savebeautician", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String saveBeautician(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = employeeService.saveBeautician(employeeVO, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 根据门店id获取该门店顾问列表
     *
     * @param storeId
     * @return java.lang.String
     * @datetime 2018/7/12 20:02
     * @author crn
     */
    @RequestMapping(value = "/getcounselorsofstore", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getCounselorsOfStore(Long storeId) {
        JSONObject json = new JSONObject();

        if (null == storeId) {
            json.put("code", -1);
            json.put("msg", "请选择门店");
            return FastJsonUtil.toJsonString(json);
        }

        // 根据门店id查询门店信息
        Store store = storeService.getStoreById(storeId);
        if (null == store) {
            json.put("code", -1);
            json.put("msg", "门店信息错误");
            return FastJsonUtil.toJsonString(json);
        }

        // 根据门店id获取该门店顾问列表
        ResultInfo resultInfo = employeeService.getCounselorsOfStore(storeId);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑顾问信息
     *
     * @param employeeVO
     * @return java.lang.String
     * @datetime 2018/7/12 20:12
     * @author crn
     */
    @RequestMapping(value = "/updatecounselor", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String updateCounselor(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException {
        JSONObject json = new JSONObject();

        // 编辑更新顾问信息
        ResultInfo resultInfo = employeeService.updateCounselor(employeeVO, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑美容师信息
     *
     * @param employeeVO
     * @return java.lang.String
     * @datetime 2018/7/13 10:12
     * @author crn
     */
    @RequestMapping(value = "/updatebeautician", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String updateBeautician(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException {
        JSONObject json = new JSONObject();

        // 编辑更新顾问信息
        ResultInfo resultInfo = employeeService.updateBeautician(employeeVO, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取顾问详情
     *
     * @param id
     * @return java.lang.String
     * @datetime 2018/7/13 10:17
     * @author crn
     */
    @RequestMapping(value = "/getcounselordetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getCounselorDetail(Long id, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == id || 0 == id) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        ResultInfo resultInfo = employeeService.getCounselorDetailById(id, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取顾问当月佣金明细
     *
     * @param id       员工id
     * @param currPage
     * @param pageSize
     * @return java.lang.String
     * @datetime 2018/7/13 17:26
     * @author crn
     */
    @RequestMapping(value = "/listcounselorcurmonbonus", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listCounselorCurMonBonus(Long id, Integer currPage, Integer pageSize) {
        JSONObject json = new JSONObject();

        if (null == id || 0 == id) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }

        User user = userService.getUserByEmployeeId(id);
        if (null == user) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        PageResult<Map<String, Object>> counselorCurMonBonus = employeeService.listCounselorCurMonBonus(user.getId(), currPage, pageSize);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", counselorCurMonBonus);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取美容师详情
     *
     * @param id
     * @return java.lang.String
     * @datetime 2018/7/13 11:10
     * @author crn
     */
    @RequestMapping(value = "/getbeauticiandetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getBeauticianDetail(Long id, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == id || 0 == id) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        ResultInfo resultInfo = employeeService.getBeauticianDetailById(id, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取美容师当月佣金明细
     *
     * @param id       员工id
     * @param currPage
     * @param pageSize
     * @return java.lang.String
     * @datetime 2018/7/13 17:26
     * @author crn
     */
    @RequestMapping(value = "/listbeauticiancurmonbonus", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listBeauticianCurMonBonus(Long id, Integer currPage, Integer pageSize) {
        JSONObject json = new JSONObject();

        if (null == id || 0 == id) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }

        User user = userService.getUserByEmployeeId(id);
        if (null == user) {
            json.put("code", -1);
            json.put("msg", "信息有误");
            return FastJsonUtil.toJsonString(json);
        }
        PageResult<Map<String, Object>> beauticianCurMonBonus = employeeService.listCounselorCurMonBonus(user.getId(), currPage, pageSize);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", beauticianCurMonBonus);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 添加或者更新美容店的验证
     *
     * @param store
     * @return
     * @author ztl
     */
    public JSONObject verifyStore(Store store) {
        JSONObject json = new JSONObject();

        if (store == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return json;

        }

        if (StringUtils.isBlank(store.getName())) {
            json.put("code", -1);
            json.put("msg", "请输入美容店名称!");
            return json;
        }

        if (store.getAreaId() == null) {
            json.put("code", -1);
            json.put("msg", "请选择所属大区!");
            return json;
        }

        if (!StrUtil.isValidAreaname(store.getName())) {
            json.put("code", -1);
            json.put("msg", "请输入正确的名称!");
            return json;
        }

        if (storeService.getStoreByNameAndAreaId(store.getName(), store.getAreaId(), store.getType()) != null) {
            json.put("code", -1);
            json.put("msg", "该美容店名称已存在，请重新输入!");
            return json;
        }

        json.put("code", 1);
        json.put("msg", "验证成功!");
        return json;

    }

    /**
     * 添加或者更新大区的验证
     *
     * @param name
     * @return
     * @author ztl
     */
    public JSONObject verifyStoreArea(String name) {
        JSONObject json = new JSONObject();

        if (StringUtils.isBlank(name)) {
            json.put("code", -1);
            json.put("msg", "请输入大区名称!");
            return json;
        }

        if (!StrUtil.isValidAreaname(name)) {
            json.put("code", -1);
            json.put("msg", "请输入正确的名称!");
            return json;
        }

        if (storeAreaService.getStoreAreaByName(name) != null) {
            json.put("code", -1);
            json.put("msg", "该大区名称已存在，请重新输入!");
            return json;
        }

        json.put("code", 1);
        json.put("msg", "验证成功!");
        return json;
    }
}
