package org.jevonD.wastewaterMS.common.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有路径应用规则
                .allowedOrigins("http://localhost:3006")  // 允许这个来源的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许所有头部信息
                .allowCredentials(true)  // 允许发送Cookie
                .maxAge(3600);  // 预检请求的缓存时间
    }
}
