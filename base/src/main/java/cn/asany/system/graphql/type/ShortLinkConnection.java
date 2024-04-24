package cn.asany.system.graphql.type;

import cn.asany.system.domain.ShortLink;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShortLinkConnection
    extends BaseConnection<ShortLinkConnection.ShortLinkEdge, ShortLink> {

  private List<ShortLinkEdge> edges;

  @Data
  public static class ShortLinkEdge implements Edge<ShortLink> {
    private String cursor;
    private ShortLink node;
  }
}
