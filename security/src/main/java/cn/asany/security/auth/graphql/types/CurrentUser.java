package cn.asany.security.auth.graphql.types;

import java.util.Set;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.core.GrantedAuthority;

public class CurrentUser extends LoginUser {

  private LoginUser user;

  public CurrentUser(LoginUser user) {
    this.user = user;
  }

  @Override
  public Long getUid() {
    return this.user.getUid();
  }

  @Override
  public String getType() {
    return this.user.getType();
  }

  @Override
  public String getName() {
    return this.user.getName();
  }

  @Override
  public String getTitle() {
    return this.user.getTitle();
  }

  @Override
  public String getAvatar() {
    return this.user.getAvatar();
  }

  @Override
  public String getPhone() {
    return this.user.getPhone();
  }

  @Override
  public String getEmail() {
    return this.user.getEmail();
  }

  @Override
  public String getSignature() {
    return this.user.getSignature();
  }

  @Override
  public Set<? extends GrantedAuthority> getAuthorities() {
    return this.user.getAuthorities();
  }
}
