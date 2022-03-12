package cn.asany.email.mailbox.graphql.input;

import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailboxMessageCreateInput {
  private List<String> to;
  private List<String> cc;
  private List<String> bcc;
  private String subject;
  private String mimeType;
  private String body;
  private List<FileObject> attachments;
}
