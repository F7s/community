package com.example.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-03-27-11:36
 */
@Data
public class LoginTicket {
    private Integer id;
    private Integer userId;
    private String ticket;
    private Integer status;
    private Date expired;
}
