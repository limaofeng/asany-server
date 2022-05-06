package cn.asany.weixin.graphql;

import cn.asany.weixin.framework.factory.WeixinSessionFactory;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信
 *
 * @author limaofeng
 */
@Component
public class WeixinGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final WeixinSessionFactory weixinSessionFactory;

  @Autowired
  public WeixinGraphQLQueryAndMutationResolver(WeixinSessionFactory weixinSessionFactory) {
    this.weixinSessionFactory = weixinSessionFactory;
  }
}
