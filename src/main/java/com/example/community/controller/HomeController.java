package com.example.community.controller;

import com.example.community.pojo.DiscussPost;
import com.example.community.pojo.Page;
import com.example.community.pojo.User;
import com.example.community.service.IDiscussPostService;
import com.example.community.service.ILikeService;
import com.example.community.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizhenghao
 * @create 2022-03-22-16:40
 */
@Controller
public class HomeController {
    @Autowired
    private IDiscussPostService discussPostService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ILikeService likeService;

    @GetMapping({"","/index"})
    public String getIndexPage(Model model, Page page) {
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());

        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if (list != null){
            for (DiscussPost discussPost : list){
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                User user = userService.findUserById(discussPost.getUserId());
                map.put("user",user);

                long likeCount = likeService.findEntityLikeCount(1, discussPost.getId());
                map.put("likeCount",likeCount);

                discussPosts.add(map);
            }
        }

        model.addAttribute("discussPosts", discussPosts);

        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "/error/500";
    }

    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/error/404";
    }


}
