package cn.asany.cms.content.graphql.resolver;

import cn.asany.cms.content.domain.VideoContent;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class VideoContentGraphQLResolver implements GraphQLResolver<VideoContent> {
  public String url(VideoContent content) {
    if (StringUtil.isNotBlank(content.getUrl())) {
      return content.getUrl();
    }
    return content.getVideo().getPath();
  }
}
