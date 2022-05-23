package com.example.community.controller;

import com.example.community.event.EventProducer;
import com.example.community.pojo.Comment;
import com.example.community.pojo.DiscussPost;
import com.example.community.pojo.Event;
import com.example.community.service.ICommentService;
import com.example.community.service.IDiscussPostService;
import com.example.community.util.CommunityConstant;
import com.example.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author lizhenghao
 * @create 2022-04-01-18:18
 */
@Controller
@RequestMapping("comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private ICommentService commetService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private IDiscussPostService discussPostService;

    @PostMapping("add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId")Integer discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commetService.addComment(comment);

        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commetService.findCommentById(comment.getId());
            event.setEntityUserId(target.getUserId());
        }

        eventProducer.fireEvent(event);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);

            eventProducer.fireEvent(event);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
