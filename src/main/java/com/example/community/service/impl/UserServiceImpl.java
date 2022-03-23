package com.example.community.service.impl;

import com.example.community.mapper.UserMapper;
import com.example.community.pojo.User;
import com.example.community.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:28
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }
}
