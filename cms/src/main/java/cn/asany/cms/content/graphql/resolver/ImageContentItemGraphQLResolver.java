package cn.asany.cms.content.graphql.resolver;

import cn.asany.cms.content.domain.ImageItem;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class ImageContentItemGraphQLResolver implements GraphQLResolver<ImageItem> {
  public String url(ImageItem image) {
    if (StringUtil.isNotBlank(image.getUrl())) {
      return image.getUrl();
    }
    return image.getImage().getPath();
  }
}
