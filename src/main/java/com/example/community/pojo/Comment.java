package com.example.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-03-31-11:28
 */
@Data
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer targetId;
    private String content;
    private Integer status;
    private Date createTime;
}
