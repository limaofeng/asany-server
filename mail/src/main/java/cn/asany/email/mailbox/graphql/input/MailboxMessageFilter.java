package cn.asany.email.mailbox.graphql.input;

import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import cn.asany.email.utils.JamesUtil;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class MailboxMessageFilter extends QueryFilter<MailboxMessageFilter, JamesMailboxMessage> {

  public void setMailbox(String id) {
    if (JamesUtil.isName(id)) {
      this.builder.equal("mailbox.name", JamesUtil.parseMailboxName(id));
    } else {
      this.builder.equal("mailbox.id", Long.valueOf(id));
    }
  }

  public List<PropertyFilter> build(String mailUserId) {
    this.builder.equal("mailbox.user", mailUserId);
    return super.build();
  }
}
