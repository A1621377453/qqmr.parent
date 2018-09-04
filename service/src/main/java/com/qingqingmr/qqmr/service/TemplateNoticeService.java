package com.qingqingmr.qqmr.service;

import com.qingqingmr.qqmr.domain.entity.TemplateNotice;

/**
 * <p>
 * 短信模板
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月18日 上午11:10:05
 */
public interface TemplateNoticeService {
	/**
	 * <p>
	 * 查询短信
	 * </p>
	 *
	 * @param scene
	 * @param type
	 * @return
	 * @author liujingjing
	 * @datetime 2018年7月17日 下午6:53:21
	 */
	TemplateNotice getBySceneType(int scene, int type);
}
