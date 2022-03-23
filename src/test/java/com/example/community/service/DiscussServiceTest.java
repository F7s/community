package com.example.community.service;

import com.example.community.pojo.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.metadata.IIOInvalidTreeException;
import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DiscussServiceTest {
    @Autowired
    private IDiscussPostService discussPostService;

    @Test
    public void findDiscussPosts(){
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, 0, 10);
        System.out.println(list);
    }
}
