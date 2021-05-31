package cn.asany.security.core.graphql.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

import java.util.List;

@Data
public class UserFilter {

    private PropertyFilterBuilder builder = new PropertyFilterBuilder();

    @JsonProperty("nickname_like")
    public void setNicknameLike(String value) {
        if (StringUtils.isNotBlank(value)) {
            builder.contains("nickName", value);
        }
    }

    @JsonProperty("organization_eq")
    public void setOrganization(String value) {
        builder.equal("organization.id", value);
    }

    @JsonProperty("department")
    public void setDepartment(Long id) {
//        DepartmentService departmentService = SpringContextUtil.getBeanByType(DepartmentService.class);
//        Department department = departmentService.get(id);
//        List<Department> departments = departmentService.departmentsByPath(department.getPath());
//        builder.and(new GrantPermissionSpecification("DEPARTMENT_ADMIN", SecurityType.user, departments.stream().map(item -> item.getId().toString()).collect(Collectors.toList())));
    }

    public List<PropertyFilter> build() {
        return this.builder.build();
    }

    @JsonProperty("username_like")
    public void setUsernameLike(String value) {
        if (StringUtils.isNotBlank(value)) {
            builder.contains("username", value);
        }
    }

    @JsonProperty("deptid_eq")
    public void setDeptId(String value) {
        if (StringUtils.isNotBlank(value)) {
            builder.equal("employee.employeePositions.department.id", value);
        }
    }

    @JsonProperty("groupid_eq")
    public void setGroupId(String value) {
        if (StringUtils.isNotBlank(value)) {
            builder.equal("employee.groups.id", value);
        }
    }

    /**
     * 按所属部门名称查询，包括其子部门 add by gy
     * @param value 部门名称
     */
    @JsonProperty("deptname_like")
    public void setDeptName(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            DepartmentService departmentService = SpringContextUtil.getBeanByType(DepartmentService.class);
//            List<PropertyFilter> filters = new DepartmentFilter().getBuilder()
//                    .contains("name", value)
//                    .build();
//            List<Department> departments = departmentService.findAll(filters);
//            if(!CollectionUtils.isEmpty(departments)) {
//                Set<Long> deptIds = getDepartmentIds(departments);
//                builder.in("employee.employeePositions.department.id", deptIds.toArray(new Long[0]));
//            }
//        }
    }

//    private Set<Long> getDepartmentIds(List<Department> departments) {
//        Set<Long> deptIds = new HashSet<>();
//        for(Department department : departments) {
//            List<Long> ids = getDepartmentChildIds(department);
//            deptIds.addAll(ids);
//        }
//        return deptIds;
//    }

//    private List<Long> getDepartmentChildIds(Department department) {
//        List<Department> childDepts = department.getChildren();
//        if(CollectionUtils.isEmpty(childDepts)) {
//            return Arrays.asList(department.getId());
//        }
//        List<Long> ids = new ArrayList<>();
//        for(Department child : childDepts) {
//            List<Long> childIds = getDepartmentChildIds(child);
//            ids.addAll(childIds);
//        }
//        return ids;
//    }
}
