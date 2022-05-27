package cn.asany.cardhop.contacts.graphql.type;

import cn.asany.cardhop.contacts.domain.Contact;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class ContactConnection extends BaseConnection<ContactConnection.ContactEdge, Contact> {

  private List<ContactEdge> edges;

  @Data
  public static class ContactEdge implements Edge<Contact> {
    private String cursor;
    private Contact node;
  }
}
