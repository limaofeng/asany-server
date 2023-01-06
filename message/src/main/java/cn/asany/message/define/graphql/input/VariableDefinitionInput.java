package cn.asany.message.define.graphql.input;

import lombok.Data;

@Data
public class VariableDefinitionInput {
  private String name;
  private String description;
  private String example;
}
