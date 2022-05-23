package com.example.community.controller;

import com.example.community.mapper.UserMapper;
import com.example.community.pojo.User;
import com.example.community.service.IUserService;
import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import com.example.community.util.RedisKeyUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.awt.SystemColor.text;

/**
 * @author lizhenghao
 * @create 2022-03-23-17:04
 */
@Controller
public class LoginController implements CommunityConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Value("${server.servlet.context-path}")
    String contextPath;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> result = userService.register(user);
        if (result == null || result.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向你的邮箱发送了一封激活邮件，请尽快激活");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", result.get("usernameMsg"));
            model.addAttribute("passwordMsg", result.get("passwordMsg"));
            model.addAttribute("emailMsg", result.get("emailMsg"));
            return "/site/register";
        }
    }

    @GetMapping("/activation/{id}/{activationCode}")
    public String activeUser(Model model,@PathVariable("id") Integer id, @PathVariable("activationCode") String activationCode) {
        Map<String, Object> result = userService.activeUser(id, activationCode);
        model.addAttribute("msg", result.get("activeMsg"));
        model.addAttribute("target", "/index");
        return "/site/operate-result";
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response/*, HttpSession session*/){
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

//        session.setAttribute("text", text);
        //验证码的归属
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptcha", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        //将验证码存入Redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            LOGGER.error("响应验证码失败" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(@CookieValue("kaptcha")String kaptchaOwner, String username, String password, String code, boolean rememberme, Model model/*, HttpSession session*/, HttpServletResponse response) {

        String text = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            text = (String) redisTemplate.opsForValue().get(redisKey);
        }

        code = code.toUpperCase();
        if (StringUtils.isBlank(text) || StringUtils.isBlank(code) || !text.equals(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/site/login";
        }

        Integer expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED;

        Map<String, Object> result = userService.login(username, password, expiredSeconds);
        if (result.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", result.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg", result.get("usernameMsg"));
            model.addAttribute("passwordMsg",  result.get("passwordMsg"));
            return "/site/login";
        }
    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }


}
