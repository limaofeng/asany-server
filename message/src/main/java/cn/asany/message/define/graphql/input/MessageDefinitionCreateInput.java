package cn.asany.message.define.graphql.input;

import java.util.List;
import lombok.Data;

@Data
public class MessageDefinitionCreateInput {
  private String name;
  private Boolean system;
  private List<VariableDefinitionInput> variables;
  private List<MappingVariableInput> mappingVariables;
  //    template: MessageTemplateCombinedInput!
  //    reminders: [MessageDefinitionReminderCreateInput!]!
}
