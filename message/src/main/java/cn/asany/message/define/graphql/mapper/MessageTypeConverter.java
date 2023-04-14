package cn.asany.message.define.graphql.mapper;

import cn.asany.message.define.domain.MessageType;
import cn.asany.message.define.graphql.input.MessageTypeCreateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MessageTypeConverter {
  @Mappings({})
  MessageType toMessageType(MessageTypeCreateInput input);
}
