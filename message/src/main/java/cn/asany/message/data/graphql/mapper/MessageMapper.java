package cn.asany.message.data.graphql.mapper;

import cn.asany.message.data.domain.Message;
import cn.asany.message.data.domain.MessageRecipient;
import cn.asany.message.data.graphql.input.MessageCreateInput;
import cn.asany.message.data.graphql.input.MessageVariableValueInput;
import cn.asany.message.data.util.MessageUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Message Mapper
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MessageMapper {

  /**
   * toMessage
   *
   * @param input input
   * @return Message
   */
  @Mappings({
    @Mapping(source = "type", target = "type.id"),
    @Mapping(
        source = "variables",
        target = "variables",
        qualifiedByName = "toMessageVariableValues"),
    @Mapping(source = "recipients", target = "recipients", qualifiedByName = "formatRecipients")
  })
  Message toMessage(MessageCreateInput input);

  /**
   * formatRecipients
   *
   * @param recipients input
   * @return List
   */
  @Named("formatRecipients")
  default List<MessageRecipient> formatRecipient(List<String> recipients) {
    return recipients.stream().map(MessageUtils::recipient).collect(Collectors.toList());
  }

  /**
   * toMessageVariableValues
   *
   * @param values input
   * @return Map
   */
  @Named("toMessageVariableValues")
  default Map<String, Object> toMessageVariableValues(List<MessageVariableValueInput> values) {
    Map<String, Object> variables = new HashMap<>(10);
    values.forEach(value -> variables.put(value.getName(), value.getValue()));
    return variables;
  }
}
