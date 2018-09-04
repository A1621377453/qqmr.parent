package com.qingqingmr.qqmr.wechat.job;

import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 自动取消订单
 * </p>
 *
 * @author liujingjing
 * @datetime 2018年7月9日 上午11:53:27
 */
@Component
public class CancelOrderJob {
    private static final Logger LOG = LoggerFactory.getLogger(CancelOrderJob.class);

    @Autowired
    private OrderService orderService;

    /**
     * 每一分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void execute() {
        LOG.info("定时任务自动取消订单开始");
        ResultInfo resultInfo = new ResultInfo();
        try {
            resultInfo = orderService.autoCancelOrder();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("执行异常结果：code={}，msg={}", -1, e.getMessage());
        }
        LOG.info("执行结果：code={}，msg={}", resultInfo.getCode(), resultInfo.getMsg());
        LOG.info("定时任务自动取消订单结束");
    }
}
