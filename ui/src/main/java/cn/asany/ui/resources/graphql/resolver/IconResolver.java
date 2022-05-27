package cn.asany.ui.resources.graphql.resolver;

import cn.asany.ui.resources.domain.Icon;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class IconResolver implements GraphQLResolver<Icon> {

  public Long libraryId(Icon icon) {
    String libraryId = icon.get(Icon.METADATA_LIBRARY_ID);
    if (StringUtil.isBlank(libraryId)) {
      return null;
    }
    return Long.valueOf(libraryId);
  }
}
