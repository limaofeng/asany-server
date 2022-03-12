package cn.asany.email.user.component;

import cn.asany.email.user.service.MailUserService;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.core.MailAddress;
import org.apache.james.user.api.UsersRepository;
import org.apache.james.user.api.UsersRepositoryException;
import org.apache.james.user.api.model.User;
import org.springframework.stereotype.Component;

/**
 * 本地用户
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class JPAUsersRepository implements UsersRepository {

  private final MailUserService userService;

  public JPAUsersRepository(MailUserService userService) {
    this.userService = userService;
  }

  @Override
  public void addUser(String username, String password) throws UsersRepositoryException {
    log.debug("getUserByName:" + username, password);
  }

  @Override
  public User getUserByName(String name) throws UsersRepositoryException {
    log.debug("getUserByName:" + name);
    return null;
  }

  @Override
  public void updateUser(User user) throws UsersRepositoryException {
    log.debug(user.getUserName());
  }

  @Override
  public void removeUser(String name) throws UsersRepositoryException {
    log.debug(name);
  }

  @Override
  public boolean contains(String name) throws UsersRepositoryException {
    return this.userService.contains(name);
  }

  @Override
  public boolean test(String name, String password) throws UsersRepositoryException {
    return this.userService.test(name, password);
  }

  @Override
  public int countUsers() throws UsersRepositoryException {
    log.debug("countUsers");
    return 0;
  }

  @Override
  public Iterator<String> list() throws UsersRepositoryException {
    log.debug("list");
    return null;
  }

  @Override
  public boolean supportVirtualHosting() throws UsersRepositoryException {
    log.debug("supportVirtualHosting");
    return false;
  }

  @Override
  public String getUser(MailAddress mailAddress) throws UsersRepositoryException {
    return mailAddress.asString();
  }

  @Override
  public MailAddress getMailAddressFor(org.apache.james.core.User user)
      throws UsersRepositoryException {
    log.debug("getMailAddressFor");
    return null;
  }

  @Override
  public boolean isAdministrator(String username) throws UsersRepositoryException {
    log.debug("isAdministrator");
    return false;
  }

  @Override
  public boolean isReadOnly() {
    log.debug("isReadOnly");
    return false;
  }
}
