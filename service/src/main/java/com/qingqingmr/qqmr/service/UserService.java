package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.base.bo.UserSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.enums.RoleTypeEnum;
import com.qingqingmr.qqmr.domain.bean.InviteMemberVO;
import com.qingqingmr.qqmr.domain.bean.UserVO;
import com.qingqingmr.qqmr.domain.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author liujingjing
 * @datetime 2018年7月5日 下午8:07:23
 */
public interface UserService {
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
     * 微信小程序：添加微信用户的用户表记录
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    ResultInfo saveBindWXUser(Map<String, String> parameters);

    /**
     * 微信小程序：通过手机号查询用户信息
     *
     * @param mobile 手机号码
     * @return
     * @author liujinjin
     */
    User getUserByMobile(String mobile);

    /**
     * 微信小程序：通过手机号修改用户表里的角色
     *
     * @param mobile 手机号码
     * @return
     * @author liujinjin
     */
    int updateUserRoleType(String mobile);

    /**
     * 微信小程序：通过用户id修改用户表里的角色
     *
     * @param roleTypeEnum 会员类型
     * @param id           用户id
     * @return
     * @author liujinjin
     */
    int updateUserRoleTypeById(RoleTypeEnum roleTypeEnum, long id);

    /**
     * <p>
     * 分页查询用户邀请会员列表
     * </p>
     *
     * @param currPage
     * @param pageSize
     * @param userId
     * @param type
     * @return
     * @author liujingjing
     * @datetime 2018年7月9日 上午10:18:31
     */
    PageResult<InviteMemberVO> listInviteMemberPage(int currPage, int pageSize, long userId, int type);

    /**
     * 微信小程序：昵称修改
     *
     * @param userId
     * @param nickName
     * @return
     * @author liujinjin
     */
    int updateUserNickName(Long userId, String nickName);

    /**
     * 微信小程序：性别修改
     *
     * @param userId
     * @param sex
     * @return
     * @author liujinjin
     */
    int updateUserSex(Long userId, String sex);

    /**
     * 微信小程序：通过用户id修改手机号
     *
     * @param userId
     * @param mobile
     * @return
     * @author liujinjin
     */
    ResultInfo updateUserMobile(Long userId, String mobile);

    /**
     * 微信小程序：通过用户id修改交易密码
     *
     * @param userId
     * @param payPassword
     * @param oldPayPassword
     * @return
     * @author liujinjin
     */
    ResultInfo updatePsyPasswordById(Long userId, String payPassword,String oldPayPassword);

    /**
     * 获取注册会员列表
     *
     * @param userSearchBo
     * @param request
     * @return
     */
    PageResult<UserVO> listUser(UserSearchBO userSearchBo, HttpServletRequest request);

    /**
     * 微信小程序：校验用户输入的交易密码是否正确
     *
     * @param userId
     * @param payPassword
     * @return
     * @author liujinjin
     */
    int checkPsyPassword(Long userId, String payPassword);

    /**
     * 获取用户详细信息
     *
     * @param type
     * @param userId
     * @param request
     * @return com.qingqingmr.qqmr.common.ResultInfo
     * @method getUserDetail
     * @datetime 2018/7/10 17:27
     * @author crn
     */
    ResultInfo getUserDetail(Integer type, Long userId, HttpServletRequest request) throws UnsupportedEncodingException;

    /**
     * 获取用户当月佣金记录
     *
     * @param userId
     * @param currPage
     * @param pageSize
     * @return
     */
    PageResult<Map<String, Object>> listUserCurMonBonus(Long userId, Integer currPage, Integer pageSize);

    /**
     * 根据门店id获取门店用户
     *
     * @param type
     * @param storeId
     * @param key
     * @return
     */
    ResultInfo listUserByStoreId(Integer type, Long storeId, String key);

    /**
     * 微信小程序：通过微信openId获取用户信息
     *
     * @param openId
     * @return
     * @author liujinjin
     */
    User getUserByWxOpenId(String openId);

    /**
     * 根据员工id获取用户信息
     *
     * @param id
     * @return
     */
    User getUserByEmployeeId(Long id);

    /**
     * <p>
     * 验证验证码
     * </p>
     *
     * @param verificationCode
     * @param mobile
     * @param scene
     * @return
     * @author liujingjing
     * @datetime 2018年7月18日 上午11:34:20
     */
    ResultInfo verifyCode(String verificationCode, String mobile, String scene);
}
