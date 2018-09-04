package com.qingqingmr.qqmr.service;

import java.util.List;
import java.util.Map;

/**
 * 省市区查询
 *
 * @author liujinjin
 * @datetime 2018年7月14日 下午1:55:59
 */
public interface DictAreaService {

    /**
     * 微信小程序：查询省市区域的信息
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    List<Map<String, Object>> getDictAreaInfo(Map<String, String> parameters);
}
