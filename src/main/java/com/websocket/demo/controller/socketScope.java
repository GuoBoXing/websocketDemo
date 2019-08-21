package com.websocket.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName socketScope
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/17 17:25
 * @Version 1.0
 **/
@Controller
public class socketScope {
    @GetMapping("scope")
    public String scope() {
        return "socketScope";
    }
}
