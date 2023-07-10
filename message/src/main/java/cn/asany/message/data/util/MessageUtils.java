package cn.asany.message.data.util;

import cn.asany.message.data.domain.MessageRecipient;
import cn.asany.message.data.domain.enums.MessageRecipientType;
import cn.asany.message.define.domain.toys.VariableDefinition;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockPart;

/**
 * 消息工具类
 *
 * @author limaofeng
 */
@Slf4j
public class MessageUtils {

  /**
   * 格式化收件人
   *
   * @param phone 手机号
   * @return 格式化后的收件人
   */
  public static String formatRecipientFromPhone(String phone) {
    return MessageRecipientType.PHONE.getName() + MessageRecipientType.DELIMITER + phone;
  }

  public static String formatRecipientFromUser(Long userId) {
    return MessageRecipientType.USER.getName() + MessageRecipientType.DELIMITER + userId;
  }

  /**
   * 格式化收件人
   *
   * @param email 邮箱
   * @return 格式化后的收件人
   */
  public static String formatRecipientFromEmail(String email) {
    return MessageRecipientType.EMAIL.getName() + MessageRecipientType.DELIMITER + email;
  }

  public static String formatRecipient(MessageRecipient recipient) {
    return recipient.getType().getName() + MessageRecipientType.DELIMITER + recipient.getValue();
  }

  public static List<String> parseRecipientPhoneNumber(String str) {
    MessageRecipient recipient = recipient(str);
    return parseRecipientPhoneNumber(recipient.getType(), recipient.getValue());
  }

  public static List<String> parseRecipientPhoneNumber(MessageRecipientType type, String value) {
    List<String> recipients = new ArrayList<>();
    if (type == MessageRecipientType.PHONE) {
      recipients.add(value);
      return recipients;
    } else {
      log.error("收件人类型错误：{}, 无法解析到手机号码", type + MessageRecipientType.DELIMITER + value);
    }
    return recipients;
  }

  public static List<String> parseRecipientEmail(MessageRecipientType type, String value) {
    List<String> recipients = new ArrayList<>();
    if (type == MessageRecipientType.EMAIL) {
      recipients.add(value);
      return recipients;
    } else {
      log.error("收件人类型错误：{}, 无法解析到邮箱", type + MessageRecipientType.DELIMITER + value);
    }
    return recipients;
  }

  public static List<String> parseRecipientUserId(MessageRecipientType type, String value) {
    List<String> recipients = new ArrayList<>();
    if (type == MessageRecipientType.USER) {
      recipients.add(value);
      return recipients;
    } else {
      log.error("收件人类型错误：{}, 无法解析到用户ID", type + MessageRecipientType.DELIMITER + value);
    }
    return recipients;
  }

  public static MessageRecipient recipient(String value) {
    MessageRecipientType recipientType = MessageRecipientType.of(value);
    return recipient(recipientType, value.substring(recipientType.getName().length() + 1));
  }

  public static MessageRecipient recipient(MessageRecipientType type, String value) {
    return MessageRecipient.builder().type(type).value(value).build();
  }

  public static void validate(List<VariableDefinition> variables, Map<String, Object> values) {
    List<String> fields = new ArrayList<>();
    variables.forEach(
        def -> {
          if (def.isRequired() && !values.containsKey(def.getName())) {
            fields.add(def.getName());
          }
        });
    if (!fields.isEmpty()) {
      throw new RuntimeException("缺少必要的参数：" + fields);
    }
  }

  public static Map<String, Object> parseMultipartString(String content, String boundary) {
    Map<String, Object> fields = new HashMap<>(5);

    if (StringUtil.isBlank(content)) {
      return fields;
    }

    String[] parts = content.split("--" + boundary);
    for (String part : parts) {
      part = part.trim();
      if (part.isEmpty() || "--".equals(part)) {
        continue;
      }

      if (part.startsWith("Content-Disposition")) {
        String name = extractFieldName(part);
        if (name == null) {
          log.warn("Field name not found in part: {}", part);
          continue;
        }
        if (part.contains("filename")) {
          // 处理文件字段
          String fileName = extractFileName(part);
          byte[] bytes = extractFileContent(part);
          String contentType = extractFileContentType(part);
          MockPart filePart = new MockPart(name, fileName, bytes);
          List<String> headerValue = new ArrayList<>();
          headerValue.add(contentType);
          filePart.getHeaders().put(HttpHeaders.CONTENT_TYPE, headerValue);
          // 处理文件内容...
          log.info("Field: " + name + ", File: " + fileName);
          log.info("File Content: " + new String(bytes));
          fields.put(name, filePart);
        } else {
          // 处理普通字段
          String value = extractFieldValue(part);
          // 处理字段值...
          log.info("Field: " + name + ", Value: " + value);
          fields.put(name, value);
        }
      }
    }
    return fields;
  }

