package cn.asany.pm.issue.attribute.graphql.connection;

import cn.asany.pm.issue.attribute.domain.Status;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

/**
 * 问题状态 分页对象
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StatusConnection extends BaseConnection<StatusConnection.StatusEdge, Status> {
  private List<StatusEdge> edges;

  @Data
  public static class StatusEdge implements Edge<Status> {
    private String cursor;
    private Status node;
  }
}
