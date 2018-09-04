package com.qingqingmr.qqmr.wechat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {

    @Test
    public void test01() {
        Logger logger = LoggerFactory.getLogger(LogbackTest.class);
        logger.info("test01");
    }
}