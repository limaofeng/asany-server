package cn.asany.email.user.service;

import cn.asany.email.user.bean.JamesSubscription;
import cn.asany.email.user.dao.SubscriptionDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionService {

  @Autowired private SubscriptionDao subscriptionDao;

  public Optional<JamesSubscription> findFindMailboxSubscriptionForUser(
      String user, String mailbox) {
    return this.subscriptionDao.findOne(
        PropertyFilter.builder().equal("username", user).equal("mailbox", mailbox).build());
  }

  public List<JamesSubscription> findSubscriptionsForUser(String user) {
    return this.subscriptionDao.findAll(PropertyFilter.builder().equal("username", user).build());
  }

  public void delete(long id) {
    this.subscriptionDao.deleteById(id);
  }
}
