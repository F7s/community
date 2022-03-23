package com.example.community.mapper;

import com.example.community.pojo.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-03-22-13:40
 */
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts (Integer userId,Integer offset,Integer limit);

    Integer selectDiscussPostRows (Integer userId);
}
