package cn.asany.cms.article.domain;

import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.ui.resources.domain.Component;
import javax.persistence.*;
import lombok.*;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageComponent {
  @Column(name = "PAGE_ENABLED", nullable = false)
  private Boolean enabled;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROUTE_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_CATEGORY_PAGE_ROUTE"))
  @ToString.Exclude
  private ApplicationRoute route;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "COMPONENT_ID",
      foreignKey = @ForeignKey(name = "FK_ARTICLE_CATEGORY_PAGE_COMPONENT"))
  @ToString.Exclude
  private Component component;
}
