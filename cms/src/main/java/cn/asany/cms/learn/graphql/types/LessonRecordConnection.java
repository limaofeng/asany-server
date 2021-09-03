package cn.asany.cms.learn.graphql.types;

import cn.asany.cms.learn.bean.LessonRecord;
import java.util.List;
import lombok.Data;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
public class LessonRecordConnection
    extends BaseConnection<LessonRecordConnection.LessonRecordEdge> {

  private List<LessonRecordConnection.LessonRecordEdge> edges;

  @Data
  public static class LessonRecordEdge implements Edge<LessonRecord> {
    private String cursor;
    private LessonRecord node;
  }
}
