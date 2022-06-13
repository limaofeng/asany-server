package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.service.ArticleCategoryService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 文章栏目筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleCategoryFilter extends QueryFilter<ArticleCategoryFilter, ArticleCategory> {

  @JsonProperty("parent")
  public void setParent(AcceptArticleCategory acceptArticleCategory) {
    ArticleCategoryService service = SpringBeanUtils.getBean(ArticleCategoryService.class);
    if (acceptArticleCategory.getSubColumns()) {
      Optional<ArticleCategory> channelOptional =
          service.findById(Long.valueOf(acceptArticleCategory.getId()));
      if (channelOptional.isPresent()) {
        ArticleCategory channel = channelOptional.get();
        this.builder.startsWith("path", channel.getPath()).notEqual("id", channel.getId());
      } else {
        this.builder.equal("parent.id", acceptArticleCategory.getId());
      }
    } else {
      this.builder.equal("parent.id", acceptArticleCategory.getId());
    }
  }
}
