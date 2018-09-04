package com.qingqingmr.qqmr.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author crn
 * @datetime 2018-08-23 10:53:58
 */
@Slf4j
public class SyncTest {
    @Test
    public void test1(){
       synchronized (this){
           for (int i = 0; i < 10; i++) {
               log.info("I'm sectionVO : {}", toString());
           }
       }
    }
}
