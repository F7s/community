package com.example.community.service;

import com.example.community.pojo.Message;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-04-02-10:27
 */
public interface IMessageService {

    List<Message> findConversations(Integer userId, Integer offset, Integer limit);

    Integer findConversationCount(Integer userId);

    List<Message> findLetters(String conversationId, Integer offset, Integer limit);

    Integer findLetterCount(String conversationId);

    Integer findLetterUnreadCount(Integer userId, String conversationId);

    Integer addMessage(Message message);

    Integer readMessage(List<Integer> ids);

    Message findLatestNotice(int userId, String topic);

    int findNoticeCount(int userId, String topic);

    int findNoticeUnreadCount(int userId, String topic);

    List<Message> findNotices(int userId, String topic, int offset, int limit);

}
