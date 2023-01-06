package cn.asany.message.define.converter;

import cn.asany.message.define.domain.MessageTemplate;
import cn.asany.message.define.graphql.input.MessageTemplateCreateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MessageTemplateConverter {
  @Mappings({})
  MessageTemplate toMessageTemplate(MessageTemplateCreateInput input);
}
