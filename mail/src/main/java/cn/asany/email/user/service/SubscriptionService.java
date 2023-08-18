package cn.asany.email.user.service;

import cn.asany.email.user.dao.SubscriptionDao;
import cn.asany.email.user.domain.JamesSubscription;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  private final SubscriptionDao subscriptionDao;

  public SubscriptionService(SubscriptionDao subscriptionDao) {
    this.subscriptionDao = subscriptionDao;
  }

  public Optional<JamesSubscription> findFindMailboxSubscriptionForUser(
      String user, String mailbox) {
    return this.subscriptionDao.findOne(
        PropertyFilter.newFilter().equal("username", user).equal("mailbox", mailbox));
  }

  public List<JamesSubscription> findSubscriptionsForUser(String user) {
    return this.subscriptionDao.findAll(PropertyFilter.newFilter().equal("username", user));
  }

  public void delete(long id) {
    this.subscriptionDao.deleteById(id);
  }
}
