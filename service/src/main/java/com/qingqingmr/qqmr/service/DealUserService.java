package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.domain.entity.DealUser;

/**
 * 用户交易记录
 *
 * @author ztl
 * @datetime 2018-7-4 14:17:43
 */
public interface DealUserService {

    /**
     * 保存用户交易记录
     *
     * @param dealUser
     * @author ztl
     */
    int saveDealUser(DealUser dealUser);

    /**
     * <p>
     * 分页查询用户交易记录
     * </p>
     *
     * @param currPage
     * @param pageSize
     * @return
     * @author liujingjing
     * @datetime 2018年7月4日 下午5:47:09
     */
    PageResult<DealUser> listDealUserPage(int currPage, int pageSize, long userId);

}
