package com.example.community.mapper;

import com.example.community.pojo.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lizhenghao
 * @create 2022-03-27-11:39
 */
@Deprecated
@Mapper
public interface LoginTicketMapper {

    Integer insertLoginTicket(LoginTicket loginTicket);

    LoginTicket selectByTicket(String ticket);

    Integer updateStatus(String ticket, Integer status);

}
