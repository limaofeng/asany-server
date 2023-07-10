package cn.asany.message.define.graphql.mapper;

import cn.asany.message.define.domain.MessageTemplate;
import cn.asany.message.define.graphql.input.MessageTemplateCreateInput;
import org.mapstruct.*;

/**
 * 消息模板转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MessageTemplateConverter {

  /**
   * 转换为消息模板
   *
   * @param input 消息模板创建输入
   * @return 消息模板
   */
  @Mappings({@Mapping(target = "content", ignore = true)})
  MessageTemplate toMessageTemplate(MessageTemplateCreateInput input);
}
