package cn.asany.cardhop.integration.convert;

import cn.asany.cardhop.contacts.bean.ContactGroup;
import cn.asany.organization.core.bean.Department;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EnterpriseContactsConverter {

  @IterableMapping(elementTargetType = ContactGroup.class)
  List<ContactGroup> toContactGroups(List<Department> departments);

  @Mappings({
    @Mapping(source = "name", target = "name"),
    @Mapping(target = "parent", source = "parent", ignore = true),
    @Mapping(target = "children", source = "children", ignore = true)
  })
  ContactGroup toContactGroup(Department department);
}
