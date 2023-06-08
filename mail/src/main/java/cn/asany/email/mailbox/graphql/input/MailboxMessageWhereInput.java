package cn.asany.email.mailbox.graphql.input;

import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import cn.asany.email.utils.JamesUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 邮件查询过滤
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MailboxMessageWhereInput
    extends WhereInput<MailboxMessageWhereInput, JamesMailboxMessage> {

  public void setMailbox(String id) {
    if (JamesUtil.isName(id)) {
      this.filter.equal("mailbox.name", JamesUtil.parseMailboxName(id));
    } else {
      this.filter.equal("mailbox.id", Long.valueOf(id));
    }
  }
}
