package com.example.community.service;

/**
 * @author lizhenghao
 * @create 2022-04-06-9:16
 */
public interface ILikeService {
    void like (int userId, int entityType, int entityId, int entityUserId);

    long findEntityLikeCount(int entityType, int entityId);

    int findEntityLikeStatus(int userId, int entityType, int entityId);

    int findUserLikeCount(int userId);
}
