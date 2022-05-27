package cn.asany.organization.core.service.dto;

import cn.asany.organization.core.domain.DepartmentType;
import cn.asany.organization.core.domain.EmployeeStatus;
import java.util.List;
import lombok.Data;

/**
 * 组织纬度
 *
 * @author limaofeng
 */
@Data
public class NuwaOrgDimension {
  private String code;
  private String name;
  private List<EmployeeStatus> statuses;
  private List<DepartmentType> departmentTypes;
  private List<NuwaDepartment> departments;
}
