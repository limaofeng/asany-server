package cn.asany.security.auth.checker;

import org.jfantasy.framework.security.core.userdetails.PostUserDetailsChecker;
import org.jfantasy.framework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 多点登录验证
 *
 * @author limaofeng
 */
@Component
public class MultipointLoginChecker implements PostUserDetailsChecker {

  @Override
  public void check(UserDetails user) {
    System.out.println("...MultipointLoginChecker");
  }
}
