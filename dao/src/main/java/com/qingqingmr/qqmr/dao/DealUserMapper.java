package com.qingqingmr.qqmr.dao;

import java.util.List;

import com.qingqingmr.qqmr.domain.entity.DealUser;

/**
 * 用户交易记录
 *
 * @author ztl
 * @datetime 2018-7-4 10:27:43
 */
public interface DealUserMapper {
	/**
	 * 通过id删除
	 * 
	 * @author ztl
	 * @param id
	 * @return
	 */
	int deleteDealUserById(Long id);

	/**
	 * 保存用户交易记录
	 * 
	 * @author ztl
	 * @param record
	 * @return
	 */
	int saveDealUser(DealUser record);

	/**
	 * 通过id查询交易记录
	 * 
	 * @author ztl
	 * @param id
	 * @return
	 */
	DealUser getDealUserById(Long id);

	/**
	 * 更新交易记录
	 * 
	 * @author ztl
	 * @param record
	 * @return
	 */
	int updateDealUserById(DealUser record);

	/**
	 * <p>
	 * 分页查询交易记录
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午6:01:52
	 * @param userId
	 * @return
	 */
	List<DealUser> listDealUserPage(long userId);
}
