package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.service.ArticleCategoryService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import net.asany.jfantasy.framework.util.regexp.RegexpConstant;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 文章栏目筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleCategoryWhereInput
    extends WhereInput<ArticleCategoryWhereInput, ArticleCategory> {

  @JsonProperty("parent")
  public void setParent(AcceptArticleCategory acceptArticleCategory) {
    ArticleCategoryService service = SpringBeanUtils.getBean(ArticleCategoryService.class);
    if (acceptArticleCategory.getSubColumns()) {
      Optional<ArticleCategory> channelOptional =
          RegexpUtil.isMatch(acceptArticleCategory.getId(), RegexpConstant.VALIDATOR_INTEGE)
              ? service.findById(Long.valueOf(acceptArticleCategory.getId()))
              : service.findOneBySlug(acceptArticleCategory.getId());
      if (channelOptional.isPresent()) {
        ArticleCategory channel = channelOptional.get();
        this.filter.startsWith("path", channel.getPath()).notEqual("id", channel.getId());
      } else {
        this.filter.equal("parent.id", acceptArticleCategory.getId());
      }
    } else {
      this.filter.equal("parent.id", acceptArticleCategory.getId());
    }
  }
}
