package cn.asany.message.define.graphql.input;

import cn.asany.message.define.domain.enums.TemplateType;
import java.util.List;
import lombok.Data;

@Data
public class MessageTemplateCreateInput {
  private String name;
  private TemplateType type;
  private List<VariableDefinitionInput> variables;
  private String sign;
  private String code;
  private String content;
}
