package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.ResultInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 平台概况接口类
 *
 * @author crn
 * @datetime 2018-07-11 17:38:44
 */
public interface PlatformOverviewService {

    /**
     * 获取平台概况
     *
     * @return
     */
    ResultInfo getPlatformOverview(String timeBegin, String timeEnd, HttpServletRequest request);

    /**
     * 获取当日平台概况
     * @return
     */
    ResultInfo getDayPlatformOverview();
}
