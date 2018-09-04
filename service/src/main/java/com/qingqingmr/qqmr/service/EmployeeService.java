package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.base.bo.EmployeeSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.bean.EmployeeVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 员工列表接口类
 *
 * @author crn
 * @datetime 2018-07-05 11:16:21
 */
public interface EmployeeService {

    /**
     * 获取顾问列表
     *
     * @param employeeSearchBo
     * @param request
     * @return
     */
    PageResult<EmployeeVO> listCounselors(EmployeeSearchBO employeeSearchBo, HttpServletRequest request);

    /**
     * 根据门店id获取最大员工编号
     *
     * @return
     */
    ResultInfo getMaxEmployeeSno(long storeId, String storeFirstSpell);

    /**
     * 添加顾问
     *
     * @param employeeVO
     * @param request
     * @return
     */
    ResultInfo saveCounselor(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException;

    /**
     * 编辑更新顾问信息
     *
     * @param employeeVO
     * @param request
     * @return
     */
    ResultInfo updateCounselor(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException;

    /**
     * 编辑更新美容师信息
     *
     * @param employeeVO
     * @param request
     * @return
     */
    ResultInfo updateBeautician(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException;

    /**
     * 获取美容师列表
     *
     * @param employeeSearchBo
     * @param request
     * @return
     */
    PageResult<EmployeeVO> listBeauticians(EmployeeSearchBO employeeSearchBo, HttpServletRequest request);

    /**
     * 根据门店id获取该门店顾问列表
     *
     * @param storeId
     * @return
     */
    ResultInfo getCounselorsOfStore(Long storeId);

    /**
     * 添加美容师
     *
     * @param employeeVO
     * @param request
     * @return
     */
    ResultInfo saveBeautician(EmployeeVO employeeVO, HttpServletRequest request) throws ParseException;

    /**
     * 获取顾问详细信息
     *
     * @param id
     * @param request
     * @return
     */
    ResultInfo getCounselorDetailById(Long id, HttpServletRequest request);

    /**
     * 获取美容师详情
     *
     * @param id
     * @param request
     * @return
     */
    ResultInfo getBeauticianDetailById(Long id, HttpServletRequest request);

    /**
     * 获取顾问当月佣金列表
     *
     * @param userId
     * @param currPage
     * @param pageSize
     * @return
     */
    PageResult<Map<String, Object>> listCounselorCurMonBonus(Long userId, Integer currPage, Integer pageSize);

    /**
     * 根据员工id获取员工信息
     * @param id
     * @return
     */
    ResultInfo getEmployeeInfo(Long id);

    /**
     * 上传员工头像
     * @param img
     * @param id
     * @return
     */
    ResultInfo imageUpload(MultipartFile img, Long id);
}
