package com.example.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-04-01-21:31
 */
@Data
public class Message {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String conversationId;
    private String content;
    private Integer status;
    private Date createTime;
}
