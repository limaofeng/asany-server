package cn.asany.shanhai.data.engine.graphql;

import cn.asany.shanhai.data.engine.IDataSourceOptions;
import lombok.*;

/**
 * GraphQL 数据源配置
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraphQLDataSourceOptions extends IDataSourceOptions {
  private String url;
}
