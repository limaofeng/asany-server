package cn.asany;

import net.asany.jfantasy.framework.security.auth.core.ClientDetails;
import net.asany.jfantasy.framework.security.auth.core.ClientDetailsService;
import net.asany.jfantasy.framework.security.auth.core.ClientRegistrationException;

public class TestClientDetailsService implements ClientDetailsService {
  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    return null;
  }
}
