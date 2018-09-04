package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.base.bo.UserBonusSearchBO;
import com.qingqingmr.qqmr.domain.bean.ResultVO;
import com.qingqingmr.qqmr.domain.bean.UserBonusVO;
import com.qingqingmr.qqmr.domain.entity.UserBonus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户奖金表
 *
 * @author liujinjin
 * @datetime 2018-7-4 11:13:33
 */
public interface UserBonusMapper {

    /**
     * 通过id删除
     *
     * @param id
     * @return
     * @author liujinjin
     */
    int deleteUserBonusById(Long id);

    /**
     * 保存用户奖金信息
     *
     * @param userBonus
     * @return
     * @author liujinjin
     */
    int saveUserBonus(UserBonus userBonus);

    /**
     * 通过id查询用户奖金信息
     *
     * @param id
     * @return
     * @author liujinjin
     */
    UserBonus getUserBonusById(Long id);

    /**
     * 更新用户奖金信息
     *
     * @param userBonus
     * @return
     * @author liujinjin
     */
    int updateUserBonus(UserBonus userBonus);

    /**
     * 根据userid查询用户佣金总额
     *
     * @param userId
     * @return
     */
    Double getUserAmountByUserId(Long userId);

    /**
     * 获取用户当月 直接/间接 佣金
     *
     * @param userId
     * @param role      用户角色
     * @param type   1:直接 0:间接
     * @return
     */
    Double getUserCurMonAmountByUserId(@Param("userId") Long userId, @Param("role") Integer role, @Param("type") Integer type);

    /**
     * <p>
     * 分页查询用户佣金明细
     * </p>
     *
     * @param userId
     * @return
     * @author liujingjing
     * @datetime 2018年7月6日 下午6:09:08
     */
    List<UserBonus> listUserBonusPage(long userId);

    /**
     * 获取平台角色奖励
     *
     * @param timeBegin
     * @param timeEnd
     * @return
     */
    List<ResultVO> getRoleAmount(@Param("timeBegin") String timeBegin, @Param("timeEnd") String timeEnd);

    /**
     * 获取平台佣金列表
     *
     * @param bonusSearchBO
     * @return
     */
    List<UserBonusVO> listBonus(UserBonusSearchBO bonusSearchBO);

    /**
     * 查询所有到期佣金
     *
     * @return
     */
    List<Map<String, Object>> listReturnExpire(int period);

    /**
     * 更新所有到期佣金标识和时间
     *
     * @return
     */
    int updateReturnExpire(int period);
}