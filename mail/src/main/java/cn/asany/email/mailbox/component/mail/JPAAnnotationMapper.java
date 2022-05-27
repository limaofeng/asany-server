package cn.asany.email.mailbox.component.mail;

import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.component.JPATransactionalMapper;
import cn.asany.email.mailbox.domain.JPAMailboxAnnotationId;
import cn.asany.email.mailbox.domain.JamesMailboxAnnotation;
import cn.asany.email.mailbox.service.MailboxAnnotationService;
import com.github.steveash.guavate.Guavate;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import org.apache.james.mailbox.model.MailboxAnnotation;
import org.apache.james.mailbox.model.MailboxAnnotationKey;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.store.mail.AnnotationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAAnnotationMapper extends JPATransactionalMapper implements AnnotationMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(JPAAnnotationMapper.class);

  public static final Function<JamesMailboxAnnotation, MailboxAnnotation> READ_ROW =
      input ->
          MailboxAnnotation.newInstance(new MailboxAnnotationKey(input.getKey()), input.getValue());

  private MailboxAnnotationService mailboxAnnotationService;

  public JPAAnnotationMapper(EntityManagerFactory entityManagerFactory) {
    super(entityManagerFactory);
  }

  @Override
  public List<MailboxAnnotation> getAllAnnotations(MailboxId mailboxId) {
    JPAId jpaId = (JPAId) mailboxId;
    return this.mailboxAnnotationService.retrieveAllAnnotations(jpaId.getRawId()).stream()
        .map(READ_ROW)
        .collect(Guavate.toImmutableList());
  }

  @Override
  public List<MailboxAnnotation> getAnnotationsByKeys(
      MailboxId mailboxId, Set<MailboxAnnotationKey> keys) {
    try {
      final JPAId jpaId = (JPAId) mailboxId;
      return ImmutableList.copyOf(
          Iterables.transform(
              keys,
              input ->
                  READ_ROW.apply(
                      this.mailboxAnnotationService.retrieveByKey(
                          jpaId.getRawId(), input.asString()))));
    } catch (NoResultException e) {
      return ImmutableList.of();
    }
  }

  @Override
  public List<MailboxAnnotation> getAnnotationsByKeysWithOneDepth(
      MailboxId mailboxId, Set<MailboxAnnotationKey> keys) {
    return getFilteredLikes(
        (JPAId) mailboxId, keys, key -> annotation -> key.isParentOrIsEqual(annotation.getKey()));
  }

  @Override
  public List<MailboxAnnotation> getAnnotationsByKeysWithAllDepth(
      MailboxId mailboxId, Set<MailboxAnnotationKey> keys) {
    return getFilteredLikes(
        (JPAId) mailboxId, keys, key -> annotation -> key.isAncestorOrIsEqual(annotation.getKey()));
  }

  private List<MailboxAnnotation> getFilteredLikes(
      final JPAId jpaId,
      Set<MailboxAnnotationKey> keys,
      final Function<MailboxAnnotationKey, Predicate<MailboxAnnotation>> predicateFunction) {
    try {
      return keys.stream()
          .flatMap(
              key ->
                  this.mailboxAnnotationService
                      .retrieveByKeyLike(jpaId.getRawId(), key.asString() + '%').stream()
                      .map(READ_ROW)
                      .filter(predicateFunction.apply(key)))
          .collect(Guavate.toImmutableList());
    } catch (NoResultException e) {
      return ImmutableList.of();
    }
  }

  @Override
  public void deleteAnnotation(MailboxId mailboxId, MailboxAnnotationKey key) {
    try {
      JPAId jpaId = (JPAId) mailboxId;
      this.mailboxAnnotationService.delete(
          new JPAMailboxAnnotationId(jpaId.getRawId(), key.asString()));
    } catch (NoResultException e) {
      LOGGER.debug(
          "Mailbox annotation not found for ID {} and key {}",
          mailboxId.serialize(),
          key.asString());
    } catch (PersistenceException pe) {
      throw new RuntimeException(pe);
    }
  }

  @Override
  public void insertAnnotation(MailboxId mailboxId, MailboxAnnotation mailboxAnnotation) {
    Preconditions.checkArgument(!mailboxAnnotation.isNil());
    JPAId jpaId = (JPAId) mailboxId;
    if (getAnnotationsByKeys(mailboxId, ImmutableSet.of(mailboxAnnotation.getKey())).isEmpty()) {
      this.mailboxAnnotationService.save(
          new JamesMailboxAnnotation(
              jpaId.getRawId(),
              mailboxAnnotation.getKey().asString(),
              mailboxAnnotation.getValue().orElse(null)));
    } else {
      JamesMailboxAnnotation old =
          this.mailboxAnnotationService.getById(
              new JPAMailboxAnnotationId(jpaId.getRawId(), mailboxAnnotation.getKey().asString()));
      old.setValue(mailboxAnnotation.getValue().orElse(null));
      this.mailboxAnnotationService.update(old);
    }
  }

  @Override
  public boolean exist(MailboxId mailboxId, MailboxAnnotation mailboxAnnotation) {
    JPAId jpaId = (JPAId) mailboxId;
    Optional<JamesMailboxAnnotation> row =
        this.mailboxAnnotationService.findById(
            new JPAMailboxAnnotationId(jpaId.getRawId(), mailboxAnnotation.getKey().asString()));
    return row.isPresent();
  }

  @Override
  public int countAnnotations(MailboxId mailboxId) {
    try {
      JPAId jpaId = (JPAId) mailboxId;
      return (int) this.mailboxAnnotationService.countAnnotationsInMailbox(jpaId.getRawId());
    } catch (PersistenceException pe) {
      throw new RuntimeException(pe);
    }
  }
}
