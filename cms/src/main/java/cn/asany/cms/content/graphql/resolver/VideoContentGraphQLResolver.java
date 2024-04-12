package cn.asany.cms.content.graphql.resolver;

import cn.asany.cms.content.domain.VideoContent;
import cn.asany.storage.dto.SimpleFileObject;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

@Component
public class VideoContentGraphQLResolver implements GraphQLResolver<VideoContent> {
  public String url(VideoContent content, DataFetchingEnvironment environment) {
    String url = content.getUrl();
    if (content.getVideo() != null) {
      SimpleFileObject fileObject = ((SimpleFileObject) content.getVideo());
      url = "/preview/" + fileObject.getId();
    }
    if (StringUtil.isBlank(url)) {
      return null;
    }
    if (url.startsWith("http://") || url.startsWith("https://")) {
      return url;
    }
    AuthorizationGraphQLServletContext context = environment.getContext();
    return WebUtil.getServerUrl(context.getRequest()) + url;
  }
}
