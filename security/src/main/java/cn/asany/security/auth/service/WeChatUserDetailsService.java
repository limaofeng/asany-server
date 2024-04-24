package cn.asany.security.auth.service;

import cn.asany.security.auth.authentication.WeChatAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.security.core.userdetails.SimpleUserDetailsService;
import net.asany.jfantasy.framework.security.core.userdetails.UserDetails;
import net.asany.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 微信获取用户
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class WeChatUserDetailsService
    implements SimpleUserDetailsService<WeChatAuthenticationToken.WeChatCredentials> {

  @Override
  public UserDetails loadUserByToken(WeChatAuthenticationToken.WeChatCredentials credentials)
      throws UsernameNotFoundException {
    String authCode = credentials.getAuthCode();
    return null;
  }
}
