package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.Store;
import com.qingqingmr.qqmr.domain.entity.StoreArea;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 门店区域接口类
 *
 * @author crn
 * @datetime 2018-07-05 18:58:33
 */
public interface StoreAreaService {

    /**
     * 查询所有门店区域信息
     *
     * @return
     */
    List<StoreArea> listStoreAreas();

    /**
     * 通过大区名称查询大区信息
     *
     * @return
     * @author ztl
     */
    StoreArea getStoreAreaByName(String name);

    /**
     * 通过大区id查询大区信息
     *
     * @return
     * @author ztl
     */
    StoreArea getStoreAreaById(Long id);

    /**
     * 保存大区信息
     *
     * @param name
     * @param request
     * @return
     * @author ztl
     */
    ResultInfo saveStoreArea(String name, HttpServletRequest request);

    /**
     * 更新区域信息
     *
     * @param id
     * @param name
     * @param request
     * @return
     * @author ztl
     */
    ResultInfo updateStoreArea(Long id, String name, HttpServletRequest request);

    /**
     * 查询所有的大区和美容店
     *
     * @param request
     * @return
     * @author ztl
     */
    Map<String, List<Store>> listStoreAndAreas(HttpServletRequest request);
}
