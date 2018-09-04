package com.qingqingmr.qqmr.wechat.job;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.service.UserBonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 返还到期奖金
 *
 * @author ythu
 * @datetime 2018-07-19 17:56:50
 */
@Component
public class ReturnExpireAwardJob {
    private final static Logger LOG = LoggerFactory.getLogger(ReturnExpireAwardJob.class);

    @Autowired
    private UserBonusService userBonusService;

    /**
     * 每天00:00:01执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void execute() {
        LOG.info("定时任务返还到期奖金开始");
        ResultInfo resultInfo = null;
        try {
            resultInfo = userBonusService.returnExpireAward();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("执行异常结果：code={}，msg={}", -1, e.getMessage());
        }
        LOG.info("执行结果：code={}，msg={}", resultInfo.getCode(), resultInfo.getMsg());
        LOG.info("定时任务返还到期奖金结束");
    }
}
