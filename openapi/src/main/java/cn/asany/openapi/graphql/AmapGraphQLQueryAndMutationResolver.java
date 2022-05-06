package cn.asany.openapi.graphql;

import cn.asany.openapi.apis.AmapOpenAPI;
import cn.asany.openapi.configs.WeixinConfig;
import cn.asany.openapi.service.OpenAPIService;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.factory.WeixinSessionFactory;
import cn.asany.weixin.framework.session.WeixinApp;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmapGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {
  private final OpenAPIService openAPIService;
  private final WeixinSessionFactory weixinSessionFactory;

  public AmapGraphQLQueryAndMutationResolver(
      OpenAPIService openAPIService, WeixinSessionFactory weixinSessionFactory) {
    this.openAPIService = openAPIService;
    this.weixinSessionFactory = weixinSessionFactory;
  }

  public AmapOpenAPI amapOpenAPI() {
    return openAPIService.getDefaultAmap();
  }

  public WeixinApp weixin() throws WeixinException {
    WeixinConfig weixinConfig = openAPIService.getDefaultWeixin();
    weixinSessionFactory.openSession(weixinConfig.getId());
    return weixinConfig;
  }
}
