package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.exception.ServiceRuntimeException;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.dao.MembershipCardMapper;
import com.qingqingmr.qqmr.dao.MembershipCardUseDetailMapper;
import com.qingqingmr.qqmr.domain.entity.MembershipCard;
import com.qingqingmr.qqmr.domain.entity.MembershipCardUseDetail;
import com.qingqingmr.qqmr.service.MembershipCardUseDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 会员卡使用详情实现类
 *
 * @author crn
 * @datetime 2018-07-10 17:33:21
 */
@Service
public class MembershipCardUseDetailServiceImpl implements MembershipCardUseDetailService {
	private static final Logger LOG = LoggerFactory.getLogger(MembershipCardUseDetailServiceImpl.class);
	@Autowired
	private MembershipCardUseDetailMapper cardUseDetailMapper;
	@Autowired
	private MembershipCardMapper membershipCardMapper;

	/**
	 * 根据卡id获取会员卡使用记录
	 * 
	 * @method listCardUse
	 * @param cardId
	 * @param currPage
	 * @param pageSize
	 * @datetime 2018/7/10 17:38
	 * @author crn
	 * @return com.qingqingmr.qqmr.common.ResultInfo
	 */
	@Override
	public PageResult<MembershipCardUseDetail> listCardUse(Long cardId, Integer currPage, Integer pageSize) {

		currPage = null == currPage || currPage == 0 ? 1 : currPage;
		pageSize = null == pageSize || pageSize == 0 ? 10 : pageSize;
		PageResult<MembershipCardUseDetail> result = new PageResult<MembershipCardUseDetail>(currPage, pageSize);
		Page<MembershipCardUseDetail> page = PageHelper.startPage(currPage, pageSize);
		List<MembershipCardUseDetail> cardUseDetails = cardUseDetailMapper.listCardUse(cardId);

		result.setPage(cardUseDetails);
		result.setTotalCount(page.getTotal());
		return result;
	}

	/**
	 * 添加服务记录
	 * 
	 * @param userId
	 * @param time
	 * @param content
	 * @param remark
	 * @return
	 * @author liujinjin
	 */
	@Transactional
	@Override
	public ResultInfo savaMembershipCardUseDetail(Long userId, String content, String remark,Long cardId) {
		ResultInfo resultInfo = new ResultInfo();
		
		try {
			MembershipCard membershipCard = membershipCardMapper.getMembershipCardById(cardId);
			if(membershipCard == null) {
				resultInfo.setInfo(-1, "未查出会员卡信息");
				return resultInfo;
			}
			MembershipCardUseDetail membershipCardUseDetail = new MembershipCardUseDetail(new Date(), cardId, userId,
					content, remark);
			int rows = cardUseDetailMapper.saveCardUseDetail(membershipCardUseDetail);
			if (rows < 1) {
				LOG.error("新增服务记录失败");
				resultInfo.setInfo(-1, "新增服务记录失败");
				return resultInfo;
			}

			boolean isActive = membershipCard.getIsActive();
			if (isActive == false) {
				rows = membershipCardMapper.updateMembershipCardIsActiveById(cardId);
				if (rows < 1) {
					LOG.error("更新会员卡激活状态失败");
					resultInfo.setInfo(-1, "更新会员卡激活状态失败");
					return resultInfo;
				}
			}
		} catch (Exception e) {
			LOG.error("添加服务记录时，发生异常--{}", e.getMessage());
			throw new ServiceRuntimeException("添加服务记录时，发生异常", true);
		}
		resultInfo.setInfo(1, "添加服务记录成功");
		return resultInfo;
	}
}
