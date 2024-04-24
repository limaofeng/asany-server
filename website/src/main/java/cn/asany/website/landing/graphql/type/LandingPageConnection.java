package cn.asany.website.landing.graphql.type;

import cn.asany.website.landing.domain.LandingPage;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class LandingPageConnection
    extends BaseConnection<LandingPageConnection.LandingPageEdge, LandingPage> {

  private List<LandingPageConnection.LandingPageEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LandingPageEdge implements Edge<LandingPage> {
    private String cursor;
    private LandingPage node;
  }
}
