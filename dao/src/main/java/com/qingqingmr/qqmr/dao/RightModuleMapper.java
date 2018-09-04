package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.RightModule;

import java.util.List;
import java.util.Map;

/**
 * 系统权限模块mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 13:52:25
 */
public interface RightModuleMapper {
    /**
     * 根据id删除权限模块
     *
     * @param id
     * @return
     */
    int deleteRightModuleById(Long id);

    /**
     * 添加系统模块
     *
     * @param record
     * @return
     */
    int saveRightModule(RightModule record);

    /**
     * 根据id查询系统模块
     *
     * @param id
     * @return
     */
    RightModule getRightModuleById(Long id);

    /**
     * 更新系统模块
     *
     * @param record
     * @return
     */
    int updateRightModuleById(RightModule record);

    /**
     *  查询所有的模块
     *
     * @author ztl
     * @datetime 2018-7-5 19:28:24
     * @return
     */
    List<Map<String,Object>> listAllModule();
}