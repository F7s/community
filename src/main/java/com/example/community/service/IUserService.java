package com.example.community.service;

import com.example.community.pojo.DiscussPost;
import com.example.community.pojo.LoginTicket;
import com.example.community.pojo.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:27
 */
public interface IUserService {
    User findUserById(Integer id);

    Map<String,Object> register(User user);

    Map<String, Object> login(String username, String password, Integer expiredSeconds);

    Map<String, Object> activeUser(Integer id, String activationCode);

    void logout(String ticket);

    LoginTicket findLoginTicket(String ticket);

    Integer updateHeader(Integer id, String headUrl);

    Integer updatePassword(Integer id, String newPassword);

    User findUserByName(String username);

    Collection<? extends GrantedAuthority> getAuthorities(int userId);

}
