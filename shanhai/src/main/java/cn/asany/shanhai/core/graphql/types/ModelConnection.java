package cn.asany.shanhai.core.graphql.types;

import cn.asany.shanhai.core.domain.Model;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * Model 分页对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModelConnection extends BaseConnection<ModelConnection.ModelEdge, Model> {
  private List<ModelEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ModelEdge implements Edge<Model> {
    private String cursor;
    private Model node;
  }
}
