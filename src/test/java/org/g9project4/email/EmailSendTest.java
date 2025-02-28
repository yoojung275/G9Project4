package org.g9project4.email;
//
//import org.g9project4.email.controllers.EmailMessage;
//import org.g9project4.email.service.EmailSendService;
//import org.g9project4.email.service.EmailVerifyService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//public class EmailSendTest {
//
//    @Autowired
//    private EmailSendService emailSendService;
//
//    @Autowired
//    private EmailVerifyService emailVerifyService;
//
//    @Test
//    void sendTest() {
//        EmailMessage message = new EmailMessage("intokys@naver.com", "제목...", "내용...");
//        boolean success = emailSendService.sendMail(message);
//
//        assertTrue(success);
//    }
//
//    @Test
//    @DisplayName("템플릿 형태로 전송 테스트")
//    void sendWithTplTest() {
//        EmailMessage message = new EmailMessage("intokys@gmail.com", "제목...", "내용...");
//        Map<String,Object> tplData = new HashMap<>();
//        tplData.put("authNum", "123456");
//        boolean success = emailSendService.sendMail(message, "auth", tplData);
//
//        assertTrue(success);
//    }
//
//    @Test
//    @DisplayName("이메일 인증 번호 전송 테스트")
//    void emailVerifyTest() {
//        boolean result = emailVerifyService.sendCode("intokys@naver.com");
//        assertTrue(result);
//    }
//}

