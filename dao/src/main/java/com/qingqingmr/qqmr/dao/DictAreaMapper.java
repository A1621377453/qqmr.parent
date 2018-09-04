package com.qingqingmr.qqmr.dao;

import java.util.List;
import java.util.Map;

/**
 * 省市区
 * @author ztl
 * @datetime 2018-7-4 13:57:18
 */
public interface DictAreaMapper {
    /**
     * 查询中国所有的省份
     *
     * @author ztl
     * @return
     */
    List<Map<String,Object>> getAllProList();

    /**
     * 根据省份编号(或者区号)查询所有城市(或者城镇)
     * @author ztl
     * @param num
     * @return
     */
    List<Map<String,Object>> getAllCityListByProOrDict(String num);
}