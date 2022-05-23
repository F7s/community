package com.example.community.mapper;

import com.example.community.pojo.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhenghao
 * @create 2022-04-02-10:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageMapperTest {
    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void selectConversations(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        System.out.println(messages);
    }

    @Test
    public void selectConversationCount() {
        Integer rows = messageMapper.selectConversationCount(111);
        System.out.println(rows);
    }

    @Test
    public void selectLetters() {
        List<Message> messages = messageMapper.selectLetters("111_112", 0, 5);
        System.out.println(messages);
    }

    @Test
    public void selectLetterCount() {
        Integer rows = messageMapper.selectLetterCount("111_112");
        System.out.println(rows);
    }

    @Test
    public void selectLetterUnreadCount(){
        Integer rows = messageMapper.selectLetterUnreadCount(111, null);
        System.out.println(rows);
    }

    @Test
    public void asdf(){
        List<Integer> ids= new ArrayList<>();
        ids.add(355);
        ids.add(356);
        ids.add(357);


        Integer integer = messageMapper.updateStatus(ids, 1);
        System.out.println(integer);
    }
}
