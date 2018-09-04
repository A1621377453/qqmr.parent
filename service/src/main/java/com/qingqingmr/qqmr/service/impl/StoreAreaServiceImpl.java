package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.dao.StoreAreaMapper;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.domain.entity.Store;
import com.qingqingmr.qqmr.domain.entity.StoreArea;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.StoreAreaService;
import com.qingqingmr.qqmr.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店区域实现类
 *
 * @author crn
 * @datetime 2018-07-05 18:58:51
 */
@Service
public class StoreAreaServiceImpl implements StoreAreaService {
    private static final Logger LOG = LoggerFactory.getLogger(StoreAreaServiceImpl.class);

    @Autowired
    private StoreAreaMapper storeAreaMapper;
    @Autowired
    private StoreService storeService;
    @Autowired
    private EventSupervisorService eventSupervisorService;

    /**
     * 查询所有门店信息
     *
     * @return
     * @author crn
     */
    @Override
    public List<StoreArea> listStoreAreas() {
        return storeAreaMapper.listStoreAreas();
    }

    /**
     * 通过大区名称查询大区信息
     *
     * @return
     * @author ztl
     */
    @Override
    public StoreArea getStoreAreaByName(String name) {
        return storeAreaMapper.getStoreAreaByName(name);
    }

    /**
     * 通过大区id查询大区信息
     *
     * @return
     * @author ztl
     */
    @Override
    public StoreArea getStoreAreaById(Long id) {
        return storeAreaMapper.getStoreAreaById(id);
    }

    /**
     * 保存大区信息
     *
     * @param name
     * @param request
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo saveStoreArea(String name, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();

        if (storeAreaMapper.saveStoreArea(name) < 0) {
            throw new ServiceRuntimeException("保存大区信息失败", true);
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.FRAME_DICT_ADD);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setInfo(1, "保存大区信息成功");
        result.setObj(result);
        return result;
    }

    /**
     * 更新区域信息
     *
     * @param id
     * @param name
     * @param request
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo updateStoreArea(Long id, String name, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();

        if (storeAreaMapper.updateStoreAreaById(id, name) < 0) {
            throw new ServiceRuntimeException("更新大区信息失败", true);
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.FRAME_DICT_EDIT);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setInfo(1, "更新大区信息成功");
        result.setObj(result);
        return result;
    }

    /**
     * 查询所有的大区和美容店
     *
     * @param request
     * @return
     * @author ztl
     */
    @Override
    public Map<String, List<Store>> listStoreAndAreas(HttpServletRequest request) {
        Map<String, List<Store>> map = new HashMap<>();
        List<StoreArea> storeAreas = listStoreAreas();

        for (StoreArea storeArea : storeAreas) {
            List<Store> storeList = storeService.listStores(storeArea.getId());
            map.put(storeArea.getName() + storeArea.getId(), storeList);
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.FRAME_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        return map;
    }


}
