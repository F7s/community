package com.example.community.controller;

import com.example.community.annotation.LoginRequired;
import com.example.community.pojo.User;
import com.example.community.service.IFollowService;
import com.example.community.service.ILikeService;
import com.example.community.service.IUserService;
import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import com.example.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author lizhenghao
 * @create 2022-03-28-8:33
 */
@Controller
@RequestMapping("user")
public class UserController implements CommunityConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private IUserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private ILikeService likeService;

    @Autowired
    private IFollowService followService;

    @LoginRequired
    @GetMapping("setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确");
            return "/site/setting";
        }
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            LOGGER.error("文件上传失败" + e.getMessage());
            throw new RuntimeException("文件上传失败，服务器发生异常");
        }

        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        Integer result = userService.updateHeader(user.getId(), headerUrl);
        if (result == 1) {
            model.addAttribute("successMsg", "上传头像成功");
        } else {
            model.addAttribute("error", "上床头像失败，数据库错误");
        }


        return "/site/setting";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try (
                ServletOutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
        ){
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            LOGGER.error("读取头像失败" + e.getMessage());
        }
    }

    @PostMapping("updatePassword")
    public String updatePassword(String oldPassword, String newPassword, String newPasswordConfirm, Model model) {
        if (oldPassword == null) {
            model.addAttribute("oldPasswordError", "密码不能为空");
            return "/site/setting";
        }

        if (newPassword == null) {
            model.addAttribute("newPasswordError", "密码不能为空");
            return "/site/setting";
        }

        if (newPasswordConfirm == null) {
            model.addAttribute("confirmError", "确认密码不能为空");
            return "/site/setting";
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("confirmError", "两次输入密码不一致");
            return "/site/setting";
        }

        User user = hostHolder.getUser();
        String salt = user.getSalt();
        if (!CommunityUtil.md5(oldPassword + salt).equals(user.getPassword())) {
            model.addAttribute("oldPasswordError", "原密码输入错误");
            return "/site/setting";
        }

        newPassword = CommunityUtil.md5(newPassword + salt);

        Integer result = userService.updatePassword(user.getId(), newPassword);

        model.addAttribute("successMsg", "密码修改成功");

        return "/site/setting";

    }

    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }

        model.addAttribute("user", user);

        //点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);

        //是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }

        model.addAttribute("hasFollowed", hasFollowed);


        return "/site/profile";

    }
}
