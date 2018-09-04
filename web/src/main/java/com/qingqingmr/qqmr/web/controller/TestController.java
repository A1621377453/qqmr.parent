package com.qingqingmr.qqmr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test01")
    @ResponseBody
    public String test01() {
        return "qqmr";
    }

}
