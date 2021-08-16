package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public class ApplicationGraphQLSubscriptionResolver implements GraphQLSubscriptionResolver {

  private StockTickerRxPublisher stockTickerPublisher;

  public Publisher<ApplicationTemplateRoute> updateRoute() {
    return stockTickerPublisher.getPublisher();
  }
}
