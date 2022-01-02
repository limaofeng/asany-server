package cn.asany.email.mailbox.dao;

import cn.asany.email.mailbox.bean.JamesSubscription;
import org.jfantasy.framework.dao.jpa.JpaRepository;

public interface SubscriptionDao extends JpaRepository<JamesSubscription, Long> {}
