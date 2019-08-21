package com.websocket.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName RefreshController
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/16 11:32
 * @Version 1.0
 **/
@Controller
public class RefreshController {

    @GetMapping("refresh")
    public String refresh() {
        return "refresh";
    }
}
