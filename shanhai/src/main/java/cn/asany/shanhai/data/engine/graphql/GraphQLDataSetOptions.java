package cn.asany.shanhai.data.engine.graphql;

import cn.asany.shanhai.data.engine.IDataSetOptions;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GraphQL DataSet Options
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphQLDataSetOptions implements IDataSetOptions {
  private String gql;
  private String operationName;
  private Map<String, String> variables;
}
