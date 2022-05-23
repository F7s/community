package com.example.community.mapper;

import com.example.community.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-04-01-21:33
 */
@Mapper
public interface MessageMapper {

    List<Message> selectConversations(Integer userId, Integer offset, Integer limit);

    Integer selectConversationCount(Integer userId);

    List<Message> selectLetters(String conversationId, Integer offset, Integer limit);

    Integer selectLetterCount(String conversationId);

    //查询未读私信数量
    Integer selectLetterUnreadCount(Integer userId, @Param("conversationId") String conversationId);

    Integer insertMessage(Message message);

    //修改消息的状态
    Integer updateStatus(List<Integer> ids, Integer status);

    //查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    //查询某个主题包含的通知数量
    int selectNoticeCount(int userId, String topic);

    //查询未读的通知的数量
    int selectNoticeUnreadCount(int userId, String topic);

    //查询某个主题所包含的通知
    List<Message> selectNotices(int userId, @Param("topic") String topic, int offset, int limit);



}
