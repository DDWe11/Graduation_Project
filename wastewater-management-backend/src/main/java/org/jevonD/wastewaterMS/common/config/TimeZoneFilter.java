package org.jevonD.wastewaterMS.common.config;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.util.TimeZone;

@Component
public class TimeZoneFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
//        System.out.println("【TimeZoneFilter已执行】设置为：" + TimeZone.getDefault().getID());
        chain.doFilter(request, response);
    }
}