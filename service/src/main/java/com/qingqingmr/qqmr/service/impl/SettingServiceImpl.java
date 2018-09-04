package com.qingqingmr.qqmr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qingqingmr.qqmr.base.bo.DistributionRuleBO;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemRedisKeyConstant;
import com.qingqingmr.qqmr.common.enums.AwardTypeEnum;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.dao.DistributionRuleMapper;
import com.qingqingmr.qqmr.dao.SettingPlatformMapper;
import com.qingqingmr.qqmr.domain.entity.DistributionRule;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.SettingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztl
 * @datetime 2018-07-06 20:35:11
 */
@Service
public class SettingServiceImpl implements SettingService {
    private static final Logger LOG = LoggerFactory.getLogger(SettingServiceImpl.class);

    @Autowired
    private SettingPlatformMapper settingPlatformMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SpringContextProperties springContextProperties;
    @Autowired
    private EventSupervisorService eventSupervisorService;
    @Autowired
    private DistributionRuleMapper distributionRuleMapper;

    /**
     * 运营规则--根据key查询
     *
     * @param key
     * @return
     * @author ztl
     */
    @Override
    public String getSettingValueByKey(String key) {
        Object obj = redisTemplate.opsForHash()
                .get(springContextProperties.getProfile() + SystemRedisKeyConstant.PLATFORM_SETTING, key);
        if (obj != null)
            return obj.toString();
        listAllSetting();
        return settingPlatformMapper.getSettingValueByKey(key);
    }

    /**
     * 运营规则--修改平台设置参数
     *
     * @param map
     * @return
     * @author ztl
     */
    @Transactional
    @Override
    public ResultInfo updateSettings(Map<String, String> map, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();
        if (map == null || map.keySet() == null || map.keySet().size() == 0) {
            resultInfo.setInfo(-1, "运营规则参数为空");
            return resultInfo;
        }

        Object obj = null;
        int rows = 0;
        for (String strKey : map.keySet()) {
            obj = redisTemplate.opsForHash().get(springContextProperties.getProfile() + SystemRedisKeyConstant.PLATFORM_SETTING,
                    strKey);
            if (obj != null && obj.toString().equals(map.get(strKey)))
                continue;

            rows = settingPlatformMapper.updateSettingByKey(strKey, map.get(strKey));
            if (rows < 1) {
                redisTemplate.delete(springContextProperties.getProfile() + SystemRedisKeyConstant.PLATFORM_SETTING);
                throw new ServiceRuntimeException("更新运营规则失败", true);
            }

            redisTemplate.opsForHash().put(springContextProperties.getProfile() + SystemRedisKeyConstant.PLATFORM_SETTING,
                    strKey, map.get(strKey));
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.RUN_RULE);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        resultInfo.setInfo(1, "更新成功");
        return resultInfo;
    }

