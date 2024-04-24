package cn.asany.ui.resources.graphql.resolver;

import cn.asany.ui.resources.domain.Component;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;

@org.springframework.stereotype.Component
public class ComponentResolver implements GraphQLResolver<Component> {

  public Long libraryId(Component component) {
    String libraryId = component.get(Component.METADATA_LIBRARY_ID);
    if (StringUtil.isBlank(libraryId)) {
      return null;
    }
    return Long.valueOf(libraryId);
  }

  public List<String> tags(Component component) {
    return ObjectUtil.defaultValue(component.getTags(), Collections::emptyList);
  }
}
