package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.ArticleChannel;
import cn.asany.cms.article.service.ArticleChannelService;
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
public class ArticleChannelFilter extends QueryFilter<ArticleChannelFilter, ArticleChannel> {

  @JsonProperty("parent")
  public void setParent(AcceptArticleChannel acceptArticleChannel) {
    ArticleChannelService service = SpringBeanUtils.getBean(ArticleChannelService.class);
    if (acceptArticleChannel.getSubColumns()) {
      Optional<ArticleChannel> channelOptional =
          service.findById(Long.valueOf(acceptArticleChannel.getId()));
      if (channelOptional.isPresent()) {
        ArticleChannel channel = channelOptional.get();
        this.builder.startsWith("path", channel.getPath()).notEqual("id", channel.getId());
      } else {
        this.builder.equal("parent.id", acceptArticleChannel.getId());
      }
    } else {
      this.builder.equal("parent.id", acceptArticleChannel.getId());
    }
  }
}
