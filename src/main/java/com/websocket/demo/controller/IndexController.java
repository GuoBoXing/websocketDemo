package com.websocket.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/16 9:26
 * @Version 1.0
 **/
@Controller
@RequestMapping("demo")
public class IndexController {

    @GetMapping("index")
    public ModelAndView gotoIndex() {
        ModelAndView modelAndView = new ModelAndView("/demo/index");
        return modelAndView;
    }

    @GetMapping("a")
    public String gotoIndex1() {
        return "a";
    }

    @GetMapping("b")
    public String gotoIndex2() {
        return "b";
    }

}
