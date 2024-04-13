package cn.asany.cms.content.graphql.resolver;

import cn.asany.cms.content.domain.ImageItem;
import cn.asany.storage.dto.SimpleFileObject;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ImageContentItemGraphQLResolver implements GraphQLResolver<ImageItem> {

  @Autowired protected Environment environment;

  public String url(ImageItem image) {
    if (StringUtil.isNotBlank(image.getUrl())) {
      return image.getUrl();
    }
    SimpleFileObject fileObject = ((SimpleFileObject) image.getImage());
    if (StringUtil.isBlank(fileObject.getUrl())) {
      fileObject.setUrl(
          environment.getProperty("STORAGE_BASE_URL") + "/preview/" + fileObject.getId());
    }
    return fileObject.getUrl();
  }
}
