package com.example.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-03-22-12:15
 */
@Data
public class DiscussPost {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Integer commentCount;
    private Double score;
}
