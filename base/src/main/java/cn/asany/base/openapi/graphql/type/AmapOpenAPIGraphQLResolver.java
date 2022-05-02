package cn.asany.base.openapi.graphql.type;

import cn.asany.base.openapi.apis.AmapOpenAPI;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Arrays;
import java.util.List;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

@Component
public class AmapOpenAPIGraphQLResolver implements GraphQLResolver<AmapOpenAPI> {

  private static final List<String> LOCAL_IP_ADDRESS =
      Arrays.asList("0:0:0:0:0:0:0:1", "127.0.0.1");

  public AmapOpenAPI.IpResult ip(AmapOpenAPI api, String _ip, DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();
    if (StringUtil.isBlank(_ip)) {
      String realIpAddress = WebUtil.getRealIpAddress(context.getRequest());
      if (!LOCAL_IP_ADDRESS.contains(realIpAddress)) {
        _ip = realIpAddress;
      }
    }
    return api.ip(_ip);
  }
}
