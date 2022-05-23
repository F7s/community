package com.example.community.service;

import com.example.community.pojo.DiscussPost;
import org.springframework.data.domain.Page;

/**
 * @author lizhenghao
 * @create 2022-04-15-14:59
 */
public interface IElasticSearchService {
    void saveDiscussPost(DiscussPost post);

    void deleteDiscussPost(int id);

    Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit);
}
