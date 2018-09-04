package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.RightRoleSupervisor;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员角色
 * 
 * @author liujingjing
 * @datetime 2018年7月4日 上午11:15:13
 */
public interface RightRoleSupervisorMapper {
	/**
	 * <p>
	 * 通过id删除管理员角色
	 * </p>
	 *
	 * @author ztl
	 * @datetime 2018年7月4日 上午11:20:24
	 * @param supervisorId
	 * @return
	 */
	int deleteRightRoleSupervisorById(Long supervisorId);

	/**
	 * <p>
	 * 添加管理员角色
	 * </p>
	 *
	 * @author ztl
	 * @datetime 2018-7-5 14:51:30
	 * @param supervisorId 管理员id roleId 角色id
	 * @return
	 */
	int saveRightRoleSupervisor(@Param("supervisorId") Long supervisorId, @Param("roleId") Long roleId);

	/**
	 * <p>
	 * 通过supervisorId查询管理员角色
	 * </p>
	 *
	 * @author ztl
	 * @datetime 2018-7-6 11:19:03
	 * @param supervisorId
	 * @return
	 */
	RightRoleSupervisor getRightRoleSupervisorBySupervisorId(Long supervisorId);

	/**
	 * <p>
	 * 修改管理员角色
	 * </p>
	 *
	 * @author ztl
	 * @param supervisorId roleId
	 * @return
	 */
	int updateRightRoleSupervisorById(@Param("supervisorId") Long supervisorId, @Param("roleId") Long roleId);

	/**
	 * 统计某个角色对应的人数
	 * @author ztl
	 * @param roleId
	 * @return
	 */
	Integer countRightRoleSupervisorByRoleId(Long roleId);

}