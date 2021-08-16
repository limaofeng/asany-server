package cn.asany.security.core.graphql.inputs;

import cn.asany.security.core.graphql.enums.ResultTypeScope;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScopeTypeResult {
  // 类型
  private ResultTypeScope type;
  // key
  private String key;
  // value
  // private String value;
  // 中文描述
  private String title;
  // 支持的目标类型
  private List<SecurityTypeData> targetTypes;
}
