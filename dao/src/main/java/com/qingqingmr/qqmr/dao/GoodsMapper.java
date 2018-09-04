package com.qingqingmr.qqmr.dao;

import com.qingqingmr.qqmr.domain.entity.Goods;

/**
 * 商品列表
 * @author ztl
 * @datetime 2018-7-4 11:36:16
 */
public interface GoodsMapper {
    /**
     * 通过id删除商品
     * @author ztl
     * @param id
     * @returnd
     */
    int deleteGoodsById(Long id);

    /**
     * 保存商品
     * @author ztl
     * @param record
     * @return
     */
    int saveGoods(Goods record);

    /**
     * 通过id查询商品
     * @author ztl
     * @param id
     * @return
     */
    Goods getGoodsById(Long id);

    /**
     * 更新商品
     * @author ztl
     * @param record
     * @return
     */
    int updateGoodsById(Goods record);
    
    /**
     * 查询会员卡商品信息
     * @author liujinjin
	 * @param
	 * @return
     */
    Goods getGoodsInfo();
}