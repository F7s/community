package com.example.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-03-22-12:20
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private Integer type;
    private Integer status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
