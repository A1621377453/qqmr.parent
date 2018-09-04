package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.bean.InviteMemberVO;
import com.qingqingmr.qqmr.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表
 *
 * @author liujinjin
 * @datetime 2018-7-4 11:13:33
 */
public interface UserInfoMapper {

    /**
     * 通过id删除
     *
     * @param id
     * @return
     * @author liujinjin
     */
    int deleteUserInfoById(Long id);

    /**
     * 保存用户信息
     *
     * @param userInfo
     * @return
     * @author liujinjin
     */
    int saveUserInfo(UserInfo userInfo);

    /**
     * 更新信息
     *
     * @param userInfo
     * @return
     * @author liujinjin
     */
    int updateUserInfo(UserInfo userInfo);

    /**
     * 获取用户累计邀请会员数量
     *
     * @param spreadId
     * @param type     2:会员
     * @return
     */
    int getUserSpreadsByUserId(@Param("spreadId") Long spreadId, @Param("type") Integer type);

    /**
     * 获取用户本月邀请的会员/顾客
     *
     * @param spreadId
     * @param type     1:顾客 2:会员
     * @return
     */
    int getUserCurMonSpreadsByUserId(@Param("spreadId") Long spreadId, @Param("type") Integer type);

    /**
     * 根据userid查询用户详细信息
     *
     * @param userId
     * @return
     */
    UserInfo getUserInfoByUserId(Long userId);

    /**
     * 选择性的根据id更新用户详细信息
     *
     * @param userInfo
     * @return
     */
    int updateUserInfoByIdSelective(UserInfo userInfo);

    /**
     * <p>
     * 分页查询用户邀请会员列表
     * </p>
     *
     * @param spreadId
     * @param type
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 上午11:34:23
     */
    List<InviteMemberVO> listInviteMemberPage(@Param("userId") Long spreadId, @Param("type") Integer type);

    /**
     * <p>
     * 获取用户累计邀请会员
     * </p>
     *
     * @param spreadId
     * @param type
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 下午4:33:09
     */
    int getUserInviteMember(@Param("userId") Long spreadId, @Param("type") Integer type);

    /**
     * <p>
     * 获取用户本月邀请会员
     * </p>
     *
     * @param spreadId
     * @param type
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 下午4:32:44
     */
    int getUserInviteMemberThisMon(@Param("userId") Long spreadId, @Param("type") Integer type);


    /**
     * 微信小程序：手机号修改
     *
     * @param userId
     * @param mobile
     * @return
     * @author liujinjin
     */
    int updateUserMobile(@Param("userId") Long userId, @Param("mobile") String mobile);

    /**
     * 根据门店id获取会员信息
     *
     * @param storeId
     * @param key
     * @return
     */
    List<Map<String, Object>> listUserByStoreId(@Param("storeId") Long storeId, @Param("key") String key);

    /**
     * 根据userId获取用户直接邀请人信息
     *
     * @param userId
     * @return
     */
    InviteMemberVO getInviterInfo(@Param("userId") Long userId);

    /**
     * 根据手机号获取用户信息
     * @param mobile
     * @return
     */
    UserInfo getUserInfoByMobile(String mobile);

    /**
     * 根据邀请人id查询邀请人信息
     * @param spreadId
     * @return
     */
    List<UserInfo> listUserInfoBySpreadId(Long spreadId);
}