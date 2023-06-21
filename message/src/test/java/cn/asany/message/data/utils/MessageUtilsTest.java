package cn.asany.message.data.utils;

import static org.junit.jupiter.api.Assertions.*;

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
}
