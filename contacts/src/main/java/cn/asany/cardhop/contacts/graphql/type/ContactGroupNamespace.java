package cn.asany.cardhop.contacts.graphql.type;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ContactGroupNamespace {
  public static final List<ContactGroupNamespace> Cardhop = new ArrayList<>();
  public static final List<ContactGroupNamespace> Enterprise = new ArrayList<>();
  public static final String ENTERPRISE_DEPARTMENT = "department";

  static {
    Cardhop.add(ContactGroupNamespace.builder().id("default").name("默认分组").build());
    Enterprise.add(ContactGroupNamespace.builder().id(ENTERPRISE_DEPARTMENT).name("部门").build());
    Enterprise.add(ContactGroupNamespace.builder().id("jobs").name("岗位").build());
  }

  private String id;
  private String name;
}
