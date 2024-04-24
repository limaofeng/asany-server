package cn.asany.myjob.factory.graphql.type;

import cn.asany.myjob.factory.domain.Screen;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

/**
 * 角色查询接口
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ScreenConnection extends BaseConnection<ScreenConnection.ScreenEdge, Screen> {

  private List<ScreenEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScreenEdge implements Edge<Screen> {
    private String cursor;
    private Screen node;
  }
}
