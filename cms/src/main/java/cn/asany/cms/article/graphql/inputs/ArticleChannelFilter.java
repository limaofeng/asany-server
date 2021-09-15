package cn.asany.cms.article.graphql.inputs;

import cn.asany.cms.article.service.ArticleService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;

@Data
public class ArticleChannelFilter {
  //  private ArticleTag articleTag = ArticleTag.builder().build();

  protected PropertyFilterBuilder builder = new PropertyFilterBuilder();

  @JsonProperty("name")
  public void setName(String name) {
    this.builder.equal("name", name);
  }

  @JsonProperty("path_startsWith")
  public void setPathStartsWith(String path) {
    this.builder.contains("path", path + "%");
  }

  @JsonProperty("parent")
  public void setParent(Long parent) {
    if (Long.valueOf(0).equals(parent)) {
      this.builder.isNull("parent.id");
    } else {
      this.builder.equal("parent.id", parent);
    }
  }

  @JsonProperty("viewer")
  public void setViewer(String viewer) {
    ArticleService articleService = SpringBeanUtils.getBeanByType(ArticleService.class);
    //        Map<String, String> viewerValue = articleService.getViewerValue(viewer);
    //        builder.and(new PermissionSpecification("ARTICLE_VIEWER", viewerValue));
  }

  @JsonProperty("founder")
  public void setFounder(String founder) {
    ArticleService articleService = SpringBeanUtils.getBeanByType(ArticleService.class);
    //        Map<String, String> viewerValue = articleService.getViewerValue(founder);
    //        builder.and(new PermissionSpecification("ARTICLE_CREATOR", viewerValue));
  }

  public List<PropertyFilter> build() {
    return builder.build();
  }
}
