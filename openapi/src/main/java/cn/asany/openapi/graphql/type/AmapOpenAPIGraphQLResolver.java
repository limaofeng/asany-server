package cn.asany.openapi.graphql.type;

import cn.asany.openapi.apis.AmapOpenAPI;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

/**
 * AMAP OpenAPI
 *
 * @author limaofeng
 */
@Component
public class AmapOpenAPIGraphQLResolver implements GraphQLResolver<AmapOpenAPI> {

  public AmapOpenAPI.IpResult ip(AmapOpenAPI api, String ip, DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();
    if (StringUtil.isBlank(ip)) {
      ip = WebUtil.getRealIpAddress(context.getRequest());
    }
    return api.ip(ip);
  }
}
