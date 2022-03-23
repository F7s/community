package com.example.community.service;

import com.example.community.pojo.User;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:27
 */
public interface IUserService {
    User findUserById(Integer id);
}
