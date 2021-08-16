package org.demo.监听本地文件夹变动;

import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.springframework.stereotype.Component;

/**
 * Server端<br><br>
 * 功能说明：服务端监听开启Servlet
 *
 * @author T480S
 * @version 1.3
 * @CreateDate 2017年08月18日
 * @Override 2017年11月07日
 * @Override 2017年11月14日
 */
@Component
public class SocketServerListener extends HttpServlet {

    private static final long serialVersionUID = -999999999999999999L;
    //  开启配置
    public final static String IS_START_SERVER = "instart";

    public static String socket;

//    @Value("${socket.listen}")
//    public void setSocket(String socket){
//        this.socket = socket ;
//    }

    //  初始化启动Socket服务
    @Override
    public void init() throws ServletException {
        super.init();
        for (int i = 0; i < 3; i++) {
            if ("instart".equals(IS_START_SERVER)) {
                open();
                break;
            }
        }
    }

    public void open() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressWarnings("resource")
            @Override
            public void run() {
                try {
                    FileUpLoadServer fileUpLoadServer = new FileUpLoadServer(6005,socket);
                    fileUpLoadServer.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }
}