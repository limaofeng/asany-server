package cn.asany.email.imap.manager;

import java.util.Set;
import org.apache.james.mailbox.events.*;
import reactor.core.publisher.Mono;

public class DefaultEventBus implements EventBus {
  @Override
  public Registration register(MailboxListener listener, RegistrationKey key) {
    return null;
  }

  @Override
  public Registration register(MailboxListener listener, Group group)
      throws GroupAlreadyRegistered {
    return null;
  }

  @Override
  public Mono<Void> dispatch(Event event, Set<RegistrationKey> key) {
    return null;
  }

  @Override
  public Mono<Void> reDeliver(Group group, Event event) {
    return null;
  }
}
