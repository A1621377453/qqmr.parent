package com.qingqingmr.qqmr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingqingmr.qqmr.dao.TemplateNoticeMapper;
import com.qingqingmr.qqmr.domain.entity.TemplateNotice;
import com.qingqingmr.qqmr.service.TemplateNoticeService;

/**
 * <p>
 * 短信模板
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月18日 上午11:11:36
 */
@Service
public class TemplateNoticeServiceImpl implements TemplateNoticeService {
	@Autowired
	private TemplateNoticeMapper templateNoticeMapper;

	/**
	 * <p>
	 * 查询短信模板
	 * </p>
	 *
	 * @param scene
	 * @param type
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月17日 下午6:53:21
	 */
	@Override
	public TemplateNotice getBySceneType(int scene, int type) {
		return templateNoticeMapper.getBySceneType(scene, type);
	}

}
