package cn.asany.cms.learn.graphql.types;

import cn.asany.cms.learn.bean.LearnerScope;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class LearnerScopeConnection
    extends BaseConnection<LearnerScopeConnection.LearnerScopeEdge, LearnerScope> {
  private List<LearnerScopeConnection.LearnerScopeEdge> edges;

  @Data
  public static class LearnerScopeEdge implements Edge<LearnerScope> {
    private String cursor;
    private LearnerScope node;
  }
}
