package cn.asany.cms.learn.graphql.types;

import cn.asany.cms.learn.bean.Course;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 课程分页对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseConnection extends BaseConnection<CourseConnection.CourseEdge, Course> {

  private List<CourseEdge> edges;

  @Data
  public static class CourseEdge implements Edge<Course> {
    private String cursor;
    private Course node;
  }
}
