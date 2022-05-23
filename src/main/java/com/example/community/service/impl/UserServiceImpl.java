package com.example.community.service.impl;

import com.example.community.mapper.LoginTicketMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.pojo.DiscussPost;
import com.example.community.pojo.LoginTicket;
import com.example.community.pojo.User;
import com.example.community.service.IUserService;
import com.example.community.util.CommunityUtil;
import com.example.community.util.MailClient;
import com.example.community.util.RedisKeyUtil;
import com.example.community.util.SensitiveFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:28
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(Integer id) {
        User user = getCache(id);//在缓存里面找User
        if (user == null) {
            user = initCache(id);//如果缓存里面没有,初始化缓存
        }
        return user;
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String,Object> map = new HashMap<>();

        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("email", "邮箱不能为空");
            return map;
        }

        User result = userMapper.selectByName(user.getUsername());
        if (result != null){
            map.put("usernameMsg", "账号已存在");
            return map;
        }

        result = userMapper.selectByEmail(user.getEmail());
        if (result != null) {
            map.put("emailMsg", "邮箱已存在");
            return map;
        }

        String salt = CommunityUtil.generateUUID().substring(0,5);
        user.setSalt(salt);
        String password = user.getPassword();
        String md5Password = CommunityUtil.md5(password + salt);
        user.setPassword(md5Password);

        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());

        String url = domain + contextPath + "/activation/" + user.getId() +"/" + user.getActivationCode();

        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);

        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    @Override
    public Map<String, Object> login(String username, String password, Integer expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        User result = userMapper.selectByName(username);
        if (result==null) {
            map.put("usernameMsg", "用户名不存在");
            return map;
        }

        if (result.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活");
        }

        String salt = result.getSalt();
        if (!CommunityUtil.md5(password + salt).equals(result.getPassword())) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

//        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(result.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + (expiredSeconds * 1000L)));
        //loginTicketMapper.insertLoginTicket(loginTicket);


        String ticketKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(ticketKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    @Override
    public Map<String, Object> activeUser(Integer id, String activationCode) {
        Map<String, Object> map = new HashMap<>();
        if (activationCode == null || id == null){
            map.put("activeMsg", "激活链接有误");
            return map;
        }
        User result = userMapper.selectById(id);
        if (result == null) {
            map.put("activeMsg", "用户不存在");
            return map;
        }
        if (!activationCode.equals(result.getActivationCode())){
            map.put("activeMsg", "激活失败激活码错误");
        }
        Integer row = userMapper.updateStatus(id, 1);
        if (row == 1) {
            map.put("activeMsg", "激活成功");
        }else {
            map.put("active", "激活失败，请联系管理员");
        }
        return map;
    }

    @Override
    public void logout(String ticket) {
//        loginTicketMapper.updateStatus(ticket, 1);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(ticketKey, loginTicket);
    }

    @Override
    public LoginTicket findLoginTicket(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket result = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        return result;

    }

    @Override
    public Integer updateHeader(Integer id, String headUrl) {

        Integer rows = userMapper.updateHeader(id, headUrl);

        cleanCache(id);

        return rows;
    }

    @Override
    public Integer updatePassword(Integer id, String newPassword) {
        Integer rows = userMapper.updatePassword(id, newPassword);
        return rows;
    }

    @Override
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    private User getCache(int userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(userKey);
    }

    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String userKey = RedisKeyUtil.getUserKey(user.getId());
        redisTemplate.opsForValue().set(userKey, user,3600, TimeUnit.SECONDS);
        return user;

    }

    private void cleanCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(int useId) {
        User user = this.findUserById(useId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return "admin";
                    case 2:
                        return "moderator";
                    default:
                        return "user";
                }
            }
        });
        return list;
    }

}
