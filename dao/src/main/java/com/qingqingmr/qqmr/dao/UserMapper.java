package com.qingqingmr.qqmr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qingqingmr.qqmr.base.bo.UserSearchBO;
import com.qingqingmr.qqmr.domain.bean.UserSaveVO;
import com.qingqingmr.qqmr.domain.bean.UserVO;
import com.qingqingmr.qqmr.domain.entity.User;

/**
 * 用户
 *
 * @author liujingjing
 * @datetime 2018年7月4日 下午2:20:30
 */
public interface UserMapper {
	/**
	 * <p>
	 * 根据id删除用户
	 * </p>
	 *
	 * @param id
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午2:22:01
	 */
	int deleteUserById(Long id);

	/**
	 * <p>
	 * 添加用户
	 * </p>
	 *
	 * @param userSaveVO
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午2:22:26
	 */
	int saveUser(UserSaveVO userSaveVO);

	/**
	 * <p>
	 * 根据id查询用户信息
	 * </p>
	 *
	 * @param id
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午2:22:44
	 */
	User getUserById(Long id);

	/**
	 * <p>
	 * 修改用户信息
	 * </p>
	 *
	 * @param nickName
	 * @param photo
	 * @param openId
	 * @param id
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月4日 下午2:23:26
	 */
	int updateUserById(@Param("nickName") String nickName, @Param("photo") String photo, @Param("wxOpenId") String openId,
			@Param("id") long id);

	/**
	 * 根据员工id查询用户信息
	 *
	 * @param employeeId
	 * @return
	 */
	User getUserByEmployeeId(Long employeeId);

	/**
	 * 选择性的根据userid更新用户信息
	 *
	 * @param record
	 * @return
	 */
	int updateUserByIdSelective(User record);

	/**
	 * 通过手机号查询用户信息
	 *
	 * @param mobile 手机号码
	 * @return
	 * @author liujinjin
	 */
	User getUserByMobile(String mobile);

	/**
	 * 通过手机号修改用户表里的角色
	 *
	 * @param mobile 手机号码
	 * @return
	 * @author liujinjin
	 */
	int updateUserRoleType(String mobile);

	/**
	 * 通过用户id修改用户表里的角色
	 *
	 * @param roleType 会员类型
	 * @param id       用户id
	 * @return
	 * @author liujinjin
	 */
	int updateUserRoleTypeById(@Param("roleType") int roleType, @Param("id") long id);

	/**
	 * 昵称修改
	 *
	 * @param userId
	 * @param nickName
	 * @return
	 * @author liujinjin
	 */
	int updateUserNickName(@Param("userId")Long userId, @Param("nickName")String nickName);

	/**
	 * 微信小程序：性别修改
	 *
	 * @param userId
	 * @param sex
	 * @return
	 * @author liujinjin
	 */
	int updateUserSex(@Param("userId")Long userId, @Param("sex")String sex);

	/**
	 * 获取用户列表
	 *
	 * @param userSearchBo
	 * @return
	 */
	List<UserVO> listUser(UserSearchBO userSearchBo);

	/**
	 * 微信小程序：手机号修改
	 *
	 * @param userId
	 * @param mobile
	 * @return
	 * @author liujinjin
	 */
	int updateUserMobile(@Param("userId")Long userId, @Param("mobile")String mobile);

	/**
	 * 微信小程序：设置交易密码
	 *
	 * @param userId
	 * @param payPassword
	 * @return
	 * @author liujinjin
	 */
	int updateUserPayPassword(@Param("userId")Long userId, @Param("payPassword")String payPassword);

	/**
	 * 获取平台指定用户人数
	 *
	 * @return
	 */
	Long countUser(@Param("type") Integer type, @Param("timeBegin") String timeBegin, @Param("timeEnd") String timeEnd);

	/**
	 * 微信小程序：通过微信openId获取用户信息
	 *
	 * @param openId
	 * @return
	 * @author liujinjin
	 */
	User getUserByWxOpenId(String openId);

	/**
	 * 根据userid查询用户邀请信息
	 * 
	 * @param userId
	 * @return
	 */
	UserVO getUserinviteInfoByUserId(Long userId);
}