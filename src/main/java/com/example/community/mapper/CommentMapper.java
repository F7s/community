package com.example.community.mapper;

import com.example.community.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-31-11:28
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit);

    Integer selectCountByEntity(Integer entityType, Integer entityId);

    Integer insertComment(Comment comment);

    Comment selectCommentById(int id);

}
