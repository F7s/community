package com.example.community.config;


import com.example.community.controller.interceptor.LoginRequiredInterception;
import com.example.community.controller.interceptor.LoginTicketInterceptor;
import com.example.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lizhenghao
 * @create 2022-03-27-18:38
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterception loginRequiredInterception;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/image/**","/**/*.js");

//        registry.addInterceptor(loginRequiredInterception)
//                .excludePathPatterns("/**/*.css","/image/**","/**/*.js");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css","/image/**","/**/*.js");
    }
}
