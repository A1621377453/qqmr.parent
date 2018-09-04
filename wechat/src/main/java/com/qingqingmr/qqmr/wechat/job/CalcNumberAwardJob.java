package com.qingqingmr.qqmr.wechat.job;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.service.UserBonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 计算人头奖定时任务
 *
 * @author ythu
 * @datetime 2018-07-19 14:09:23
 */
@Component
public class CalcNumberAwardJob {
    private static final Logger LOG = LoggerFactory.getLogger(CalcNumberAwardJob.class);

    @Autowired
    private UserBonusService userBonusService;

    /**
     * 每月的1号00:00:00执行
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void execute() {
        LOG.info("定时任务计算人头奖开始");
        ResultInfo resultInfo = null;
        try {
            resultInfo = userBonusService.calcNumberAward();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("执行结果：code={}，msg={}", -1, e.getMessage());
        }
        LOG.info("执行结果：code={}，msg={}", resultInfo.getCode(), resultInfo.getMsg());
        LOG.info("定时任务计算人头奖结束");
    }
}
