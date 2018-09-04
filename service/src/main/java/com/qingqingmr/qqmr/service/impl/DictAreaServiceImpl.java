package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.dao.DictAreaMapper;
import com.qingqingmr.qqmr.service.DictAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liujinjin
 * @datetime 2018年7月14日 下午2:00:37
 */
@Service
public class DictAreaServiceImpl implements DictAreaService {

    @Autowired
    private DictAreaMapper dictAreaMapper;

    /**
     * 微信小程序：查询省市区域的信息
     *
     * @param parameters
     * @return
     * @author liujinjin
     */
    @Override
    public List<Map<String, Object>> getDictAreaInfo(Map<String, String> parameters) {
        return dictAreaMapper.getAllCityListByProOrDict(parameters.get("parentId"));
    }

}
