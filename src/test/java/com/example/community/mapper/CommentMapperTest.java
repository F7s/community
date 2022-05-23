package com.example.community.mapper;

import com.example.community.pojo.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-31-11:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void selectCommentByEntity() {
        List<Comment> comments = commentMapper.selectCommentsByEntity(1, 274, 0, 4);
        System.out.println(comments);
    }

    @Test
    public void selectCountByEntity() {
        Integer rows = commentMapper.selectCountByEntity(1, 274);
        System.out.println(rows);
    }
}
