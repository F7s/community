package com.example.community.service.impl;

import com.example.community.event.EventProducer;
import com.example.community.mapper.CommentMapper;
import com.example.community.pojo.Comment;
import com.example.community.service.ICommentService;
import com.example.community.service.IDiscussPostService;
import com.example.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-31-12:01
 */
@Service
public class CommentServiceImpl implements ICommentService {

    //评论
    private Integer ENTITY_TYPE_COMMENT = 2;

    //帖子
    private Integer ENTITY_TYPE_POST = 1;

    @Autowired
    private IDiscussPostService discussPostService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;


    @Override
    public List<Comment> findCommentByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        List<Comment> comments = commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
        return comments;
    }

    @Override
    public Integer findCommentCount(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Integer addComment(Comment comment) {
         if (comment == null) {
             throw new IllegalArgumentException("参数不能为空");
         }

         //添加评论
         comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
         comment.setContent(sensitiveFilter.filter(comment.getContent()));
         Integer rows = commentMapper.insertComment(comment);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            Integer count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }

    @Override
    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }
}
