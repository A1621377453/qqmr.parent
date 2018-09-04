package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.base.bo.EmployeeSearchBO;
import com.qingqingmr.qqmr.domain.bean.EmployeeVO;
import com.qingqingmr.qqmr.domain.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 员工列表
 *
 * @author ztl
 * @datetime 2018-7-4 10:27:43
 */
public interface EmployeeMapper {
    /**
     * 通过id删除员工
     * @author ztl
     * @param id
     * @return
     */
    int deleteEmployeeById(Long id);

    /**
     * 保存员工
     * @author ztl
     * @param record
     * @return
     */
    int saveEmployee(Employee record);

    /**
     * 通过id查询员工基本信息
     * @author ztl
     * @param id
     * @return
     */
    Employee getEmployeeBaseInfoById(Long id);

    /**
     * 通过id查询员工
     * @author ztl
     * @param id
     * @return
     */
    EmployeeVO getEmployeeById(Long id);

    /**
     * 更新员工
     * @author ztl
     * @param record
     * @return
     */
    int updateEmployeeById(Employee record);

    /**
     * 根据搜索条件查询顾问列表
     * @param employee
     * @return
     */
    List<EmployeeVO> listEmployees(EmployeeSearchBO employee);

    /**
     * 根据门店id获取最大员工编号
     * @return
     */
    String getMaxEmployeeSno(long storeId);

    /**
     * 根据员工编号获取员工信息
     * @param sno
     * @return
     */
    Employee getEmployeeBySno(String sno);

    /**
     * 根据员工编号获取员工数量
     * @param sno
     * @return
     */
    int countEmployeeBySno(String sno);

    /**
     * 根据身份证号获取员工数量
     * @param idNumber
     * @return
     */
    int countEmployeeByidNumber(String idNumber);

    /**
     * 选择性的根据id更新员工信息
     * @param employee
     * @return
     */
    int updateEmployeeByIdSelective(Employee employee);

    /**
     * 根据门店id获取该门店顾问列表
     * @param storeId
     * @return
     */
    List<Employee> getCounselorsOfStore(Long storeId);

    /**
     * 获取用户当月佣金列表信息
     * @param userId
     * @return
     */
    List<Map<String, Object>> listUserCurMonBonus(Long userId);

    /**
     * 获取美容师所属顾问信息
     * @param id
     * @return
     */
    EmployeeVO getCounselorOfBeautician(Long id);

    /**
     * 根据userId查询员工信息
     * @param userId
     * @return
     */
    EmployeeVO getEmployeeByUserId(Long userId);

    /**
     * 微信小程序：修改员工手机号
     * @author liujinjin
     * @param Employee
     * @return
     */
    int updateEmployeeMobile(Employee employee);

    /**
     * 根据门店id获取门店员工
     * @param storeId
     * @param type
     * @param key
     * @return
     */
    List<Map<String,Object>> listEmployeeByStoreId(@Param("storeId") Long storeId, @Param("type")Integer type, @Param("key") String key);

    /**
     * 根据手机号查询员工信息
     * @param mobile
     * @return
     */
    Employee getEmployeeByMobile(String mobile);
}