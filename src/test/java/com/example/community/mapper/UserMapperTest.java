package com.example.community.mapper;

import com.example.community.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lizhenghao
 * @create 2022-03-22-12:41
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetUserById(){
        User user = userMapper.selectById(120);

        System.out.println(user);
    }
    @Test
    public void testSelectByName(){
        User user = userMapper.selectByName("niuke");

        System.out.println(user);
    }
    @Test
    public void testSelectByEmail(){
        User user = userMapper.selectByEmail("nowcoder138@sina.com");

        System.out.println(user);
    }
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("aeded");
        user.setEmail("2133@qq.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/133t.png");
        user.setActivationCode(null);
        user.setPassword("231312abc");
        user.setSalt("fajslkassda");
        Integer rows = userMapper.insertUser(user);
        System.out.println(rows);
    }
}
