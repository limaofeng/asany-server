package cn.asany.email.mailbox.component;

import cn.asany.email.user.domain.JamesSubscription;
import javax.inject.Inject;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.store.StoreSubscriptionManager;
import org.apache.james.mailbox.store.user.model.Subscription;

public class JPASubscriptionManager extends StoreSubscriptionManager {

  @Inject
  public JPASubscriptionManager(JPAMailboxSessionMapperFactory mapperFactory) {
    super(mapperFactory);
  }

  @Override
  protected Subscription createSubscription(MailboxSession session, String mailbox) {
    return new JamesSubscription(session.getUser().asString(), mailbox);
  }
}
