package com.qingqingmr.qqmr.dao;

import java.util.List;

/**
 * 权限资源表mapper接口
 *
 * @author crn
 * @datetime 2018-7-4 13:52:25
 */
public interface RightResourceMapper {
    List<Long> getRightByResource(String resource);
}