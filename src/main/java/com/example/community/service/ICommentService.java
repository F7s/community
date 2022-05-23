package com.example.community.service;

import com.example.community.pojo.Comment;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-31-11:58
 */
public interface ICommentService {
    List<Comment> findCommentByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit);

    Integer findCommentCount(Integer entityType, Integer entityId);

    Integer addComment(Comment comment);

    Comment findCommentById(int id);
}
