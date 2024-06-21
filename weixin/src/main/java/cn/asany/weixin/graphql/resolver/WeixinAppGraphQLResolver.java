/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.weixin.graphql.resolver;

import cn.asany.weixin.framework.core.Jsapi;
import cn.asany.weixin.framework.core.Openapi;
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

  public Openapi openapi(WeixinApp app) throws WeixinException {
    WeixinSession session = app.getSession();
    Openapi openapi = session.getOpenapi();
    if (openapi == null) {
      throw new WeixinException(" openapi is null ");
    }
    return openapi;
  }
}
