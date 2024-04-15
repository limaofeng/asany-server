package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.service.ArticleCategoryService;
import cn.asany.cms.article.service.ArticleService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 文件筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleWhereInput extends WhereInput<ArticleWhereInput, Article> {

  public void setKeyword(String keyword) {
    filter.or(
        PropertyFilter.newFilter().contains("summary", keyword),
        PropertyFilter.newFilter().contains("title", keyword));
  }

  public void setTags(List<Long> tags) {
    filter.in("tags.id", tags);
  }

  public void setCategory(AcceptArticleCategory acceptArticleCategory) {
    ArticleCategoryService service = SpringBeanUtils.getBean(ArticleCategoryService.class);
    boolean hasId =
        RegexpUtil.isMatch(acceptArticleCategory.getId(), RegexpConstant.VALIDATOR_INTEGE);
    if (acceptArticleCategory.getSubColumns()) {
      Optional<ArticleCategory> channelOptional =
          hasId
              ? service.findById(Long.valueOf(acceptArticleCategory.getId()))
              : service.findOneBySlug(acceptArticleCategory.getId());
      if (channelOptional.isPresent()) {
        ArticleCategory channel = channelOptional.get();
        this.filter.startsWith("category.path", channel.getPath()).notEqual("id", channel.getId());
      } else {
        throw new RuntimeException("栏目不存在");
      }
    } else {
      if (hasId) {
        this.filter.equal("category.id", Long.valueOf(acceptArticleCategory.getId()));
      } else {
        this.filter.equal("category.slug", acceptArticleCategory.getId());
      }
    }
  }

  public void setCategorySlug(String code) {
    filter.in("category.slug", code);
  }

  @JsonProperty(value = "category_in")
  public void setCategoryIn(List<Long> ids) {
    filter.in("category.id", ids);
  }

  @JsonProperty("publishedAt")
  public void setPublishedAt(String publishedAt) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String[] publishDateArray = publishedAt.split("~");
    List<String> publishedAts = Arrays.asList(publishDateArray);
    filter.between(
        "publishedAt", format.parse(publishedAts.get(0)), format.parse(publishedAts.get(1)));
  }

  @JsonProperty("viewer")
  public void setViewer(String viewer) {
    ArticleService articleService = SpringBeanUtils.getBeanByType(ArticleService.class);
    //        Map<String, String> viewerValue = articleService.getViewerValue(viewer);
    //        builder.and(new PermissionSpecification("ARTICLE_VIEWER", viewerValue));
  }

  @JsonProperty("validity")
  public void setValidity(Boolean validity) {
    if (validity) {
      this.filter.equal("validity", validity);
      PropertyFilter filter = PropertyFilter.newFilter();
      filter.greaterThan("validityStartDate", new Date());
      this.filter.and(filter);
      filter = PropertyFilter.newFilter();
      filter.lessThan("validityEndDate", new Date());
      this.filter.and(filter);
    }
  }
}
