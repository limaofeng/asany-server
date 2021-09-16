package cn.asany.cms.article.graphql.input;

import java.util.List;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

/** @Description @Author ChenWenJie @Data 2020/10/22 11:24 上午 */
@Data
public class RecommendFilter {
  private PropertyFilterBuilder builder = new PropertyFilterBuilder();

  public void setName(String name) {
    this.builder.contains("name", name);
  }

  public void setEnableProcess(Boolean enableProcess) {
    this.builder.equal("enableProcess", enableProcess);
  }

  public List<PropertyFilter> build() {
    return this.builder.build();
  }
}
