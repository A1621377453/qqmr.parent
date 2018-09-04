package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.bean.SupervisorEventLogVO;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 管理员事件记录
 *
 * @author ztl
 * @datetime 2018-7-4 10:27:43
 */
public interface EventSupervisorMapper {
    /**
     * 保存管理员事件记录
     *
     * @param record
     * @return
     * @author ztl
     */
    int saveEventSupervisor(EventSupervisor record);

    /**
     * 查询操作日志列表
     *
     * @param map
     * @return
     * @author ztl
     */
    List<SupervisorEventLogVO> listSupervisorEventLogPage(Map<String, Object> map);

    /**
     * 根据管理员id查询操作日志信息
     *
     * @param supervisorId
     * @param remark
     * @return
     */
    EventSupervisor getLastLoginInfo(@Param("supervisorId") Long supervisorId, @Param("remark") String remark);
}