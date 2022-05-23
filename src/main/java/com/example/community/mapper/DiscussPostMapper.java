package com.example.community.mapper;

import com.example.community.pojo.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-22-13:40
 */
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts (@Param("userId") Integer userId,Integer offset,Integer limit);

    Integer selectDiscussPostRows (@Param("userId") Integer userId);

    Integer insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(Integer id);

    Integer updateCommentCount(Integer id, Integer commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);
}
