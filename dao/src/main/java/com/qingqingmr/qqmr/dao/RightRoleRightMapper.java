package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.RightRoleRight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限角色
 * 
 * @author liujingjing
 * @datetime 2018年7月4日 上午10:50:37
 */
public interface RightRoleRightMapper {

	/**
	 * <p>
	 * 增加权限角色
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 上午10:54:01
	 * @param rightRoleRight
	 * @return
	 */
	int saveRightRoleRight(RightRoleRight rightRoleRight);

	/**
	 * <p>
	 * 通过roleId查询权限角色
	 * </p>
	 *
	 * @author ztl
	 * @datetime 2018-7-5 20:35:00
	 * @param roleId
	 * @return
	 */
	List<Long> getRightRoleRightByRoleId(Long roleId);

	/**
	 * <p>
	 * 删除权限角色
	 * </p>
	 *
	 * @author ztl
	 * @datetime 2018-7-5 20:31:40
	 * @param rightId
	 * @param roleId
	 * @return
	 */
	int deleteRightRoleRightByRoleId(@Param("rightId") Long rightId, @Param("roleId") Long roleId);

}