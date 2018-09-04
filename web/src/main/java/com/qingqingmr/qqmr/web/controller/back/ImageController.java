package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.service.EmployeeService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片处理控制器
 *
 * @author crn
 * @datetime 2018-07-05 17:33:40
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping("/image")
public class ImageController {
    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * 上传图片文件
     *
     * @param img
     * @param id  员工id (添加员工的时候id为null)
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String imageUpload(MultipartFile img, @RequestParam(value = "id",required = false) Long id) {
        JSONObject json = new JSONObject();
        ResultInfo resultInfo = null;

        try {
            // 上传员工头像
            resultInfo = employeeService.imageUpload(img, id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("上传员工头像异常", e.getMessage());
            json.put("code", -1);
            json.put("msg", "上传头像失败");
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }
}
