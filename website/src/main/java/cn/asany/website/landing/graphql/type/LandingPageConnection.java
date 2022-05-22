package cn.asany.website.landing.graphql.type;

import cn.asany.website.landing.bean.LandingPage;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

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
