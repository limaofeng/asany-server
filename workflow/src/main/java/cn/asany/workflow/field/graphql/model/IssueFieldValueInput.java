package cn.asany.workflow.field.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-24 09:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueFieldValueInput {
  private String name;
  private String value;
}
