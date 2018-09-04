package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.Store;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 门店接口类
 *
 * @author crn
 * @datetime 2018-07-05 18:58:33
 */
public interface StoreService {

    /**
     * 根据区域id查询门店列表
     *
     * @param areaId
     * @return
     */
    List<Store> listStores(Long areaId);

    /**
     * 保存美容店信息
     *
     * @param store
     * @param request
     * @return
     * @author ztl
     */
    ResultInfo saveStore(Store store, HttpServletRequest request);

    /**
     * 查询要编辑的美容店信息
     *
     * @param id
     * @return
     * @author ztl
     */
    Store getStoreById(Long id);

    /**
     * 编辑美容店
     *
     * @param store
     * @return
     * @author ztl
     */
    ResultInfo updateStore(Store store, HttpServletRequest request);

    /**
     * 根据area和name查询美容店信息
     *
     * @param name
     * @param areaId
     * @return
     * @author ztl
     */
    Store getStoreByNameAndAreaId(String name, Long areaId, Integer type);


    /**
     * 微信小程序：通过用户id查询门店名称
     *
     * @return
     * @author liujinjin
     */
    String getStoreNameByUserId(Long userId);
}
