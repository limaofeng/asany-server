package cn.asany.weixin.graphql.resolver;

import cn.asany.weixin.framework.core.Jsapi;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.user.User;
import cn.asany.weixin.framework.session.WeixinApp;
import cn.asany.weixin.framework.session.WeixinSession;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 微信接口
 *
 * @author limaofeng
 */
@Component
public class WeixinAppGraphQLResolver implements GraphQLResolver<WeixinApp> {

  public List<User> users(WeixinApp app) throws WeixinException {
    WeixinSession session = app.getSession();
    return session.getUsers();
  }

  public Jsapi jsapi(WeixinApp app) throws WeixinException {
    WeixinSession session = app.getSession();
    Jsapi jsapi = session.getJsapi();
    if (jsapi == null) {
      throw new WeixinException(" jsapi is null ");
    }
    return jsapi;
  }
}
