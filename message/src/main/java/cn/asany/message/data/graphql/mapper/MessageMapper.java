/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
