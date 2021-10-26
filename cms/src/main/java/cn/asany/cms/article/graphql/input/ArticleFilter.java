package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.enums.ArticleStatus;
import cn.asany.cms.article.service.ArticleService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 文件筛选
 *
 * @author limaofeng
 * @date 2019-06-26 17:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleFilter extends QueryFilter<ArticleFilter, Article> {

  private PropertyFilterBuilder builder = new PropertyFilterBuilder();

  @JsonProperty("Keyword")
  public void setKeyword(String keyword) {
    builder.or(PropertyFilter.builder().contains("summary", keyword).contains("title", keyword));
  }

  public void setTags(List<Long> tags) {
    builder.in("tags.id", tags.toArray(new Long[0]));
  }

  public void setChannel(Long channel) {
    builder.equal("channels.id", channel);
  }

  public void setChannelCode(String channelCode) {
    builder.in("channels.code", channelCode);
  }

  @JsonProperty(value = "channel_in")
  public void setChannelIn(List<Long> ids) {
    builder.in("channels.id", ids);
  }

  @JsonProperty(value = "channels_isEmpty")
  public void setChannels_Empty(boolean onOrOff) {
    if (onOrOff) {
      builder.isEmpty("channels");
    }
  }

  @JsonProperty("status")
  public void setStatus(ArticleStatus status) {
    builder.equal("status", status);
  }

  private String creator;

  @JsonProperty("creator")
  public void setCreator(String creator) {
    this.creator = creator;
  }

  @JsonProperty("publishedAt")
  public void setPublishedAt(String publishedAt) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String[] publishDateArray = publishedAt.split("~");
    List<String> publishedAts = Arrays.asList(publishDateArray);
    builder.between(
        "publishedAt", format.parse(publishedAts.get(0)), format.parse(publishedAts.get(1)));
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

  @JsonProperty("validity")
  public void setValidity(Boolean validity) {
    if (validity) {
      this.builder.equal("validity", validity);
      PropertyFilterBuilder filter = PropertyFilter.builder();
      filter.greaterThan("validityStartDate", new Date());
      this.builder.and(filter);
      filter = PropertyFilter.builder();
      filter.lessThan("validityEndDate", new Date());
      this.builder.and(filter);
    }
  }
}
