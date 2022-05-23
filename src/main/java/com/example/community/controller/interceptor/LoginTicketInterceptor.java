package com.example.community.controller.interceptor;

import com.example.community.pojo.LoginTicket;
import com.example.community.pojo.User;
import com.example.community.service.IUserService;
import com.example.community.util.CookieUtil;
import com.example.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-03-27-18:54
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Autowired
    private HostHolder hostHolder;

    //private User user;多线程情况下容易产生冲突

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            LoginTicket result = userService.findLoginTicket(ticket);
            if (result != null && result.getStatus() == 0 && result.getExpired().after(new Date())) {
                User user = userService.findUserById(result.getUserId());
                hostHolder.setUser(user);
                //构建用户认证结果，并存入SecurityContext，以便于Security进行授权
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
