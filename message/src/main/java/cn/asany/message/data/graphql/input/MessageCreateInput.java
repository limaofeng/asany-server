package cn.asany.message.data.graphql.input;

import java.util.List;
import lombok.Data;

@Data
public class MessageCreateInput {
  private String type;
  private List<MessageVariableValueInput> variables;
  private String uri;
}
