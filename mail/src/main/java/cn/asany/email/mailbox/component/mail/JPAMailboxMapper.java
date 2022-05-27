package cn.asany.email.mailbox.component.mail;

import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.component.JPATransactionalMapper;
import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.mailbox.service.MailboxService;
import com.github.steveash.guavate.Guavate;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import javax.persistence.*;
import org.apache.james.mailbox.acl.ACLDiff;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.exception.MailboxExistsException;
import org.apache.james.mailbox.exception.MailboxNotFoundException;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.model.MailboxACL;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.model.MailboxPath;
import org.apache.james.mailbox.store.mail.MailboxMapper;

public class JPAMailboxMapper extends JPATransactionalMapper implements MailboxMapper {

  private static final char SQL_WILDCARD_CHAR = '%';
  private String lastMailboxName;

  private final MailboxService mailboxService;

  public JPAMailboxMapper(
      EntityManagerFactory entityManagerFactory, MailboxService mailboxService) {
    super(entityManagerFactory);
    this.mailboxService = mailboxService;
  }

  /**
   * Commit the transaction. If the commit fails due a conflict in a unique key constraint a {@link
   * MailboxExistsException} will get thrown
   */
  @Override
  protected void commit() throws MailboxException {
    try {
      getEntityManager().getTransaction().commit();
    } catch (PersistenceException e) {
      if (e instanceof EntityExistsException) {
        throw new MailboxExistsException(lastMailboxName);
      }
      if (e instanceof RollbackException) {
        Throwable t = e.getCause();
        if (t instanceof EntityExistsException) {
          throw new MailboxExistsException(lastMailboxName);
        }
      }
      throw new MailboxException("Commit of transaction failed", e);
    }
  }

  @Override
  public MailboxId save(Mailbox mailbox) throws MailboxException {
    try {
      if (isPathAlreadyUsedByAnotherMailbox(mailbox)) {
        throw new MailboxExistsException(mailbox.getName());
      }

      this.lastMailboxName = mailbox.getName();
      JamesMailbox persistedMailbox = jpaMailbox(mailbox);

      this.mailboxService.save(persistedMailbox);

      mailbox.setMailboxId(persistedMailbox.toMailbox().getMailboxId());
      return persistedMailbox.getMailboxId();
    } catch (PersistenceException e) {
      throw new MailboxException("Save of mailbox " + mailbox.getName() + " failed", e);
    }
  }

  private JamesMailbox jpaMailbox(Mailbox mailbox) {
    if (mailbox.getMailboxId() == null) {
      return JamesMailbox.from(mailbox);
    }
    try {
      JamesMailbox result = loadJpaMailbox(mailbox.getMailboxId());
      result.setNamespace(mailbox.getNamespace());
      result.setUser(mailbox.getUser());
      result.setName(mailbox.getName());
      return result;
    } catch (MailboxNotFoundException e) {
      return JamesMailbox.from(mailbox);
    }
  }

  private boolean isPathAlreadyUsedByAnotherMailbox(Mailbox mailbox) throws MailboxException {
    try {
      Mailbox storedMailbox = findMailboxByPath(mailbox.generateAssociatedPath());
      return !Objects.equal(storedMailbox.getMailboxId(), mailbox.getMailboxId());
    } catch (MailboxNotFoundException e) {
      return false;
    }
  }

  @Override
  public Mailbox findMailboxByPath(MailboxPath mailboxPath)
      throws MailboxException, MailboxNotFoundException {
    try {
      if (mailboxPath.getUser() == null) {
        Optional<JamesMailbox> optional =
            this.mailboxService.findMailboxByName(
                mailboxPath.getName(), mailboxPath.getNamespace());

        return optional
            .map(JamesMailbox::toMailbox)
            .orElseThrow(() -> new MailboxNotFoundException(mailboxPath));
      } else {

        Optional<JamesMailbox> optional =
            this.mailboxService.findMailboxByNameWithUser(
                mailboxPath.getName(), mailboxPath.getUser(), mailboxPath.getNamespace());
        return optional
            .map(JamesMailbox::toMailbox)
            .orElseThrow(() -> new MailboxNotFoundException(mailboxPath));
      }
    } catch (PersistenceException e) {
      throw new MailboxException("Search of mailbox " + mailboxPath + " failed", e);
    }
  }

