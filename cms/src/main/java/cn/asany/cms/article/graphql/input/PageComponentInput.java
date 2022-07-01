package cn.asany.cms.article.graphql.input;

import cn.asany.ui.resources.domain.toy.ComponentData;
import java.util.List;
import lombok.Data;

@Data
public class PageComponentInput {
  private Boolean enabled;
  private String path;
  private String template;
  private List<ComponentData> blocks;
}
