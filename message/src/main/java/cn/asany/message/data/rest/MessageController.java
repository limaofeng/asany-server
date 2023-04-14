package cn.asany.message.data.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息测试
 *
 * @author limaofeng
 */
@Slf4j
@RestController
public class MessageController {

  @PostMapping("/messages")
  public void testSendMessage(
      @RequestParam("token") String token, @RequestBody TestMessageData data) {
    System.out.println(token);
    System.out.println(data);
  }
}
