package cn.asany.weixin.graphql.resolver;

import cn.asany.weixin.framework.core.Jsapi;
import cn.asany.weixin.framework.exception.WeixinException;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 微信 Jsapi Graphql 接口
 *
 * @author limaofeng
 */
@Component
public class WeixinJsapiGraphQLResolver implements GraphQLResolver<Jsapi> {

  public String ticket(Jsapi jsapi) throws WeixinException {
    return jsapi.getTicket();
  }

  public Jsapi.Signature signature(Jsapi jsapi, String url) throws WeixinException {
    return jsapi.signature(url);
  }
}
