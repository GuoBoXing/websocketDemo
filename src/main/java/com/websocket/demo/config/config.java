package com.websocket.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpoint;

/**
 * @ClassName config
 * @Description  开启websocket支持
 * @Author G-B-X
 * @Date 2019/7/13 19:37
 * @Version 1.0
 **/
@Configuration
public class config {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
