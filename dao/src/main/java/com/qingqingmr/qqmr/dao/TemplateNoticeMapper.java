package com.qingqingmr.qqmr.dao;

import org.apache.ibatis.annotations.Param;

import com.qingqingmr.qqmr.domain.entity.TemplateNotice;

/**
 * 短信模板
 *
 * @author liujingjing
 * @datetime 2018年7月17日 下午6:53:00
 */
public interface TemplateNoticeMapper {

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
    TemplateNotice getBySceneType(@Param("scene") int scene, @Param("type") int type);
}