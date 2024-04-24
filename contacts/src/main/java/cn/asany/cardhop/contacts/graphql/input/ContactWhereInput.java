package cn.asany.cardhop.contacts.graphql.input;

import cn.asany.cardhop.contacts.domain.Contact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 联系人过滤
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContactWhereInput extends WhereInput<ContactWhereInput, Contact> {
  private String token;
}
