package cn.asany.cardhop.contacts.graphql.input;

import cn.asany.cardhop.contacts.domain.Contact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactFilter extends QueryFilter<ContactFilter, Contact> {
  private String token;
}
