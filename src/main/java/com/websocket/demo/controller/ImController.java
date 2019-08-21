package com.websocket.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.websocket.demo.util.ApiReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import sun.plugin2.message.Message;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ImController
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/14 15:22
 * @Version 1.0
 **/
@ServerEndpoint("/im/{userId}")
@Component
public class ImController {
    // 静态变量，用来j记录当前连接数，应该把它设计成线程安全的
    private static int onlineCount = 0;
    // 与某个客户端的连接会话，需要通过他来给客户端发送数据
    private Session session;
    //新：使用map对象， 根据useid来获取对应的websocket
    private static ConcurrentHashMap<String,ImController> websocketList = new ConcurrentHashMap<>();
    // 接收sid
    private String userId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        websocketList.put(userId,this);
        System.out.println("websocketList ->" + JSON.toJSONString(websocketList));
        addOnlineCount(); //在线人数加1
        System.out.println("有新窗口开始监听："+userId + "，当前在线人数为"+getOnlineCount());
        this.userId = userId;
        try {
            sendMessage(JSON.toJSONString(ApiReturnUtil.success("连接成功")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (websocketList.get(this.userId) != null) {
            websocketList.remove(this.userId);
            subOnlineCount(); //在线人数减1
            System.out.println("有一连接关闭！当前在线人数为："+getOnlineCount());
        }
    }

    /**
     * 收到客户端消息后掉用的方法
     * @param message 客户端发送来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message,Session session) {
        System.out.println("收到来自窗口："+userId+"的消息"+message);
        if (StringUtils.isNotBlank(message)) {
            JSONArray list = JSONArray.parseArray(message);
            for (int i = 0; i < list.size(); i++) {
                try {
                    // 解析发送的报文
                    JSONObject jsonObject = list.getJSONObject(i);
                    String toUserId = jsonObject.getString("toUserId");
                    String contextText = jsonObject.getString("contentText");
                    jsonObject.put("formUserId",this.userId);
                    // 传送给对应用户的websocket
                    if (StringUtils.isNotBlank(toUserId) && StringUtils.isNotBlank(contextText)){
                        ImController socketx = websocketList.get(toUserId);
                        // 需要进行转换
                        if (socketx != null) {
                            socketx.sendMessage(JSON.toJSONString(ApiReturnUtil.success(jsonObject)));
                            // 此处可以放相关业务代码,例如存储到数据库
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发生错误
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session,Throwable throwable) {
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    /**
     * 实现服务主动推送
     * @param message
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义的消息
     */
    public static void sendInfo(String message,@PathParam("userId") String userId) {
        System.out.println("推送消息到窗口："+ userId + "，推送内容"+message);

        for (ImController item: websocketList.values()) {
            try {
                // 这里可以设定只推送给这个sid 的，为null则全部推送
                if (userId == null) {
                    item.sendMessage(message);
                } else if(item.userId.equals(userId)) {
                    item.sendMessage(message);
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void subOnlineCount() {
        ImController.onlineCount--;
    }
    private int getOnlineCount() {
        return onlineCount;
    }
    private void addOnlineCount() {
        ImController.onlineCount++;
    }
}
