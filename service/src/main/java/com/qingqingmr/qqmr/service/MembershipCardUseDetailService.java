package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.domain.entity.MembershipCardUseDetail;

/**
 * 会员卡使用详情接口类
 * @author crn
 * @datetime 2018-07-10 17:32:48
 */
public interface MembershipCardUseDetailService {

    /**
     * 根据卡id获取会员卡使用记录
     * @param cardId
     * @param currPage
     * @param pageSize
     * @return
     */
    PageResult<MembershipCardUseDetail> listCardUse(Long cardId, Integer currPage, Integer pageSize);
    
    /**
     *添加服务记录 
     * @param userId
     * @param time
     * @param content
     * @param remark
     * @return
     * @author liujinjin
     */
	ResultInfo savaMembershipCardUseDetail(Long userId, String content, String remark, Long cardId);
}
