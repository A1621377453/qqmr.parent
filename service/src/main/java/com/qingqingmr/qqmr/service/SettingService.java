package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.base.bo.DistributionRuleBO;
import com.qingqingmr.qqmr.common.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 规则设置
 *
 * @author ztl
 * @datetime 2018-07-06 20:32:50
 */
public interface SettingService {
    /**
     * 运营规则--根据key值查询value
     *
     * @param key
     * @return
     * @author ztl
     */
    String getSettingValueByKey(String key);

    /**
     * 运营规则--根据key更新对象value值
     *
     * @param maps
     * @return
     * @author ztl
     */
    ResultInfo updateSettings(Map<String, String> maps, HttpServletRequest request);

    /**
     * 运营规则--查询所有的信息
     *
     * @return
     * @author ztl
     */
    Map<String, String> listAllSetting();

    /**
     * 查找所有的分销规则并存入redis
     *
     * @return
     * @author ztl
     */
    Map<String, List<DistributionRuleBO>> listAllDistributionVORule();

    /**
     * 查找所有的分销规则
     *
     * @return
     * @author ztl
     */
    Map<String, String> listAllDistributionRule();

    /**
     * 编辑分销规则
     *
     * @author ztl
     */
    ResultInfo updateDistributionRule(String distributionRules, HttpServletRequest request);
}
