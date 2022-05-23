package com.example.community.service;

import com.example.community.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author lizhenghao
 * @create 2022-04-07-8:08
 */
public interface IFollowService {
    void follow(int userId, int entityType, int entityId);

    void unfollow(int userId, int entityType, int entityId);

    //查询关注的实体的数量
    long findFolloweeCount(int userId, int entityType);

    //查询关注的粉丝的数量
    long findFollowerCount(int entityType, int entityId);

    //查询当前用户是否已关注该实体
    boolean hasFollowed(int userId, int entityType, int entityId);

    List<Map<String,Object>> findFollowees(int userId, int offset, int limit);

    List<Map<String,Object>> findFollowers(int userId, int offset, int limit);


}
