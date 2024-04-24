package cn.asany.cms.learn.graphql.types;

import cn.asany.cms.learn.domain.Learner;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class LearnerConnection
    extends BaseConnection<LearnerConnection.LearnerRecordEdge, Learner> {
  private List<LearnerConnection.LearnerRecordEdge> edges;

  @Data
  public static class LearnerRecordEdge implements Edge<Learner> {
    private String cursor;
    private Learner node;
  }
}
