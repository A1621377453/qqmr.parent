package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.DistributionRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销规则设置
 *
 * @author ztl
 * @datetime 2018-7-4 10:27:43
 */
public interface DistributionRuleMapper {
    /**
     * 更新分销规则
     *
     * @return
     * @author ztl
     */
    int updateDistributionRule(@Param("role") Integer role, @Param("type") Integer type, @Param("rule") String rule);

    /**
     * 查找所有的分销规则
     *
     * @return
     * @author ztl
     */
    List<DistributionRule> listAllDistributionRule();
}