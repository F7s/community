package com.example.community.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lizhenghao
 * @create 2022-04-15-16:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DiscussPostServiceTest {

    @Autowired
    private IDiscussPostService discussPostService;

    @Test
    public void findDiscussPostRows() {
        Integer rows = discussPostService.findDiscussPostRows(0);
        System.out.println(rows);
    }
}
