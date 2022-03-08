package cn.asany.email.utils;

import cn.asany.email.mailbox.bean.JamesMailboxMessage;

public class SentMailContext {
  private JamesMailboxMessage message;

  private static final ThreadLocal THREAD_LOCAL = ThreadLocal.withInitial(SentMailContext::new);

  public SentMailContext() {}

  public SentMailContext(JamesMailboxMessage message) {
    this.message = message;
  }

  public static void create(JamesMailboxMessage message) {
    THREAD_LOCAL.set(new SentMailContext(message));
  }

  public static SentMailContext get() {
    return (SentMailContext) THREAD_LOCAL.get();
  }

  public JamesMailboxMessage getMessage() {
    return message;
  }
}
