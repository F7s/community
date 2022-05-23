package com.example.community.service;

import com.example.community.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    public void getById(){
        User user = userService.findUserById(103);
        System.out.println(user);
    }

    @Test
    public void register(){
        User user = new User();
        user.setUsername("niuniu");
        user.setPassword("niuke123");
        user.setEmail("9845056@qq.com");
        userService.register(user);
    }

    @Test
    public void login(){
        Map<String, Object> login = userService.login("niuniu", "niuke123", 1000);
        System.out.println(login);
    }

    @Test
    public void fuck() {
        String filename = "jfklasfj.jpg";
        String suffix = filename.substring(filename.lastIndexOf("."));
        System.out.println(suffix);
    }

}
