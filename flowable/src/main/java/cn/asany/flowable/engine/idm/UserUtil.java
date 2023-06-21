package cn.asany.flowable.engine.idm;

import org.flowable.idm.api.User;
import org.flowable.ui.common.model.RemoteUser;
import org.jfantasy.framework.security.LoginUser;

/**
 * 用户工具类
 *
 * @author limaofeng
 */
public class UserUtil {

  public static User toUser(LoginUser loginUser) {
    if (loginUser == null) {
      loginUser = LoginUser.builder().uid(1L).build();
    }
    RemoteUser user = new RemoteUser();
    user.setId(String.valueOf(loginUser.getUid()));
    user.setFirstName(loginUser.getName());
    user.setEmail(loginUser.getEmail());
    return user;
  }
}
