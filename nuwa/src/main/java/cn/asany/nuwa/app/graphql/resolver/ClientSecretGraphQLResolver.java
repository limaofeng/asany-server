package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.ClientSecret;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

/**
 * 客户端密钥
 *
 * @author limaofeng
 */
@Component
public class ClientSecretGraphQLResolver implements GraphQLResolver<ClientSecret> {

  public String secret(ClientSecret clientSecret, DataFetchingEnvironment environment) {
    boolean isCreated =
        environment.getExecutionStepInfo().getPath().toString().startsWith("/createApplication/");
    return isCreated ? clientSecret.getSecret() : "*****" + clientSecret.getSecret().substring(32);
  }
}
