package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.base.bo.FeeSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.domain.bean.PlatformFeeVO;
import com.qingqingmr.qqmr.domain.entity.PlatformFee;

import javax.servlet.http.HttpServletRequest;

/**
 * 平台管理费
 *
 * @author crn
 * @datetime 2018-07-12 11:18:45
 */
public interface PlatformService {

    /**
     * 获取平台管理费列表
     *
     * @param feeSearchBO
     * @param request
     * @return
     */
    PageResult<PlatformFeeVO> listPlatformFee(FeeSearchBO feeSearchBO, HttpServletRequest request);

    /**
     * 保存平台管理费记录
     *
     * @param platformFee
     * @return
     */
    int savePlatformFee(PlatformFee platformFee);
}
