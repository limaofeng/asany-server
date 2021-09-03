package cn.asany.cms.learn.graphql.types;

import cn.asany.cms.learn.bean.Learner;
import java.util.List;
import lombok.Data;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
public class LearnerConnection extends BaseConnection<LearnerConnection.LearnerRecordEdge> {
  private List<LearnerConnection.LearnerRecordEdge> edges;

  @Data
  public static class LearnerRecordEdge implements Edge<Learner> {
    private String cursor;
    private Learner node;
  }
}
