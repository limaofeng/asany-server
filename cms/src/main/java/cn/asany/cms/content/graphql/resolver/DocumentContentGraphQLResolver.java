package cn.asany.cms.content.graphql.resolver;

import cn.asany.cms.content.domain.DocumentContent;
import graphql.kickstart.tools.GraphQLResolver;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class DocumentContentGraphQLResolver implements GraphQLResolver<DocumentContent> {

  public String url(DocumentContent content) {
    if (StringUtil.isNotBlank(content.getUrl())) {
      return content.getUrl();
    }
    return content.getDocument().getPath();
  }

  public Long size(DocumentContent content) {
    if (content.getSize() != null) {
      return content.getSize();
    }
    return content.getDocument().getSize();
  }
}
