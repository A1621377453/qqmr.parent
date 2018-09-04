package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.MembershipCardUseDetail;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 会员卡使用详情
 *
 * @author ztl
 * @datetime 2018-7-4 11:53:59
 */
public interface MembershipCardUseDetailMapper {
    /**
     * 通过id删除会员卡使用信息
     * @author ztl
     * @param id
     * @return
     */
    int deleteCardUseDetailById(Long id);

    /**
     * 保存会员卡使用信息
     * @param record
     * @author ztl
     * @return
     */
    int saveCardUseDetail(MembershipCardUseDetail record);

    /**
     * 通过id查询会员卡使用信息
     * @author ztl
     * @param id
     * @return
     */
    MembershipCardUseDetail getCardUseDetailById(Long id);

    /**
     * 更新会员卡使用信息
     * @author ztl
     * @param record
     * @return
     */
    int updateCardUseDetailById(MembershipCardUseDetail record);

    /**
     * 根据卡id获取服务记录
     * @param cardId
     * @return
     */
    List<MembershipCardUseDetail> listCardUse(@Param("cardId") Long cardId);
}