package com.websocket.demo.controller;

import com.websocket.demo.util.ApiReturnUtil;
import com.websocket.demo.websockerserver.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @ClassName CheckCenterController
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/13 21:15
 * @Version 1.0
 **/
@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {
    // 页面请求
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView modelAndView = new ModelAndView("/socket");
        modelAndView.addObject("cid",cid);
        return modelAndView;
    }

    // 页面请求
    @GetMapping("/socket1")
    public ModelAndView socket1() {
        ModelAndView modelAndView = new ModelAndView("/socket1");
        return modelAndView;
    }

    // 推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public ApiReturnUtil pushToWeb(@PathVariable String cid, String message) {
        try {
            WebSocketServer.senInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiReturnUtil.error(cid + "##" + e.getMessage());
        }
        return ApiReturnUtil.success(cid);
    }
}
