package cn.asany.message.define.domain.toys;

import cn.asany.message.data.utils.MessageUtils;
import java.util.Map;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;

/**
 * 消息内容
 *
 * @author limaofeng
 */
public class MessageContent {

  public static final String DATA_BOUNDARY = "--MessageContentBoundary--";
  public static final String CONTENT = "content";
  public static final String TITLE = "title";

  private final Map<String, Object> variables;

  public MessageContent(Map<String, Object> variables) {
    this.variables = variables;
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
