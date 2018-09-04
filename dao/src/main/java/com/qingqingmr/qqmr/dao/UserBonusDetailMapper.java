package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.UserBonusDetail;

import java.util.List;
import java.util.Map;

/**
 * 用户奖金明细表
 *
 * @author liujinjin
 * @datetime 2018-7-4 11:13:33
 */
public interface UserBonusDetailMapper {

    /**
     * 通过id删除
     *
     * @param id
     * @return
     * @author liujinjin
     */
    int deleteUserBonusDetailById(Long id);

    /**
     * 保存用户奖金明细
     *
     * @param userBonusDetail
     * @return
     * @author liujinjin
     */
    int saveUserBonusDetail(UserBonusDetail userBonusDetail);

    /**
     * 通过id查询用户奖金明细
     *
     * @param id
     * @return
     * @author liujinjin
     */
    UserBonusDetail getUserBonusDetailById(Long id);

    /**
     * 根据用户id分组查询用户上个月某种类型的邀请情况
     *
     * @return
     */
    List<Map<String, Long>> listAllUserLastMonthBonusDetailGroupByType(int type);
}