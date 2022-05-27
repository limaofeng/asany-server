package cn.asany.email.mailbox.component.mail;

import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.mailbox.service.MailboxService;
import java.util.Optional;
import javax.inject.Inject;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.store.mail.AbstractLockingModSeqProvider;

public class JPAModSeqProvider extends AbstractLockingModSeqProvider {

  private MailboxService mailboxService;

  @Inject
  public JPAModSeqProvider(MailboxPathLocker locker) {
    super(locker);
  }

  @Inject
  public void setMailboxService(MailboxService mailboxService) {
    this.mailboxService = mailboxService;
  }

  @Override
  public long highestModSeq(MailboxSession session, Mailbox mailbox) throws MailboxException {
    JPAId mailboxId = (JPAId) mailbox.getMailboxId();
    Optional<Long> optional = this.mailboxService.findHighestModSeq(mailboxId.getRawId());
    if (!optional.isPresent()) {
      throw new MailboxException("Unable to get highest mod-sequence for mailbox " + mailbox);
    }
    return optional.get();
  }

  @Override
  protected long lockedNextModSeq(MailboxSession session, Mailbox mailbox) throws MailboxException {
    JPAId mailboxId = (JPAId) mailbox.getMailboxId();
    Optional<JamesMailbox> optional = this.mailboxService.findMailboxById(mailboxId.getRawId());
    if (!optional.isPresent()) {
      throw new MailboxException("Unable to save highest mod-sequence for mailbox " + mailbox);
    }
    JamesMailbox m = optional.get();
    long modSeq = m.consumeModSeq();
    this.mailboxService.update(m);
    return modSeq;
  }

  @Override
  public long highestModSeq(MailboxSession session, MailboxId mailboxId) throws MailboxException {
    throw new NotImplementedException("not implemented");
  }
}