  /**
   * 转换为 multipart 字符串
   *
   * @param fields 字段
   * @param boundary 分隔符
   * @return multipart 字符串
   */
  public static String convertToMultipartString(Map<String, Object> fields, String boundary) {
    StringBuilder multipartBuilder = new StringBuilder();

    for (Map.Entry<String, Object> entry : fields.entrySet()) {
      String fieldName = entry.getKey();
      Object fieldValue = entry.getValue();

      multipartBuilder.append("--").append(boundary).append("\r\n");
      if (fieldValue instanceof Part) {
        // 处理 Part 对象
        Part part = (Part) fieldValue;
        String fileName = part.getSubmittedFileName();
        multipartBuilder
            .append("Content-Disposition: form-data; name=\"")
            .append(fieldName)
            .append("\"; filename=\"")
            .append(fileName)
            .append("\"\r\n");
        multipartBuilder.append("Content-Type: ").append(part.getContentType()).append("\r\n");
        multipartBuilder.append("\r\n");
        try {
          byte[] buffer = new byte[8192];
          int bytesRead;
          InputStream inputStream = part.getInputStream();
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            multipartBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
          }
          multipartBuilder.append("\r\n");
        } catch (IOException e) {
          log.error("Failed to read part: " + part, e);
          throw new RuntimeException(e);
        }
      } else if (fieldValue instanceof File) {
        // 处理文件字段
        File file = (File) fieldValue;
        multipartBuilder
            .append("Content-Disposition: form-data; name=\"")
            .append(fieldName)
            .append("\"; filename=\"")
            .append(file.getName())
            .append("\"\r\n");
        multipartBuilder.append("Content-Type: ").append(getContentType(file)).append("\r\n");
        multipartBuilder.append("\r\n");
        try (InputStream fileInputStream = Files.newInputStream(file.toPath())) {
          byte[] buffer = new byte[8192];
          int bytesRead;
          while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            multipartBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
          }
          multipartBuilder.append("\r\n");
        } catch (IOException e) {
          log.error("Failed to read file: " + file, e);
          throw new RuntimeException(e);
        }
      } else {
        // 处理普通字段
        String fieldValueString = fieldValue.toString();
        multipartBuilder
            .append("Content-Disposition: form-data; name=\"")
            .append(fieldName)
            .append("\"\r\n");
        multipartBuilder.append("\r\n");
        multipartBuilder.append(fieldValueString).append("\r\n");
      }
    }

    multipartBuilder.append("--").append(boundary).append("--");

    return multipartBuilder.toString();
  }

  public static String getContentType(File file) {
    // 根据文件扩展名或其他方式获取文件的 Content-Type
    // 这里只是简单示例，可以根据实际需求进行扩展
    String contentType = "application/octet-stream";
    String fileName = file.getName();
    String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
    if ("txt".equalsIgnoreCase(fileExtension)) {
      contentType = "text/plain";
    } else if ("jpg".equalsIgnoreCase(fileExtension) || "jpeg".equalsIgnoreCase(fileExtension)) {
      contentType = "image/jpeg";
    } else if ("png".equalsIgnoreCase(fileExtension)) {
      contentType = "image/png";
    }
    return contentType;
  }

  public static String extractBoundary(String multipartString) {
    String[] lines = multipartString.split("\r\n|\n\n");
    for (String line : lines) {
      if (line.startsWith("--")) {
        return line.substring(2);
      }
    }
    return null;
  }

  private static String extractFieldName(String part) {
    String[] lines = part.split("\r\n|\n\n");
    for (String line : lines) {
      if (line.startsWith("Content-Disposition")) {
        int startIndex = line.indexOf("name=\"") + 6;
        int endIndex = line.indexOf("\"", startIndex);
        return line.substring(startIndex, endIndex);
      }
    }
    return null;
  }

  private static String extractFileName(String part) {
    String[] lines = part.split("\r\n|\n\n");
    for (String line : lines) {
      if (line.startsWith("Content-Disposition")) {
        int startIndex = line.indexOf("filename=\"") + 10;
        int endIndex = line.indexOf("\"", startIndex);
        return line.substring(startIndex, endIndex);
      }
    }
    return null;
  }

  private static String extractFieldValue(String part) {
    String[] lines = part.split("\r\n|\n\n");
    boolean skipLines = true;
    StringBuilder valueBuilder = new StringBuilder();

    for (String line : lines) {
      if (skipLines) {
        skipLines = false;
        continue;
      }

      valueBuilder.append(line).append("\r\n");
    }

    return valueBuilder.toString().trim();
  }

  public static String extractFileContentType(String part) {
    String[] lines = part.split("\r\n|\n\n");
    for (String line : lines) {
      if (line.startsWith("Content-Type")) {
        int startIndex = line.indexOf(":") + 1;
        return line.substring(startIndex).trim();
      }
    }
    return null;
  }

  private static byte[] extractFileContent(String part) {
    String[] lines = part.split("\r\n|\n\n");
    StringBuilder contentBuilder = new StringBuilder();
    boolean startReading = false;
    for (String line : lines) {
      if ("".equals(line)) {
        startReading = true;
        continue;
      }
      if (startReading) {
        contentBuilder.append(line).append("\r\n");
      }
    }
    return contentBuilder.toString().getBytes();
  }
}
