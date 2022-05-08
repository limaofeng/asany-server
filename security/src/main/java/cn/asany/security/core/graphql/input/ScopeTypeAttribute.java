package cn.asany.security.core.graphql.input;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ScopeTypeAttribute {

  //    public Map<String, String> getQueryTypeToAttributeMap(SecurityExpressType type) {
  //        switch (type) {
  //            case employee:
  //                Map<String, String> employeeAttribute = getEmployeeAttribute();
  //                return employeeAttribute;
  //            case organization:
  //                Map<String, String> organizationAttribute = getOrganizationAttribute();
  //                return organizationAttribute;
  //            case department:
  //                Map<String, String> departmentAttribute = getDepartmentAttribute();
  //                return departmentAttribute;
  //            case position:
  //                Map<String, String> positionAttribute = getPositionAttribute();
  //                return positionAttribute;
  //            case employeeGroup:
  //                Map<String, String> employeeGroupAttribute = getEmployeeGroupAttribute();
  //                return employeeGroupAttribute;
  //            case role:
  //                Map<String, String> roleAttribute = getRoleAttribute();
  //                return roleAttribute;
  //            case job:
  //                Map<String, String> jobAttribute = getJobAttribute();
  //                return jobAttribute;
  //
  //        }
  //        return null;
  //    }

  public Map<String, String> getEmployeeAttribute() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "主键");
    map.put("name", "名称");
    map.put("status", "状态");
    map.put("jobNumber", "工号");
    map.put("birthday", "生日");
    map.put("sex", "性别");
    return map;
  }

  public Map<String, String> getDepartmentAttribute() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "主键");
    map.put("sn", "简写");
    map.put("name", "名称");
    map.put("enabled", "是否启用");
    map.put("type", "部门类型");
    map.put("organization", "所属组织");
    return map;
  }

  public Map<String, String> getOrganizationAttribute() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "主键");
    map.put("name", "名称");
    return map;
  }

  public Map<String, String> getJobAttribute() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "主键");
    map.put("name", "名称");
    map.put("organization", "所属组织");
    return map;
  }

  public Map<String, String> getRoleAttribute() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "主键");
    map.put("name", "名称");
    map.put("enabled", "是否启用");
    map.put("scope", "角色范围");
    map.put("organization", "所属组织");
    map.put("roleType", "分类");
    return map;
  }

  public Map<String, String> getEmployeeGroupAttribute() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "主键");
    map.put("name", "名称");
    map.put("enabled", "是否启用");
    map.put("organization", "所属组织");
    return map;
  }

  public Map<String, String> getPositionAttribute() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "主键");
    map.put("name", "名称");
    map.put("job", "职务");
    map.put("department", "所属部门");
    map.put("organization", "所属组织");
    return map;
  }
}
