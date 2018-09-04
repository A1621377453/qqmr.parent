package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.StoreArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店区域
 * 
 * @author liujingjing
 * @datetime 2018年7月4日 上午11:54:02
 */
public interface StoreAreaMapper {
	/**
	 * <p>
	 * 通过id删除门店区域
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 上午11:55:44
	 * @param id
	 * @return
	 */
	int deleteStoreAreaById(Long id);

	/**
	 * <p>
	 * 添加门店区域
	 * </p>
	 *
	 * @author ztl
	 * @datetime 2018-7-6 17:03:34
	 * @param name
	 * @return
	 */
	int saveStoreArea(String name);

	/**
	 * <p>
	 * 通过id查询门店区域
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 上午11:56:33
	 * @param id
	 * @return
	 */
	StoreArea getStoreAreaById(Long id);

	/**
	 * <p>
	 * 修改区域信息
	 * </p>
	 *
	 * @author ztl
	 * @datetime 2018-7-6 17:15:47
	 * @return
	 */
	int updateStoreAreaById(@Param("id") Long id, @Param("name") String name);

	/**
	 * 查询所有门店信息
	 * @return
	 */
	List<StoreArea> listStoreAreas();

	/**
	 * 通过区域名称查询大区信息
	 * @author ztl
	 * @param name
	 * @return
	 */
	StoreArea getStoreAreaByName(String name);

	/**
	 * 根据门店id获取所属区域
	 * @param id
	 * @return
	 */
    StoreArea getStoreAreaByStoreId(Long id);
}