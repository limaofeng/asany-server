package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.ClientSecret;
import cn.asany.security.oauth.domain.AccessToken;
import cn.asany.security.oauth.service.AccessTokenService;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 客户端密钥
 *
 * @author limaofeng
 */
@Component
public class ClientSecretGraphQLResolver implements GraphQLResolver<ClientSecret> {

  @Autowired private AccessTokenService accessTokenService;

  public String secret(ClientSecret clientSecret, DataFetchingEnvironment environment) {
    boolean isCreated =
        environment.getExecutionStepInfo().getPath().toString().startsWith("/createApplication/");
    return isCreated ? clientSecret.getSecret() : "*****" + clientSecret.getSecret().substring(32);
  }

  public Date lastUseTime(ClientSecret clientSecret) {
    return accessTokenService.getLastUseTime(clientSecret.getClient(), clientSecret.getSecret());
  }
}
