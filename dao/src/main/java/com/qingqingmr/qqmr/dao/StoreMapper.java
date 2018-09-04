package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.Store;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午1:41:32
 */
public interface StoreMapper {
    /**
     * <p>
     * 根据id删除门店
     * </p>
     *
     * @param id
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午1:42:37
     */
    int deleteStoreById(Long id);

    /**
     * <p>
     * 添加门店
     * </p>
     *
     * @param store
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午1:44:02
     */
    int saveStore(Store store);

    /**
     * <p>
     * 根据id查询门店
     * </p>
     *
     * @param id
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午1:44:36
     */
    Store getStoreById(Long id);

    /**
     * <p>
     * 修改门店
     * </p>
     *
     * @param store
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午1:44:58
     */
    int updateStoreById(Store store);

    /**
     * 根据区域id查询门店列表
     *
     * @param areaId
     * @return
     */
    List<Store> listStores(Long areaId);

    /**
     * 通过用户id查询门店信息
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    Store getStoreNameByUserId(Long userId);

    /**
     * 根据area和name查询美容店信息
     *
     * @param name
     * @param areaId
     * @return
     * @author ztl
     */
    Store getStoreByNameAndAreaId(@Param("name") String name, @Param("areaId") Long areaId, @Param("type") Integer type);
}