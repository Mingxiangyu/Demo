package cn.iglens.config;

import cn.iglens.handler.MyStringWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketServerConfig implements WebSocketConfigurer {

    @Autowired
    private MyStringWebSocketHandler myStringWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 将上面创建的MyStringWebSocketHandler注册到了WebSocketHandlerRegistry
        // withSockJS的含义是，通信的客户端是通过SockJS实现的,需要配合请求时使用socketJS使用<p>
        // 否则使用这个：https://blog.csdn.net/Ouyzc/article/details/79994401
        registry.addHandler(myStringWebSocketHandler, "/connect").withSockJS();
    }
}