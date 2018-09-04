package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.bean.SupervisorCurrentVO;
import com.qingqingmr.qqmr.domain.entity.Supervisor;

import java.util.List;

/**
 * 管理员
 * 
 * @author liujingjing
 * @datetime 2018年7月4日 下午1:52:54
 */
public interface SupervisorMapper {

	/**
	 * <p>
	 * 根据id删除管理员
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午1:59:29
	 * @param id
	 * @return
	 */
	int deleteSupervisorById(Long id);

	/**
	 * <p>
	 * 添加管理员
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午1:59:56
	 * @param supervisor
	 * @return
	 */
	int saveSupervisor(Supervisor supervisor);

	/**
	 * <p>
	 * 根据id进行查询
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午2:00:34
	 * @param id
	 * @return
	 */
	Supervisor getSupervisorById(Long id);

	/**
	 * <p>
	 * 修改管理员信息
	 * </p>
	 *
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午2:01:02
	 * @param supervisor
	 * @return
	 */
	int updateSupervisorById(Supervisor supervisor);

	/**
	 * 根据用户名查询管理员信息
	 * @param name
	 * @return
	 */
	Supervisor getSupervisorByName(String name);

	/**
	 * 更新管理员信息
	 * @param record
	 * @return
	 */
	int updateSupervisorByIdSelective(Supervisor record);

	/**
	 * 查询管理员列表
	 * @author ztl
	 * @return
	 */
	List<SupervisorCurrentVO> listSupervisorPage();

	/**
	 * 通过mobile查询管理员信息
	 * @author ztl
	 * @return
	 */
	Supervisor getSupervisorByMobile(String mobile);

	/**
	 * 查询当前登录的管理员的信息包括权限
	 * @param id
	 * @return
	 */
	SupervisorCurrentVO getSupervisorCurrentById(Long id);

	/**
	 * 获取后台首页管理员信息
	 * @param id
	 * @return
	 */
    SupervisorCurrentVO getSupervisorHomeInfo(Long id);
}