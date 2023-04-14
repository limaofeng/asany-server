package cn.asany.message.data.graphql.mapper;

import cn.asany.message.data.domain.Message;
import cn.asany.message.data.graphql.input.MessageCreateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MessageMapper {

  @Mappings({
    @Mapping(source = "type", target = "type.id"),
  })
  Message toMessage(MessageCreateInput input);
}
