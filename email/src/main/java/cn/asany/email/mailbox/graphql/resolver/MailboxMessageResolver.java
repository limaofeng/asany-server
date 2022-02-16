package cn.asany.email.mailbox.graphql.resolver;

import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class MailboxMessageResolver implements GraphQLResolver<JamesMailboxMessage> {}
