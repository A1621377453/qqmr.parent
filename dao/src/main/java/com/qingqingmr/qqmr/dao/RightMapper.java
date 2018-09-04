package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.Right;

import java.util.List;

/**
 * 系统权限表mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 13:52:25
 */
public interface RightMapper {
    /**
     * 根据id删除权限
     *
     * @param id
     * @return
     */
    int deleteRightById(Long id);

    /**
     * 保存权限
     *
     * @param record
     * @return
     */
    int saveRight(Right record);

    /**
     * 根据id查询权限
     *
     * @param id
     * @return
     */
    Right getRightById(Long id);

    /**
     * 更新权限
     *
     * @param record
     * @return
     */
    int updateRightById(Right record);

    /**
     *  查询所有的模块
     *
     * @author ztl
     * @datetime 2018-7-5 19:28:24
     * @return
     */
     List<Right> getAllRightByModule(Long moduleId);
}