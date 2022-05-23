package com.example.community.mapper;

import com.example.community.pojo.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-03-27-11:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginTicketMapperTest {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void insetTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setTicket("abcd");
        loginTicket.setUserId(3748);
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date());
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void selectByTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abcd");
        System.out.println(loginTicket);
    }

    @Test
    public void updateStatus() {
        Integer rows = loginTicketMapper.updateStatus("abcd", 0);
        System.out.println(rows);
    }
}
