package cn.asany.email.mailbox.domain.toys;

import org.junit.jupiter.api.Test;

class MailboxIdUidKeyTest {

  @Test
  void newObject() {
    MailboxIdUidKey mailboxIdUidKey = new MailboxIdUidKey();
    mailboxIdUidKey.setMailbox(100);
    mailboxIdUidKey.setId(21);

    String key = mailboxIdUidKey.toKey();

    MailboxIdUidKey restore = new MailboxIdUidKey(key);

    System.out.println(restore.getMailbox());
    System.out.println(restore.getId());
  }
}
