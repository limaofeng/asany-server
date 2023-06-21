package cn.asany.nuwa.app.graphql.subscription;

import cn.asany.nuwa.template.domain.ApplicationTemplateRoute;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

/**
 * 应用模板路由 GraphQL 订阅解析器
 *
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLSubscriptionResolver implements GraphQLSubscriptionResolver {

  private StockTickerRxPublisher stockTickerPublisher;

  public Publisher<ApplicationTemplateRoute> updateRoute() {
    return stockTickerPublisher.getPublisher();
  }
}
