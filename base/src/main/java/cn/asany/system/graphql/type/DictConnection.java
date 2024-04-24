package cn.asany.system.graphql.type;

import cn.asany.system.domain.Dict;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictConnection extends BaseConnection<DictConnection.DictEdge, Dict> {

  private List<DictEdge> edges;

  @Data
  public static class DictEdge implements Edge<Dict> {
    private String cursor;
    private Dict node;
  }
}
