package com.qingqingmr.qqmr.service.impl;

import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.util.Arith;
import com.qingqingmr.qqmr.dao.*;
import com.qingqingmr.qqmr.domain.entity.*;
import com.qingqingmr.qqmr.service.MembershipCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 会员卡信息
 *
 * @author liujinjin
 * @datetime 2018年7月5日 下午6:22:32
 */
@Service
public class MembershipCardServiceImpl implements MembershipCardService {
    private final static Logger LOG = LoggerFactory.getLogger(MembershipCardServiceImpl.class);

    @Autowired
    private MembershipCardMapper membershipCardMapper;
    @Autowired
    private StoreMapper storeMapper;

    /**
     * 微信小程序：根据用户id查询会员卡信息
     *
     * @param userId
     * @return
     */
    @Override
    public MembershipCard getMembershipCard(Long userId) {

        return membershipCardMapper.getMembershipCardNoInfoByUserId(userId);
    }

    /**
     * 微信小程序：获取将要产生的会员卡卡号
     *
     * @param userId
     * @return
     * @author liujinjin
     */
    @Override
    public String getMembershipCardCode(Long userId) {
        Store store = storeMapper.getStoreNameByUserId(userId);
        Long storeId = store.getId();
        Long areaId = store.getAreaId();

        StringBuffer buffer = new StringBuffer();
        buffer.append(Arith.formatLong(areaId)).append(Arith.formatLong(storeId));
        Long count = membershipCardMapper.countMembershipCardNoPrefix(buffer.toString());
        if (count > 0) {
            buffer.append(Arith.formatLong(count + 1));
        } else {
            buffer.append("0001");
        }
        return buffer.toString();
    }

    /**
     * 微信小程序：微信绑定的用户，下单购买会员卡，添加会员卡记录
     *
     * @param userId
     * @param cardNo
     * @param isPayFor
     * @param orderId
     * @param remark
     * @return
     * @author liujinjin
     */
    @Transactional
    @Override
    public int saveMembershipCard(long userId, String cardNo, boolean isPayFor, long orderId, String remark) {
        int rows = -1;
        try {
            MembershipCard membershipCard = new MembershipCard();
            membershipCard.setTime(new Date());
            membershipCard.setUserId(userId);
            membershipCard.setCardNo(cardNo);
            membershipCard.setIsActive(false);
            membershipCard.setIsPayFor(isPayFor);
            membershipCard.setOrderId(orderId);
            membershipCard.setIsExpire(false);
            membershipCard.setRemark(remark);
            rows = membershipCardMapper.saveMembershipCard(membershipCard);
        } catch (Exception e) {
        	LOG.error("绑定微信绑定表时出现异常--{}", e.getMessage());
            throw new ServiceRuntimeException("微信绑定新增异常", true);
        }
        
        if(rows < 1) {
        	LOG.error("新增会员卡记录失败");
        	rows = -1;
        }
        return rows;
    }
}
