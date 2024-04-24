package cn.asany.openapi.graphql.type;

import cn.asany.openapi.apis.AmapOpenAPI;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
import net.asany.jfantasy.graphql.security.context.AuthGraphQLServletContext;
import org.springframework.stereotype.Component;

/**
 * AMAP OpenAPI
 *
 * @author limaofeng
 */
@Component
public class AmapOpenAPIGraphQLResolver implements GraphQLResolver<AmapOpenAPI> {

  public AmapOpenAPI.IpResult ip(AmapOpenAPI api, String ip, DataFetchingEnvironment environment) {
    AuthGraphQLServletContext context = environment.getContext();
    if (StringUtil.isBlank(ip)) {
      ip = WebUtil.getClientIP(context.getRequest());
    }
    return api.ip(ip);
  }
}
