package cn.asany.organization.core.convert;

import cn.asany.base.common.bean.Email;
import cn.asany.base.common.bean.Phone;
import cn.asany.base.common.bean.enums.EmailStatus;
import cn.asany.base.common.bean.enums.PhoneNumberStatus;
import cn.asany.organization.core.bean.*;
import cn.asany.organization.core.service.dto.*;
import cn.asany.organization.employee.bean.Employee;
import cn.asany.organization.employee.bean.EmployeeEmail;
import cn.asany.organization.employee.bean.EmployeePhoneNumber;
import cn.asany.organization.relationship.bean.EmployeePosition;
import cn.asany.organization.relationship.bean.Position;
import cn.asany.security.core.bean.enums.Sex;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.mapstruct.*;

/**
 * 组织导入对象转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface NuwaOrganizationConverter {

  /**
   * 转换组织纬度集合
   *
   * @param dimensions 导入对象
   * @return List<OrganizationDimension> 数据库对象
   */
  @IterableMapping(elementTargetType = OrganizationDimension.class)
  List<OrganizationDimension> toDimensions(List<NuwaOrgDimension> dimensions);

  /**
   * 转换组织纬度
   *
   * @param dimension 导入对象
   * @return OrganizationDimension 数据库对象
   */
  @Mappings({
    @Mapping(target = "statuses", ignore = true),
    @Mapping(target = "departments", ignore = true),
  })
  OrganizationDimension toDimension(NuwaOrgDimension dimension);

  /**
   * 转换部门集合
   *
   * @param departments 导入对象
   * @return List<Department> 数据对象
   */
  List<Department> toDepartments(List<NuwaDepartment> departments);

  /**
   * 转换组织纬度
   *
   * @param department 导入对象
   * @return Department 数据对象
   */
  @Mappings({
    @Mapping(source = "type", target = "type", qualifiedByName = "toDepartmentTypeFromName"),
    @Mapping(source = "positions", target = "positions", qualifiedByName = "toPositionsFromString"),
  })
  Department toDepartment(NuwaDepartment department);

  /**
   * 转换职位
   *
   * @param positions 职位
   * @return List<Position>
   */
  @Named("toPositionsFromString")
  default List<Position> toPositionsFromString(List<String> positions) {
    if (positions == null) {
      return new ArrayList<>();
    }
    return positions.stream()
        .map(item -> Position.builder().name(item).build())
        .collect(Collectors.toList());
  }

  /**
   * 通过名称构建 DepartmentType 对象
   *
   * @param name 名称
   * @return DepartmentType
   */
  @Named("toDepartmentTypeFromName")
  default DepartmentType toDepartmentTypeFromName(String name) {
    return DepartmentType.builder().name(name).build();
  }

  /**
   * 转换员工
   *
   * @param employees 员工
   * @return List<Employee>
   */
  @IterableMapping(elementTargetType = Employee.class)
  List<Employee> toEmployees(List<NuwaEmployee> employees);

  /**
   * 转换员工
   *
   * @param employee 员工
   * @return Employee
   */
  @Mappings({
    @Mapping(source = "sex", target = "sex", qualifiedByName = "toSexFromName"),
    @Mapping(source = "identities", target = "identities"),
    @Mapping(source = "phones", target = "phones", qualifiedByName = "toPhonesFromString"),
    @Mapping(source = "emails", target = "emails", qualifiedByName = "toEmailsFromString"),
    @Mapping(source = "avatar", target = "avatar", qualifiedByName = "toAvatarFromString"),
  })
  Employee toEmployee(NuwaEmployee employee);

  /**
   * 转换 员工职位集合
   *
   * @param positions 导入对象
   * @return List<EmployeePosition> 数据库对象
   */
  @IterableMapping(elementTargetType = EmployeePosition.class)
  List<EmployeePosition> toPositions(List<EmployeePosition> positions);

  /**
   * 转换 员工职位
   *
   * @param position 导入对象
   * @return EmployeePosition
   */
  @Mappings({
    @Mapping(
        source = "department",
        target = "department",
        qualifiedByName = "toDepartmentFromName"),
    @Mapping(source = "position", target = "position", qualifiedByName = "toPositionFromName"),
  })
  EmployeePosition toEmployeePosition(NuwaEmployeePosition position);

  @Named("toAvatarFromString")
  default FileObject toAvatarFromString(String avatar) {
    UploadService uploadService = SpringBeanUtils.getBean(UploadService.class);
    //      FileObject object = UploadUtils.partToObject(part);
    //    uploadService.upload();
    return null;
  }

  /**
   * 转换 部门对象， 通过 名称
   *
   * @param name 名称
   * @return Department
   */
  @Named("toDepartmentFromName")
  default Department toDepartmentFromName(String name) {
    return Department.builder().name(name).build();
  }

  /**
   * 转换职位对象， 通过 名称
   *
   * @param name 名称
   * @return Position
   */
  @Named("toPositionFromName")
  default Position toPositionFromName(String name) {
    return Position.builder().name(name).build();
  }
  /**
   * 电话转换
   *
   * @param phones 电话
   * @return List<EmployeePhoneNumber>
   */
  @Named("toPhonesFromString")
  default List<EmployeePhoneNumber> toPhonesFromString(List<String> phones) {
    if (phones == null) {
      return new ArrayList<>();
    }
    return phones.stream()
        .map(
            item ->
                EmployeePhoneNumber.builder()
                    .phone(Phone.builder().number(item).status(PhoneNumberStatus.VERIFIED).build())
                    .build())
        .collect(Collectors.toList());
  }

  /**
   * 邮箱转换
   *
   * @param emails 电话
   * @return List<EmployeeEmail>
   */
  @Named("toEmailsFromString")
  default List<EmployeeEmail> toEmailsFromString(List<String> emails) {
    if (emails == null) {
      return new ArrayList<>();
    }
    return emails.stream()
        .map(
            item ->
                EmployeeEmail.builder()
                    .email(Email.builder().address(item).status(EmailStatus.VERIFIED).build())
                    .build())
        .collect(Collectors.toList());
  }

  /**
   * 转换 员工身份 对象
   *
   * @param status 状态对象
   * @return EmployeeIdentity
   */
  @Mappings({
    @Mapping(
        source = "department",
        target = "department",
        qualifiedByName = "toDepartmentFromName"),
    @Mapping(source = "position", target = "position", qualifiedByName = "toPositionFromName"),
    @Mapping(source = "dimension", target = "dimension", qualifiedByName = "toDimensionFromName"),
    @Mapping(source = "status", target = "status", qualifiedByName = "toEmployeeStatusFromName"),
  })
  EmployeeIdentity toEmployeeIdentity(NuwaEmployeeIdentity status);

  /**
   * 通过名称转换性别枚举
   *
   * @param name 名称
   * @return Sex
   */
  @Named("toSexFromName")
  default Sex toSexFromName(String name) {
    return Sex.of(name);
  }

  /**
   * 通过状态名称称构对象
   *
   * @param name 名称
   * @return EmployeeStatus
   */
  @Named("toEmployeeStatusFromName")
  default EmployeeStatus toEmployeeStatusFromName(String name) {
    return EmployeeStatus.builder().name(name).build();
  }

  /**
   * 通过组织纬度名称构对象
   *
   * @param name 名称
   * @return OrganizationDimension
   */
  @Named("toDimensionFromName")
  default OrganizationDimension toDimensionFromName(String name) {
    return OrganizationDimension.builder().name(name).build();
  }
}
