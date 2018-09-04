package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.base.bo.FeeSearchBO;
import com.qingqingmr.qqmr.domain.bean.PlatformFeeVO;
import com.qingqingmr.qqmr.domain.bean.ResultVO;
import com.qingqingmr.qqmr.domain.entity.PlatformFee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台管理费mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 13:52:25
 */
public interface PlatformFeeMapper {
    /**
     * 根据id删除平台管理费
     *
     * @param id
     * @return
     */
    int deletePlatformFeeById(Long id);

    /**
     * 添加平台管理费
     *
     * @param record
     * @return
     */
    int savePlatformFee(PlatformFee record);

    /**
     * 根据id查询平台管理费
     *
     * @param id
     * @return
     */
    PlatformFee getPlatformFeeById(Long id);

    /**
     * 更新平台管理费
     *
     * @param record
     * @return
     */
    int updatePlatformFeeById(PlatformFee record);

    /**
     * 获取平台费用
     *
     * @param timeBegin
     * @param timeEnd
     * @return
     */
    List<ResultVO> getPlatformFee(@Param("timeBegin") String timeBegin, @Param("timeEnd") String timeEnd);

    /**
     * 获取收取费用列表
     * @return
     * @param feeSearchBO
     */
    List<PlatformFeeVO> listPlatformFee(FeeSearchBO feeSearchBO);
}