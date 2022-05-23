package com.example.community.mapper;

import com.example.community.pojo.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-22-14:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DiscussPostMapperTest {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void selectDiscussPostsTest () {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(103,5,1);
        System.out.println(list);
    }
    @Test
    public void selectDiscussPostsCount () {
        Integer rows = discussPostMapper.selectDiscussPostRows(103);
        System.out.println(rows);
    }

    @Test
    public void insertDiscussPost() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent("fadsjfjlasd");
        discussPost.setStatus(2);
        discussPost.setType(2);
        discussPost.setTitle("JKJLJLK");
        discussPost.setUserId(123);
        discussPost.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(discussPost);
    }


}
