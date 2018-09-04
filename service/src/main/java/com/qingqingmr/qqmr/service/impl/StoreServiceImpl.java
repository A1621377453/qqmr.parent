package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.dao.StoreMapper;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.domain.entity.Store;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 门店实现类
 *
 * @author crn
 * @datetime 2018-07-05 18:58:51
 */
@Service
public class StoreServiceImpl implements StoreService {
    private static final Logger LOG = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private EventSupervisorService eventSupervisorService;

    /**
     * 根据区域id查询门店列表
     *
     * @param areaId
     * @return
     * @author crn
     */
    @Override
    public List<Store> listStores(Long areaId) {
        return storeMapper.listStores(areaId);
    }


    /**
     * 保存美容店信息
     *
     * @param store
     * @param request
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo saveStore(Store store, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();

        if (storeMapper.saveStore(store) < 0) {
            throw new ServiceRuntimeException("保存美容店信息失败", true);
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.FRAME_SHOP_ADD);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setInfo(1, "保存美容店信息成功");
        result.setObj(result);
        return result;
    }

    /**
     * 查询要编辑的美容店信息
     *
     * @param id
     * @return
     * @author ztl
     */
    @Override
    public Store getStoreById(Long id) {
        return storeMapper.getStoreById(id);
    }

    /**
     * 编辑美容店
     *
     * @param store
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public ResultInfo updateStore(Store store, HttpServletRequest request) {
        ResultInfo result = new ResultInfo();

        if (storeMapper.updateStoreById(store) < 0) {
            throw new ServiceRuntimeException("更新美容店信息失败", true);
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.FRAME_SHOP_Edit);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setInfo(1, "更新美容店信息成功");
        result.setObj(result);
        return result;
    }

    /**
     * 根据area和name查询美容店信息
     *
     * @param name
     * @param areaId
     * @param type
     * @return
     * @author ztl
     */
    @Override
    public Store getStoreByNameAndAreaId(String name, Long areaId, Integer type) {
        return storeMapper.getStoreByNameAndAreaId(name, areaId, type);
    }

	/**
	 * 微信小程序：通过用户id查询门店名称
	 * 
	 * @param userId
	 * @return
	 * @author liujinjin
	 */
	@Override
	public String getStoreNameByUserId(Long userId) {
		return storeMapper.getStoreNameByUserId(userId).getName();
	}
}
