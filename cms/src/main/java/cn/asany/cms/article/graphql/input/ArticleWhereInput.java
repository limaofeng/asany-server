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
    filter.or(PropertyFilter.newFilter().contains("summary", keyword).contains("title", keyword));
  }

  public void setTags(List<Long> tags) {
    filter.in("tags.id", tags);
  }

  public void setCategory(AcceptArticleCategory acceptArticleCategory) {
    ArticleCategoryService service = SpringBeanUtils.getBean(ArticleCategoryService.class);
    if (acceptArticleCategory.getSubColumns()) {
      Optional<ArticleCategory> channelOptional =
          service.findById(Long.valueOf(acceptArticleCategory.getId()));
      if (channelOptional.isPresent()) {
        ArticleCategory channel = channelOptional.get();
        this.filter.startsWith("category.path", channel.getPath()).notEqual("id", channel.getId());
      } else {
        this.filter.equal("category.id", acceptArticleCategory.getId());
      }
    } else {
      this.filter.equal("category.id", acceptArticleCategory.getId());
    }
  }

  public void setChannelCode(String channelCode) {
    filter.in("channels.code", channelCode);
  }

  @JsonProperty(value = "channel_in")
  public void setChannelIn(List<Long> ids) {
    filter.in("channels.id", ids);
  }

  @JsonProperty(value = "channels_isEmpty")
  public void setChannels_Empty(boolean onOrOff) {
    if (onOrOff) {
      filter.isEmpty("channels");
    }
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
