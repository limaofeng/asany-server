package cn.asany.message.define.domain.converter;

import cn.asany.message.define.domain.toys.MessageContent;
import jakarta.persistence.AttributeConverter;

/**
 * 消息内容转换器
 *
 * @author limaofeng
 */
public class MessageContentConverter implements AttributeConverter<MessageContent, String> {
  @Override
  public String convertToDatabaseColumn(MessageContent attribute) {
    return attribute.toString();
  }

  @Override
  public MessageContent convertToEntityAttribute(String dbData) {
    return MessageContent.of(dbData);
  }
}
