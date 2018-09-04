package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.dao.DealUserMapper;
import com.qingqingmr.qqmr.domain.entity.DealUser;
import com.qingqingmr.qqmr.service.DealUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户交易记录
 *
 * @author ztl
 * @datetime 2018-7-4 14:17:43
 */
@Service
public class DealUserServiceImpl implements DealUserService {

    @Autowired
    private DealUserMapper dealUserMapper;


    /**
     * 保存交易记录
     *
     * @param dealUser
     * @return
     * @author ztl
     */
    @Override
    @Transactional
    public int saveDealUser(DealUser dealUser) {
        int rows = dealUserMapper.saveDealUser(dealUser);
        if (rows < 0) {
            throw new ServiceRuntimeException("保存交易记录异常", true);
        }
        return rows;
    }

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
    @Override
    public PageResult<DealUser> listDealUserPage(int currPage, int pageSize, long userId) {
        currPage = currPage < 1 ? 1 : currPage;
        pageSize = pageSize < 1 ? 10 : pageSize;
        PageResult<DealUser> result = new PageResult<DealUser>(currPage, pageSize);

        Page<DealUser> page = PageHelper.startPage(currPage, pageSize);
        List<DealUser> listDealUserPages = dealUserMapper.listDealUserPage(userId);

        result.setPage(listDealUserPages);
        result.setTotalCount(page.getTotal());

        return result;
    }


}
