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
}
