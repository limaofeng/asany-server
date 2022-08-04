package cn.asany.pm.project.graphql.type;

import cn.asany.pm.project.domain.Project;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 问题分页
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectConnection extends BaseConnection<ProjectConnection.ProjectEdge, Project> {
  private List<ProjectEdge> edges;

  @Data
  public static class ProjectEdge implements Edge<Project> {
    private String cursor;
    private Project node;
  }
}
