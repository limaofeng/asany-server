package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.service.ArticleChannelService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 文章栏目筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleChannelFilter extends QueryFilter<ArticleChannelFilter, ArticleChannel> {

  private Boolean descendant = false;

  @JsonProperty("parent")
  public void setParent(Long parent) {
    this.builder.equal("parent.id", parent);
  }

  @Override
  public List<PropertyFilter> build() {
    ArticleChannelService service = SpringBeanUtils.getBean(ArticleChannelService.class);
    List<PropertyFilter> filters = builder.build();
    PropertyFilter filter = ObjectUtil.find(filters, "propertyName", "parent.id");
    if (this.descendant && filter != null) {
      filters.remove(filter);
      ArticleChannel channel = service.findOne(filter.getPropertyValue());
      builder.startsWith("path", channel.getPath());
      builder.notEqual("id", channel.getId());
    }
    return builder.build();
  }
}
