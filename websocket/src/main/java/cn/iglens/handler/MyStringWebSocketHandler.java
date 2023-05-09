package cn.iglens.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @link https://mrbird.cc/Spring-Boot%E6%95%B4%E5%90%88WebSocket.html
 * @author xming
 * 为我们的目的是实现和客户端的通信，并且内容为文本内容，所以我们继承的是TextWebSocketHandler；
 * 如果传输的是二进制内容，则可以继承BinaryWebSocketHandler
 */
@Component
@Slf4j
public class MyStringWebSocketHandler extends TextWebSocketHandler {

    @Override
    // 和客户端链接成功的时候触发该方法；
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("和客户端建立连接");
    }

    @Override
    // 和客户端连接失败的时候触发该方法；
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
        log.error("连接异常", exception);
    }

    @Override
    // 和客户端断开连接的时候触发该方法；
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info("和客户端断开连接");
    }

    @Override
    // 和客户端建立连接后，处理客户端发送的请求。
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获取到客户端发送过来的消息
        String receiveMessage = message.getPayload();
        System.out.println("=====接受到的数据"+receiveMessage);
        log.info(receiveMessage);

        long start = System.currentTimeMillis();
        String imageFileName =
            File.separator + "example" + File.separator + "AIS_2019_01_01.csv";
        String decode = URLDecoder.decode(imageFileName, "UTF-8");
        ClassPathResource resource = new ClassPathResource(decode);
        // 因为 ClassPathResource.getFile 只能读取文件，jar包中已经不是文件，不适用所以只能使用 getInputStream 来获取文件流
        InputStream inputStream = resource.getInputStream();
        InputStreamReader inreader = new InputStreamReader(inputStream);
        BufferedReader bfreader = new BufferedReader(inreader,510241024);//设置缓存大小:5M
        String m;
        long end = System.currentTimeMillis();
        System.out.println("读取耗时："+(end-start)/1000);

        long sendStart = System.currentTimeMillis();
        while((m=bfreader.readLine())!=null){
            // 发送消息给客户端
            session.sendMessage(new TextMessage(m));
            Thread.sleep(1000);
        }
        long sendend = System.currentTimeMillis();
        System.out.println("发送耗时："+(end-start)/1000);



        // 关闭连接
        // session.close(CloseStatus.NORMAL);
    }

    private static String fakeAi(String input) {
        if (input == null || "".equals(input)) {
            return "你说什么？没听清︎";
        }
        return input.replace('你', '我')
                .replace("吗", "")
                .replace('?', '!')
                .replace('？', '！');
    }
}