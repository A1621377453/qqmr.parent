package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 操作日志
 * @author ztl
 * @datetime 2018-07-04 16:02:46
 */
public interface EventSupervisorService {
    /**
     * 保存管理员事件记录
     *
     * @return
     * @author ztl
     */
    ResultInfo saveEventSupervisor(HttpServletRequest request, EventSupervisor.SupervisorEventEnum supervisorEvent);

    /**
     * 查询操作日志列表
     *
     * @param currPage
     * @param pageSize
     * @param request
     * @return
     * @author ztl
     */
    ResultInfo listSupervisorEventLogPage(Integer currPage, Integer pageSize, Map<String, Object> map, HttpServletRequest request);
}
