package cn.asany.email.mailbox.graphql.type;

import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.address.MailboxList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailboxMessageResult {

  private Message message;
  private JamesMailboxMessage mailboxMessage;

  public MailboxMessageResult(JamesMailboxMessage mailboxMessage) {
    this.mailboxMessage = mailboxMessage;
  }

  public long getId() {
    return this.mailboxMessage.getId();
  }

  public String getSubject() {
    return this.message.getSubject();
  }

  public Date getDate() {
    return this.message.getDate();
  }

  public Mailbox getSender() {
    return this.message.getSender();
  }

  public MailboxList getFrom() {
    return this.message.getFrom();
  }

  public AddressList getTo() {
    return this.message.getTo();
  }

  public AddressList getCc() {
    return this.message.getCc();
  }

  public AddressList getBcc() {
    return this.message.getBcc();
  }

  public AddressList getReplyTo() {
    return this.message.getReplyTo();
  }

  public String getMimeType() {
    return this.message.getMimeType();
  }

  public boolean isBase64Encoding() {
    return "base64".equals(this.message.getContentTransferEncoding());
  }

  public Body getBody() {
    return this.message.getBody();
  }

  public JamesMailbox getMailbox() {
    return this.mailboxMessage.getMailbox();
  }

  public boolean isSeen() {
    return this.mailboxMessage.isSeen();
  }

  public boolean isAnswered() {
    return this.mailboxMessage.isAnswered();
  }

  public boolean isDeleted() {
    return this.mailboxMessage.isDeleted();
  }

  public boolean isDraft() {
    return this.mailboxMessage.isDraft();
  }

  public boolean isFlagged() {
    return this.mailboxMessage.isFlagged();
  }

  public boolean isRecent() {
    return this.mailboxMessage.isRecent();
  }
}
