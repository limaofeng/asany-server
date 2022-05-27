package cn.asany.email.mailbox.component.mail;

import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.mailbox.service.MailboxService;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.MessageUid;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.store.mail.AbstractLockingUidProvider;

public class JPAUidProvider extends AbstractLockingUidProvider {

  private MailboxService mailboxService;

  @Inject
  public JPAUidProvider(MailboxPathLocker locker) {
    super(locker);
  }

  @Inject
  public void setMailboxService(MailboxService mailboxService) {
    this.mailboxService = mailboxService;
  }

  @Override
  public Optional<MessageUid> lastUid(MailboxSession session, Mailbox mailbox) {
    JPAId mailboxId = (JPAId) mailbox.getMailboxId();
    Optional<Long> optional = this.mailboxService.findLastUid(mailboxId.getRawId());
    return optional.map(MessageUid::of);
  }

  @Override
  protected MessageUid lockedNextUid(MailboxSession session, Mailbox mailbox)
      throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      Optional<JamesMailbox> optional = this.mailboxService.findMailboxById(mailboxId.getRawId());
      if (!optional.isPresent()) {
        throw new MailboxException("Unable to save next uid for mailbox " + mailbox);
      }
      JamesMailbox m = optional.get();
      long uid = m.consumeUid();
      this.mailboxService.update(m);
      return MessageUid.of(uid);
    } catch (PersistenceException e) {
      throw new MailboxException("Unable to save next uid for mailbox " + mailbox, e);
    }
  }
}
