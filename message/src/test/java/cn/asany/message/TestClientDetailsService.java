package cn.asany.message;

import org.jfantasy.framework.security.oauth2.core.ClientDetails;
import org.jfantasy.framework.security.oauth2.core.ClientDetailsService;
import org.jfantasy.framework.security.oauth2.core.ClientRegistrationException;

public class TestClientDetailsService implements ClientDetailsService {
  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    return null;
  }
}
