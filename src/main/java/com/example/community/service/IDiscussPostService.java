package com.example.community.service;

import com.example.community.pojo.DiscussPost;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:11
 */
public interface IDiscussPostService {
    List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit);

    Integer findDiscussPostRows(Integer userId);

    Integer addDiscussPost(DiscussPost post);

    DiscussPost findDiscussPostById(Integer id);

    Integer updateCommentCount(Integer id, Integer commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);
}
