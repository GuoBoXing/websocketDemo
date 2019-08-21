package com.websocket.demo.websockerserver;

import com.alibaba.fastjson.JSONObject;
import com.websocket.demo.Thread.ThreadDemo;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName WebSocketServer
 * @Description webSocket服务端
 * @Author G-B-X
 * @Date 2019/7/13 19:40
 * @Version 1.0
 **/
@ServerEndpoint("/websoket/{sid}")
@Component
public class WebSocketServer {
    private static Map map = new HashMap<>();
    // 静态变量，用来记录当前连接数的，应该把它设计成线程安全的
    private static int onLineCount = 0;
    // concurrent包的线程安全set，用来存放每个客户端对应的websocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServers = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话，需要通过他来给客户端发送数据
    private Session session;
    // 接收sid
    private String sId;


    /**
     * 连接成功调用的方法
     * @param session
     * @param sid
     */
    @OnOpen
    public  void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketServers.add(this); // 加入set中
        addOnlineCount();   // 在线人数加1
        System.out.println("有新窗口开始监听："+ sid + ",当前在线人数为" + getOnlineCount());
        this.sId = sid;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success",200);
            jsonObject.put("data","链接成功");
            jsonObject.put("message","HeartBeat");
            sendMessage(jsonObject.toString());
            ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5));
                ThreadDemo myTask = new ThreadDemo();
                ThreadDemo myTask1 = new ThreadDemo();
                ThreadDemo myTask2 = new ThreadDemo();
                if (!map.containsKey("sc")) {
                    executor.execute(myTask);
                    map.put("sc","20%");
                    sendMessage(map.get("sc").toString());
                    executor.execute(myTask1);
                    map.put("sc","50%");
                    Thread.sleep(8000);
                    sendMessage(map.get("sc").toString());
                    executor.execute(myTask2);
                    map.put("sc","100%");
                    Thread.sleep(3000);
                    sendMessage(map.get("sc").toString());
                    executor.shutdown();
                } else {
                   if(map.get("sc").equals("20%")) {
                       executor.execute(myTask);
                       map.put("sc","20%");
                       sendMessage(map.get("sc").toString());
                   } else if(map.get("sc").equals("50%")) {
                       executor.execute(myTask1);
                       map.put("sc","50%");
                       Thread.sleep(8000);
                       sendMessage(map.get("sc").toString());
                   } else if(map.get("sc").equals("100%")) {
                       executor.execute(myTask2);
                       map.put("sc","100%");
                       Thread.sleep(3000);
                       sendMessage(map.get("sc").toString());
                   }
                    executor.shutdown();
                }

                System.out.println("线程池中线程数目："+executor.getPoolSize() +",队列中等待执行的任务数目："+executor.getQueue().size()+",已执行完别的项目数目："+executor.getCompletedTaskCount());
                onClose();
        } catch (IOException e) {
            System.out.println("webscoket IO异常");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketServers.remove(this);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为："+ getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message,Session session) {
        System.out.println("收到来自窗口："+ sId + "的消息："+message);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","HeartBeat");
        try {
            sendMessage(jsonObject.toString());
        } catch (IOException e) {
            System.out.println("心跳出现问题");
        }
        // 群发消息
        for (WebSocketServer item:webSocketServers) {
            try {
                System.out.println("message ================= "+message);
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session,Throwable error) {
        System.out.println("发生错误" + error.getMessage());
    }

    /**
     * 实现服务器主动推送
     * @param message
     */
    private void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义的消息
     */
    public static void senInfo(String message,@PathParam("sid")String sid) throws IOException {
        for (WebSocketServer item:webSocketServers) {
            try {
                // 这里可以设定只推送给这个sid的。为null全部推送
                if (sid ==  null) {
                    item.sendMessage(message);
                } else {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }



    private static synchronized void addOnlineCount() {
        WebSocketServer.onLineCount++;
    }

    private static synchronized int getOnlineCount() {
        return onLineCount;
    }

    private static synchronized void subOnlineCount() {
        if (WebSocketServer.onLineCount == 0){
            return;
        } else if(WebSocketServer.onLineCount >= 0){
            WebSocketServer.onLineCount = 0;
        } else {
            WebSocketServer.onLineCount--;
        }
    }
}
