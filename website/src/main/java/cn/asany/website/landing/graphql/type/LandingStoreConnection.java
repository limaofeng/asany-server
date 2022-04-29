package cn.asany.website.landing.graphql.type;

import cn.asany.website.landing.bean.LandingStore;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class LandingStoreConnection
    extends BaseConnection<LandingStoreConnection.LandingStoreEdge> {

  private List<LandingStoreConnection.LandingStoreEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LandingStoreEdge implements Edge<LandingStore> {
    private String cursor;
    private LandingStore node;
  }
}
