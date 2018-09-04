package com.qingqingmr.qqmr.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingqingmr.qqmr.base.bo.FeeSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.dao.PlatformFeeMapper;
import com.qingqingmr.qqmr.domain.bean.PlatformFeeVO;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.domain.entity.PlatformFee;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.PlatformService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 平台管理费实现类
 *
 * @author crn
 * @datetime 2018-07-12 11:19:14
 */
@Service
public class PlatformServiceImpl implements PlatformService {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Autowired
    private PlatformFeeMapper platformFeeMapper;
    @Autowired
    private EventSupervisorService eventSupervisorService;

    /**
     * 获取平台管理费列表
     *
     * @param feeSearchBO
     * @param request
     * @return
     * @author crn
     */
    @Override
    public PageResult<PlatformFeeVO> listPlatformFee(FeeSearchBO feeSearchBO, HttpServletRequest request) {
        Integer currPage = feeSearchBO.getCurrPage();
        Integer pageSize = feeSearchBO.getPageSize();
        currPage = null == currPage || currPage == 0 ? 1 : currPage;
        pageSize = null == pageSize || pageSize == 0 ? 10 : pageSize;
        PageResult<PlatformFeeVO> result = new PageResult<PlatformFeeVO>(currPage, pageSize);
        Page<PlatformFeeVO> page = PageHelper.startPage(currPage, pageSize);
        String type = feeSearchBO.getType();
        String typeStr = feeSearchBO.getTypeStr();

        //兼容搜索昵称是表情符号
        if ("2".equals(type) && StringUtils.isNotBlank(typeStr)) {
            feeSearchBO.setTypeStr(Security.encodeHexString(typeStr));
        }
        List<PlatformFeeVO> feeList = platformFeeMapper.listPlatformFee(feeSearchBO);

        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.SETTING_PAY_SHOW);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }
        result.setPage(feeList);
        result.setTotalCount(page.getTotal());
        return result;
    }

    /**
     * 保存平台管理费记录
     *
     * @param platformFee
     * @return
     */
    @Transactional
    @Override
    public int savePlatformFee(PlatformFee platformFee) {

        return platformFeeMapper.savePlatformFee(platformFee);
    }

}
