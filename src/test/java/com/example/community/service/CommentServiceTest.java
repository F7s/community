package com.example.community.service;

import com.example.community.pojo.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.model.IComment;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-04-01-20:03
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @Autowired
    private ICommentService commentService;

    @Test
    public void addComment() {
        Comment comment = new Comment();
        comment.setCreateTime(new Date());
        comment.setUserId(323);
        comment.setStatus(2);
        comment.setEntityType(2);
        comment.setTargetId(231);
        comment.setContent("fasdjfkl;asdjf;aslkd");

        commentService.addComment(comment);
    }


}
