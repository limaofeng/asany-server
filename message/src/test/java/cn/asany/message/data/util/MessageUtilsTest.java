package cn.asany.message.data.util;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.message.define.domain.toys.MessageContent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MessageUtilsTest {

  @Test
  void parseMultipartString() {
    String multipartString =
        "--MessageContentBoundary--\r\n"
            + "Content-Disposition: form-data; name=\"key1\"\r\n"
            + "\r\n"
            + "value1\r\n"
            + "--MessageContentBoundary--\r\n"
            + "Content-Disposition: form-data; name=\"key2\"\r\n"
            + "\r\n"
            + "value2\r\n"
            + "--MessageContentBoundary--\r\n"
            + "Content-Disposition: form-data; name=\"file\"; filename=\"example.txt\"\r\n"
            + "Content-Type: text/plain\r\n"
            + "\r\n"
            + "This is the content of the file.\r\n"
            + "--MessageContentBoundary--";

    String boundary = MessageUtils.extractBoundary(multipartString);
    Map<String, Object> data = MessageUtils.parseMultipartString(multipartString, boundary);
    assertEquals("value1", data.get("key1"));

    multipartString = MessageUtils.convertToMultipartString(data, boundary);
    log.info("multipartString: {}", multipartString);
  }

  @Test
  void convertToMultipartString() {
    Map<String, Object> data = new HashMap<>();
    data.put(MessageContent.TITLE, "hello, {{user.name}}");
    data.put(MessageContent.CONTENT, "you have a new task: {{task.name}}");
    String multipartString =
        MessageUtils.convertToMultipartString(data, MessageContent.DATA_BOUNDARY);
    log.info("multipartString: {}", multipartString);

    data = MessageUtils.parseMultipartString(multipartString, MessageContent.DATA_BOUNDARY);

    assertEquals("hello, {{user.name}}", data.get(MessageContent.TITLE));
  }
}
