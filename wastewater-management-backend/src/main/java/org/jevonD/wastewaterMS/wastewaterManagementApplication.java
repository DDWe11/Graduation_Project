package org.jevonD.wastewaterMS;

import org.jevonD.wastewaterMS.common.utils.LoadingAnimationUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@MapperScan("org.jevonD.wastewaterMS.modules.*.repository")
@SpringBootApplication
@EnableWebSocket
public class wastewaterManagementApplication {
    static {
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println("已设置JVM默认时区为: " + java.util.TimeZone.getDefault().getID());
    }
    public static void main(String[] args) {
        SpringApplication.run(wastewaterManagementApplication.class, args);
//        startLoadingAnimation();
    }
    // 启动动画的方法
    private static void startLoadingAnimation() {
        new Thread(() -> {
            try {
                LoadingAnimationUtils.showLoadingAnimation(true); // 启动动画
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}