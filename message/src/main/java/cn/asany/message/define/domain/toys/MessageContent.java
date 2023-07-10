package cn.asany.message.define.domain.toys;

import cn.asany.message.data.util.MessageUtils;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;

/**
 * 消息内容
 *
 * @author limaofeng
 */
public class MessageContent implements Serializable {

  public static final String DATA_BOUNDARY = "--MessageContentBoundary--";
  public static final String CONTENT = "content";
  public static final String TITLE = "title";

  private final Map<String, Object> variables;

  public MessageContent(Map<String, Object> variables) {
    this.variables = variables;
  }

  public static MessageContent empty() {
    return new MessageContent(Collections.emptyMap());
  }

  public String getTitle() {
    return get(TITLE, String.class);
  }

  public String getContent() {
    return get(CONTENT, String.class);
  }

  public void setTitle(String title) {
    variables.put(TITLE, title);
  }

  public String getTitle(Map<String, Object> data) {
    return processTemplateIntoString(TITLE, data);
  }

  public String getContent(Map<String, Object> data) {
    return processTemplateIntoString(CONTENT, data);
  }

  public String processTemplateIntoString(String key, Map<String, Object> data) {
    String templateInline = get(key, String.class);
    return HandlebarsTemplateUtils.processTemplateIntoString(templateInline, data);
  }

  public void setContent(String content) {
    variables.put(CONTENT, content);
  }

  public void remove(String key) {
    variables.remove(key);
  }

  public void clear() {
    variables.clear();
  }

  public String get(String key) {
    return get(key, String.class);
  }

  public <T> T get(String key, Class<T> type) {
    Object value = variables.get(key);
    if (value == null) {
      return null;
    }
    return ReflectionUtils.convert(value, type);
  }

  public void set(String key, Object value) {
    variables.put(key, value);
  }

  @Override
  public String toString() {
    return MessageUtils.convertToMultipartString(variables, DATA_BOUNDARY);
  }

  public static MessageContent of(String content) {
    return new MessageContent(MessageUtils.parseMultipartString(content, DATA_BOUNDARY));
  }
}
