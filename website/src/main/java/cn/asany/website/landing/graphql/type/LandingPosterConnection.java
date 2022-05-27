package cn.asany.website.landing.graphql.type;

import cn.asany.website.landing.domain.LandingPoster;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class LandingPosterConnection
    extends BaseConnection<LandingPosterConnection.LandingPosterEdge, LandingPoster> {

  private List<LandingPosterConnection.LandingPosterEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LandingPosterEdge implements Edge<LandingPoster> {
    private String cursor;
    private LandingPoster node;
  }
}