  @Override
  public Mailbox findMailboxById(MailboxId id) throws MailboxException, MailboxNotFoundException {

    try {
      return loadJpaMailbox(id).toMailbox();
    } catch (PersistenceException e) {
      throw new MailboxException("Search of mailbox " + id.serialize() + " failed", e);
    }
  }

  private JamesMailbox loadJpaMailbox(MailboxId id) throws MailboxNotFoundException {
    JPAId mailboxId = (JPAId) id;
    Optional<JamesMailbox> optional = this.mailboxService.findMailboxById(mailboxId.getRawId());
    return optional.orElseThrow(() -> new MailboxNotFoundException(mailboxId));
  }

  public boolean exists(MailboxId id) throws MailboxException, MailboxNotFoundException {
    try {
      loadJpaMailbox(id);
      return true;
    } catch (MailboxNotFoundException e) {
      return false;
    }
  }

  @Override
  public void delete(Mailbox mailbox) throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      this.mailboxService.delete(mailboxId.getRawId());
    } catch (PersistenceException e) {
      throw new MailboxException("Delete of mailbox " + mailbox + " failed", e);
    }
  }

  @Override
  public List<Mailbox> findMailboxWithPathLike(MailboxPath path) throws MailboxException {
    try {
      return findMailboxWithPathLikeTypedQuery(path).stream()
          .map(JamesMailbox::toMailbox)
          .collect(Guavate.toImmutableList());
    } catch (PersistenceException e) {
      throw new MailboxException("Search of mailbox " + path + " failed", e);
    }
  }

  private List<JamesMailbox> findMailboxWithPathLikeTypedQuery(MailboxPath path) {
    if (path.getUser() == null) {
      return this.mailboxService.findMailboxWithNameLike(path.getName(), path.getNamespace());
    } else {
      return this.mailboxService.findMailboxWithNameLikeWithUser(
          path.getName(), path.getUser(), path.getNamespace());
    }
  }

  @Override
  public boolean hasChildren(Mailbox mailbox, char delimiter) {
    final String name = mailbox.getName() + delimiter + SQL_WILDCARD_CHAR;
    final Long numberOfChildMailboxes;
    if (mailbox.getUser() == null) {

      numberOfChildMailboxes =
          this.mailboxService.countMailboxesWithNameLike(name, mailbox.getNamespace());
    } else {
      numberOfChildMailboxes =
          this.mailboxService.countMailboxesWithNameLikeWithUser(
              name, mailbox.getUser(), mailbox.getNamespace());
    }
    return numberOfChildMailboxes != null && numberOfChildMailboxes > 0;
  }

  @Override
  public List<Mailbox> list() throws MailboxException {
    return this.mailboxService.listMailboxes().stream()
        .map(JamesMailbox::toMailbox)
        .collect(Guavate.toImmutableList());
  }

  @Override
  public ACLDiff updateACL(Mailbox mailbox, MailboxACL.ACLCommand mailboxACLCommand)
      throws MailboxException {
    MailboxACL oldACL = mailbox.getACL();
    MailboxACL newACL = mailbox.getACL().apply(mailboxACLCommand);
    mailbox.setACL(newACL);
    return ACLDiff.computeDiff(oldACL, newACL);
  }

  @Override
  public ACLDiff setACL(Mailbox mailbox, MailboxACL mailboxACL) throws MailboxException {
    MailboxACL oldMailboxAcl = mailbox.getACL();
    mailbox.setACL(mailboxACL);
    return ACLDiff.computeDiff(oldMailboxAcl, mailboxACL);
  }

  @Override
  public List<Mailbox> findNonPersonalMailboxes(String userName, MailboxACL.Right right)
      throws MailboxException {
    return ImmutableList.of();
  }
}
