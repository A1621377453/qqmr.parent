package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.RightRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色表mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 13:52:25
 */
public interface RightRoleMapper {
    /**
     * 根据id删除权限角色
     *
     * @param id
     * @return
     */
    int deleteRightRoleById(Long id);

    /**
     * 添加权限角色
     * @author ztl
     * @param name
     * @param description
     * @return
     */
    int saveRightRole(@Param("name") String name, @Param("description") String description);

    /**
     * 根据id查询权限角色
     *
     * @param id
     * @return
     */
    RightRole getRightRoleById(Long id);

    /**
     * 根据id更新权限角色
     *
     * @param record
     * @return
     */
    int updateRightRoleById(RightRole record);

    /**
     * 根据姓名查询角色信息
     * @author ztl
     * @param name
     * @return
     */
    RightRole getRightRoleByName(String name);

    /**
     * 查询角色列表
     * @author ztl
     * @return
     */
    List<RightRole> listRightRolePage();

    /**
     * 查询所有的角色id和名称
     * @author ztl
     * @return
     */
    List<Map<String,Object>> listAllRole();
}