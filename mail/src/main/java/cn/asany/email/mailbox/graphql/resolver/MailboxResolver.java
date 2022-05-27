package cn.asany.email.mailbox.graphql.resolver;

import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.mailbox.graphql.type.MailboxCountType;
import cn.asany.email.mailbox.service.MailboxMessageService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class MailboxResolver implements GraphQLResolver<JamesMailbox> {

  private final MailboxMessageService mailboxMessageService;

  public MailboxResolver(MailboxMessageService mailboxMessageService) {
    this.mailboxMessageService = mailboxMessageService;
  }

  public long count(JamesMailbox mailbox, MailboxCountType countType) {
    if (countType == MailboxCountType.UNREAD) {
      return this.mailboxMessageService.countUnseenMessagesInMailbox(mailbox.getId());
    } else {
      return this.mailboxMessageService.countMessagesInMailbox(mailbox.getId());
    }
  }
}
