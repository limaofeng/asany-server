package cn.asany.email.mailbox.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.mail.Flags;
import javax.mail.internet.SharedInputStream;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.james.mailbox.MessageUid;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.store.mail.model.MailboxMessage;
import org.apache.james.mailbox.store.mail.model.impl.PropertyBuilder;

@Setter
@Getter
@Entity(name = "MailboxMessage")
@Table(name = "JAMES_MAIL")
public class JamesMailboxMessage extends AbstractJPAMailboxMessage {

  private static final byte[] EMPTY_BODY = new byte[] {};

  @Basic(optional = false, fetch = FetchType.LAZY)
  @Column(name = "MAIL_BYTES", length = 1048576000, nullable = false)
  @Lob
  private byte[] body;

  @Basic(optional = false, fetch = FetchType.LAZY)
  @Column(name = "HEADER_BYTES", length = 10485760, nullable = false)
  @Lob
  private byte[] header;

  public JamesMailboxMessage(
      JamesMailbox mailbox, MessageUid uid, long modSeq, MailboxMessage message)
      throws MailboxException {
    super(mailbox, uid, modSeq, message);
    try {
      this.body = IOUtils.toByteArray(message.getBodyContent());
      this.header = IOUtils.toByteArray(message.getHeaderContent());
    } catch (IOException e) {
      throw new MailboxException("Unable to parse message", e);
    }
  }

  public JamesMailboxMessage(
      JamesMailbox mailbox,
      Date internalDate,
      int size,
      Flags flags,
      SharedInputStream content,
      int bodyStartOctet,
      PropertyBuilder propertyBuilder)
      throws MailboxException {
    super(mailbox, internalDate, flags, size, bodyStartOctet, propertyBuilder);
    try {
      int headerEnd = bodyStartOctet;
      if (headerEnd < 0) {
        headerEnd = 0;
      }
      this.header = IOUtils.toByteArray(content.newStream(0, headerEnd));
      this.body = IOUtils.toByteArray(content.newStream(getBodyStartOctet(), -1));

    } catch (IOException e) {
      throw new MailboxException("Unable to parse message", e);
    }
  }

  @Override
  public InputStream getBodyContent() throws IOException {
    if (body == null) {
      return new ByteArrayInputStream(EMPTY_BODY);
    }
    return new ByteArrayInputStream(body);
  }

  @Override
  public InputStream getHeaderContent() throws IOException {
    return new ByteArrayInputStream(header);
  }
}
