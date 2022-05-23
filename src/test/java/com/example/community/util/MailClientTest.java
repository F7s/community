package com.example.community.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author lizhenghao
 * @create 2022-03-23-16:09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MailClientTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("984505622@qq.com", "TEST", "Welcome.");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "sunday");

        String content = templateEngine.process("/mail/demo", context);

        System.out.println(content);

        mailClient.sendMail("984505622@qq.com","HTML",content);

    }
}
