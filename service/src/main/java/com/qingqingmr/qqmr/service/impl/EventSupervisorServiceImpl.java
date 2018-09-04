package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.util.BaseHttpUtil;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.dao.EventSupervisorMapper;
import com.qingqingmr.qqmr.domain.bean.SupervisorCurrentVO;
import com.qingqingmr.qqmr.domain.bean.SupervisorEventLogVO;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.SupervisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作日志
 *
 * @author ztl
 * @datetime 2018-07-04 16:03:23
 */
@Service
public class EventSupervisorServiceImpl implements EventSupervisorService {
    private static final Logger LOG = LoggerFactory.getLogger(EventSupervisorServiceImpl.class);

    @Autowired
    private EventSupervisorMapper eventSupervisorMapper;
    @Autowired
    private SupervisorService supervisorService;

    /**
     * 保存管理员事件记录
     *
     * @return
     * @author ztl
     */
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public ResultInfo saveEventSupervisor(HttpServletRequest request, EventSupervisor.SupervisorEventEnum supervisorEvent) {
        ResultInfo info = new ResultInfo();

        SupervisorCurrentVO currentSupervisor = supervisorService.getSupervisorCurrent(request);
        EventSupervisor eventsupervisor = new EventSupervisor();

        if (currentSupervisor != null) {
            String ip = BaseHttpUtil.getIp(request);
            eventsupervisor.setTime(new Date());
            eventsupervisor.setSupervisorId(currentSupervisor.getId());
            eventsupervisor.setType(supervisorEvent.getCode());
            eventsupervisor.setIp(ip);
            eventsupervisor.setRemark(supervisorEvent.getValue());
        }

        if (eventSupervisorMapper.saveEventSupervisor(eventsupervisor) < 0) {
            throw new ServiceRuntimeException("添加异常", true);
        }

        info.setInfo(1, "添加成功");
        return info;
    }

    /**
     * 查询操作日志列表
     *
     * @param currPage
     * @param pageSize
     * @param map
     * @param request
     * @return
     * @author ztl
     */
    @Override
    public ResultInfo listSupervisorEventLogPage(Integer currPage, Integer pageSize, Map<String, Object> map, HttpServletRequest request) {
        ResultInfo info = new ResultInfo();
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<SupervisorEventLogVO> result = new PageResult<SupervisorEventLogVO>(currPage, pageSize);

        Page<SupervisorEventLogVO> page = PageHelper.startPage(currPage, pageSize);
        List<SupervisorEventLogVO> listSupervisorEventLog = eventSupervisorMapper.listSupervisorEventLogPage(map);

        result.setPage(listSupervisorEventLog);
        result.setTotalCount(page.getTotal());

        //添加管理员操作事件
        try {
            info = saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.OPERATIONLOG_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        info.setInfo(1, "查询成功");
        info.setObj(result);
        return info;
    }
}
