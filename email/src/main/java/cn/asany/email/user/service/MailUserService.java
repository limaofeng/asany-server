package cn.asany.email.user.service;

import cn.asany.email.domainlist.bean.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import cn.asany.email.mailbox.bean.JamesMailbox;
import cn.asany.email.mailbox.dao.MailboxDao;
import cn.asany.email.user.bean.MailSettings;
import cn.asany.email.user.bean.MailUser;
import cn.asany.email.user.bean.toys.UpdateMode;
import cn.asany.email.user.dao.MailSettingsDao;
import cn.asany.email.user.dao.MailUserDao;
import cn.asany.security.core.bean.User;
import cn.asany.security.core.dao.UserDao;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.model.MailboxConstants;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮件用户服务
 *
 * @author limaofeng
 */
@Service("JamesUserService")
public class MailUserService {

  private final UserDao userDao;
  private final MailUserDao mailUserDao;
  private final MailboxDao mailboxDao;
  private final DomainService domainService;
  private final MailSettingsDao mailSettingsDao;

  public MailUserService(
      MailUserDao mailUserDao,
      MailboxDao mailboxDao,
      DomainService domainService,
      UserDao userDao,
      MailSettingsDao mailSettingsDao) {
    this.mailUserDao = mailUserDao;
    this.mailboxDao = mailboxDao;
    this.domainService = domainService;
    this.userDao = userDao;
    this.mailSettingsDao = mailSettingsDao;
  }

  public boolean test(String name, String password) {
    Optional<MailUser> optional = this.mailUserDao.findById(name);
    return optional.map(mailUser -> mailUser.verifyPassword(password)).orElse(false);
  }

  public boolean contains(String name) {
    return this.mailUserDao.existsById(name);
  }

  @Transactional
  public void createUser(Long id) {
    this.createUser(this.userDao.getById(id));
  }

  @Transactional
  public MailUser repairUser(Long id) {
    User user = this.userDao.getById(id);
    JamesDomain domain = this.domainService.getDefaultDomain();
    String username = user.getUsername() + "@" + domain.getName();
    Optional<MailUser> mailUser = this.mailUserDao.findById(username);
    if (!mailUser.isPresent()) {
      return createUser(user);
    }
    initMailboxes(mailUser.get(), false);
    return mailUser.get();
  }

  public MailUser createUser(User user) {
    JamesDomain domain = this.domainService.getDefaultDomain();
    String username = user.getUsername() + "@" + domain.getName();
    MailUser mailUser =
        this.mailUserDao.save(
            MailUser.builder()
                .name(username)
                .domain(domain)
                .user(user)
                .password(user.getPassword())
                .alg(MailUser.ALGORITHM_NONE)
                .build());
    initMailboxes(mailUser, true);
    return mailUser;
  }

  private Set<String> getDefaultMailboxes() {
    Set<String> mailboxes = new HashSet<>(DefaultMailboxes.DEFAULT_MAILBOXES);
    mailboxes.add(DefaultMailboxes.ARCHIVE);
    return mailboxes;
  }

  private void initMailboxes(MailUser mailUser, boolean added) {
    Set<String> mailboxes = getDefaultMailboxes();

    List<JamesMailbox> _mailboxes =
        mailboxes.stream()
            .map(
                mailbox ->
                    JamesMailbox.builder()
                        .name(mailbox)
                        .namespace(MailboxConstants.USER_NAMESPACE)
                        .lastUid(0)
                        .highestModSeq(0)
                        .uidValidity(0)
                        .user(mailUser.getUserName())
                        .build())
            .collect(Collectors.toList());
    if (added) {
      this.mailboxDao.saveAllInBatch(_mailboxes);
    } else {
      this.mailboxDao.saveAllInBatch(
          _mailboxes.stream()
              .filter(
                  item ->
                      !this.mailboxDao.exists(
                          PropertyFilter.builder()
                              .equal("user", item.getUser())
                              .equal("name", item.getName())
                              .equal("namespace", item.getNamespace())
                              .build()))
              .collect(Collectors.toList()));
    }
  }

  public Set<String> getFavoriteMailboxes(String user) {
    Optional<MailSettings> settings = this.mailSettingsDao.findOneBy("user.id", user);
    assert settings.isPresent();
    return settings.get().getMailboxes();
  }

  public MailUser getMailUser(String account) {
    return null;
  }

  public Set<String> updateMyFavoriteMailboxes(
      String user, Set<String> mailboxes, UpdateMode mode) {
    Optional<MailSettings> settingsOptional = this.mailSettingsDao.findOneBy("user.id", user);
    MailSettings settings = settingsOptional.orElseGet(() -> initializeMailSettings(user));
    if (mode == UpdateMode.ADD) {
      settings.getMailboxes().addAll(mailboxes);
    } else if (mode == UpdateMode.REMOVE) {
      settings.getMailboxes().removeAll(mailboxes);
    } else {
      settings.setMailboxes(mailboxes);
    }
    return this.mailSettingsDao.update(settings).getMailboxes();
  }

  private MailSettings initializeMailSettings(String user) {
    MailSettings settings =
        MailSettings.builder()
            .user(MailUser.builder().name(user).build())
            .mailboxes(getDefaultMailboxes())
            .build();
    return this.mailSettingsDao.save(settings);
  }
}
