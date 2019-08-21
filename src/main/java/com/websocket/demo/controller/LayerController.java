package com.websocket.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName LayerController
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/15 14:19
 * @Version 1.0
 **/
@Controller
@RequestMapping("layer")
public class LayerController {
    // 页面请求
    @GetMapping("layer/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView modelAndView = new ModelAndView("/socketLayer");
        modelAndView.addObject("uname","张三");
        return modelAndView;
    }
}