    /**
     * 查询平台所有的设置信息
     *
     * @author ztl
     */
    @Override
    public Map<String, String> listAllSetting() {
        String key = springContextProperties.getProfile() + SystemRedisKeyConstant.PLATFORM_SETTING;
        if (redisTemplate.hasKey(key)) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            Map<String, String> tmp = new HashMap<>();
            for (Object obj : entries.keySet()) {
                tmp.put(obj.toString(), entries.get(obj).toString());
            }
            return tmp;
        }

        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> list = settingPlatformMapper.listAllSetting();
        for (Map<String, String> tmp : list) {
            map.put(tmp.get("_key"), tmp.get("_value"));
        }
        hashOperations.putAll(springContextProperties.getProfile() + SystemRedisKeyConstant.PLATFORM_SETTING, map);
        return map;
    }

    /**
     * 查找所有的分销规则并存入redis
     *
     * @return
     * @author ztl
     */
    @Override
    public Map<String, List<DistributionRuleBO>> listAllDistributionVORule() {
        String key = springContextProperties.getProfile() + SystemRedisKeyConstant.SALE_RULE_SETTING;
        if (redisTemplate.hasKey(key)) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            Map<String, List<DistributionRuleBO>> mapList = new HashMap<>();
            for (Object obj : entries.keySet()) {
                String[] str = obj.toString().split(":");
                Integer role = Integer.parseInt(str[1]);
                Integer type = Integer.parseInt(str[2]);
                List<DistributionRuleBO> boList = FastJsonUtil.parseListObj(entries.get(obj).toString(), DistributionRuleBO.class);
                boList = disList(boList, role, type);

                mapList.put(DistributionRule.DistriRoleEnum.getEnum(role).value + AwardTypeEnum.getAwardType(type).value, boList);
            }
            return mapList;
        }

        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Map<String, String> map = new HashMap<>();
        List<DistributionRule> list = distributionRuleMapper.listAllDistributionRule();
        Map<String, List<DistributionRuleBO>> distributionRuleMap = new HashMap<>();
        for (DistributionRule distributionRule : list) {
            List<DistributionRuleBO> distributionRuleVOList = JSONArray.parseArray(distributionRule.getRule(), DistributionRuleBO.class);
            distributionRuleVOList = disList(distributionRuleVOList, distributionRule.getRole(), distributionRule.getType());
            distributionRuleMap.put(DistributionRule.DistriRoleEnum.getEnum(distributionRule.getRole()).value + AwardTypeEnum.getAwardType(distributionRule.getType()).value, distributionRuleVOList);

            map.put(SystemRedisKeyConstant.SALE_RULE_PREFIX + distributionRule.getRole() + ":" + distributionRule.getType(), distributionRule.getRule());
        }
        hashOperations.putAll(springContextProperties.getProfile() + SystemRedisKeyConstant.SALE_RULE_SETTING, map);

        return distributionRuleMap;
    }

    /**
     * 为distributionRuleVOList赋值
     *
     * @param distributionRuleVOList
     * @param role
     * @param type
     * @return
     */
    public List<DistributionRuleBO> disList(List<DistributionRuleBO> distributionRuleVOList, Integer role, Integer type) {
        String name = null;
        for (int i = 0; i < distributionRuleVOList.size(); i++) {
            if (distributionRuleVOList.size() > 1) {
                name = DistributionRule.DistriRoleEnum.getEnum(role).value + AwardTypeEnum.getAwardType(type).value + i;
            } else {
                name = DistributionRule.DistriRoleEnum.getEnum(role).value + AwardTypeEnum.getAwardType(type).value;
            }
            distributionRuleVOList.get(i).setName(name);
            distributionRuleVOList.get(i).setType(type);
            distributionRuleVOList.get(i).setRole(role);
        }
        return distributionRuleVOList;
    }

    /**
     * 查找所有的分销规则
     *
     * @return
     * @author ztl
     */
    @Override
    public Map<String, String> listAllDistributionRule() {
        String key = springContextProperties.getProfile() + SystemRedisKeyConstant.SALE_RULE_SETTING;
        if (redisTemplate.hasKey(key)) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            Map<String, String> mapList = new HashMap<>();
            for (Object obj : entries.keySet()) {
                mapList.put(obj.toString(), entries.get(obj).toString());
            }
            return mapList;
        }

        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Map<String, String> map = new HashMap<>();
        List<DistributionRule> list = distributionRuleMapper.listAllDistributionRule();
        for (DistributionRule distributionRule : list) {
            map.put(SystemRedisKeyConstant.SALE_RULE_PREFIX + distributionRule.getRole() + ":" + distributionRule.getType(), distributionRule.getRule());
        }
        hashOperations.putAll(springContextProperties.getProfile() + SystemRedisKeyConstant.SALE_RULE_SETTING, map);
        return map;
    }

    /**
     * 编辑分销规则
     *
     * @author ztl
     */
    @Transactional
    @Override
    public ResultInfo updateDistributionRule(String distributionRules, HttpServletRequest request) {
        ResultInfo resultInfo = new ResultInfo();

        if (StringUtils.isBlank(distributionRules)) {
            resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
            return resultInfo;
        }

        Map mapType = JSON.parseObject(distributionRules, Map.class);
        Map<String, String> map = new HashMap<>();
        Map<String, List<DistributionRuleBO>> distributionRuleMap = new HashMap<>();
        for (Object obj : mapType.keySet()) {
            List<DistributionRuleBO> listDis = JSONArray.parseArray(mapType.get(obj).toString(), DistributionRuleBO.class);

            if (listDis.size() < 1) {
                resultInfo.setInfo(-1, ResultInfo.STR_NULL_OBJ);
                return resultInfo;
            }
            map.put("RULE:" + listDis.get(0).getRole() + ":" + listDis.get(0).getType(), listDis.toString());
            distributionRuleMap.put(obj.toString(), listDis);
        }

        if (map == null || map.keySet() == null || map.keySet().size() == 0) {
            resultInfo.setInfo(-1, "分销规则参数为空");
            return resultInfo;
        }

        Object obj = null;
        int rows = 0;
        for (String strKey : map.keySet()) {
            obj = redisTemplate.opsForHash().get(springContextProperties.getProfile() + SystemRedisKeyConstant.SALE_RULE_SETTING,
                    strKey);
            if (obj != null && obj.toString().equals(map.get(strKey)))
                continue;

            String[] split = strKey.split(":");
            Integer role = Integer.parseInt(split[1]);
            Integer type = Integer.parseInt(split[2]);
            String rule = map.get(strKey);
            rows = distributionRuleMapper.updateDistributionRule(role, type, rule);
            if (rows < 1) {
                redisTemplate.delete(springContextProperties.getProfile() + SystemRedisKeyConstant.SALE_RULE_SETTING);
                throw new ServiceRuntimeException("更新分销失败", true);
            }

            redisTemplate.opsForHash().put(springContextProperties.getProfile() + SystemRedisKeyConstant.SALE_RULE_SETTING,
                    strKey, map.get(strKey));
        }

        //添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.SALE_RULE);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        resultInfo.setInfo(1, "更新成功");
        return resultInfo;
    }

}
