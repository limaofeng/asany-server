// package cn.asany.security.core.graphql.inputs;
//
//
// import cn.asany.security.core.graphql.enums.ResultTypeScope;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
//
// @Component
// public class AllCombinations {
//
//    @Autowired
//    private ScopeTypeAttribute typeAttribute;
//
//
//    public boolean isRight(SecurityExpressType type, String funName, SecurityExpressType
// targettype) {
//        if (type == null) {
//            return true;
//        }
//        switch (type) {
//            case employee:
//                List<ScopeTypeResult> employeeCombination = getEmployeeCombination(type);
//                return getScopeRight(employeeCombination, funName, targettype);
//            case department:
//                List<ScopeTypeResult> departmentCombination = getDepartmentCombination(type);
//                return getScopeRight(departmentCombination, funName, targettype);
//            case organization:
//                List<ScopeTypeResult> organizationCombination = getOrganizationCombination(type);
//                return getScopeRight(organizationCombination, funName, targettype);
//            case role:
//                List<ScopeTypeResult> roleCombination = getRoleCombination(type);
//                return getScopeRight(roleCombination, funName, targettype);
//            case position:
//                List<ScopeTypeResult> positionCombination = getPositionCombination(type);
//                return getScopeRight(positionCombination, funName, targettype);
//            case job:
//                List<ScopeTypeResult> jobCombination = getJobCombination(type);
//                return getScopeRight(jobCombination, funName, targettype);
//            case employeeGroup:
//                List<ScopeTypeResult> employeeGroupCombination =
// getEmployeeGroupCombination(type);
//                return getScopeRight(employeeGroupCombination, funName, targettype);
//        }
//        return false;
//    }
//
//    public boolean getScopeRight(List<ScopeTypeResult> data, String funName, SecurityExpressType
// targettype){
//        if (funName == null && targettype == null) {
//            return false;
//        }
//        // 判断函数是否存在
//        List<ScopeTypeResult> collectfun = data.stream().filter(item ->
// item.getKey().equals(funName)).collect(Collectors.toList());
//        List<SecurityTypeData> collect = new ArrayList<>();
//        for (ScopeTypeResult scopeTypeResult: data){
//            // 可以转换的类型
//            List<SecurityTypeData> targetTypes = scopeTypeResult.getTargetTypes();
//            collect = targetTypes.stream().filter(item ->
// item.getKey().equals(targettype)).collect(Collectors.toList());
//
//        }
//        if ((collectfun != null && collectfun .size() >0) && (collect != null && collect .size()
// >0)){
//            return true;
//        }
//        return false;
//    }
//
//    public List<ScopeTypeResult> getEmployeeCombination(SecurityExpressType type) {
//        List<ScopeTypeResult> list = new ArrayList<>();
//        if (SecurityExpressType.employee.equals(type)) {
//            // 员工对应的函数有
//            List<SecurityTypeData> targetTypes = getTargetEmployeeTypes();
//            // parent
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parent.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parent.toString())).targetTypes(targetTypes).build());
//            // parents
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parents.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parents.toString())).targetTypes(targetTypes).build());
//            // closest
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.closest.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.closest.toString())).targetTypes(targetTypes).build());
//            // 对应的属性
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.attribute).title("可选择属性").targetTypes(queryAllAttribute(SecurityExpressType.employee)).build());
//        }
//        return list;
//    }
//
//
//
//    public List<SecurityTypeData> getTargetEmployeeTypes() {
//        List<SecurityTypeData> targetTypes = new ArrayList<>();
//
// targetTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employeeGroup.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employeeGroup.toString())).build());
//
// targetTypes.add(SecurityTypeData.builder().key(SecurityExpressType.role.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.role.toString())).build());
//
// targetTypes.add(SecurityTypeData.builder().key(SecurityExpressType.position.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.position.toString())).build());
//
// targetTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetTypes.add(SecurityTypeData.builder().key(SecurityExpressType.job.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.job.toString())).build());
//
// targetTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//        return targetTypes;
//    }
//
//    public List<ScopeTypeResult> getDepartmentCombination(SecurityExpressType type) {
//        List<ScopeTypeResult> list = new ArrayList<>();
//        if (SecurityExpressType.department.equals(type)) {
//            // 员工对应的函数有
//            List<SecurityTypeData> targetParentTypes = getTargetDepartmentParentTypes();
//            // parent
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parent.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parent.toString())).targetTypes(targetParentTypes).build());
//            // parents
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parents.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parents.toString())).targetTypes(targetParentTypes).build());
//            // closest
//            List<SecurityTypeData> targetDepartmentTypes = getTargetDepartmentTypes();
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.closest.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.closest.toString())).targetTypes(targetDepartmentTypes).build());
//            // children
//            List<SecurityTypeData> targetFindTypes = getTargetDepartmentFindTypes();
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.children.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.children.toString())).targetTypes(targetFindTypes).build());
//            // find
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.find.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.find.toString())).targetTypes(targetFindTypes).build());
//            // 对应的属性
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.attribute).title("可选择属性").targetTypes(queryAllAttribute(SecurityExpressType.department)).build());
//
//        }
//        return list;
//    }
//
//    public List<SecurityTypeData> getTargetDepartmentParentTypes() {
//        List<SecurityTypeData> targetParentTypes = new ArrayList<>();
//
// targetParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//        return targetParentTypes;
//    }
//
//    public List<SecurityTypeData> getTargetDepartmentTypes() {
//        List<SecurityTypeData> targetParentTypes = new ArrayList<>();
//
// targetParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//
// targetParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// targetParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.role.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.role.toString())).build());
//
// targetParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.position.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.position.toString())).build());
//        return targetParentTypes;
//    }
//
//    public List<SecurityTypeData> getTargetDepartmentFindTypes() {
//        List<SecurityTypeData> targetFindTypes = new ArrayList<>();
//
// targetFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//
// targetFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// targetFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.position.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.position.toString())).build());
//
// targetFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.role.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.role.toString())).build());
//        return targetFindTypes;
//    }
//
//    public List<ScopeTypeResult> getOrganizationCombination(SecurityExpressType type) {
//        List<ScopeTypeResult> list = new ArrayList<>();
//        if (SecurityExpressType.organization.equals(type)) {
//            // closest
//            List<SecurityTypeData> targetOrganizationFindTypes = getTargetOrganizationFindTypes();
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.closest.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.closest.toString())).targetTypes(targetOrganizationFindTypes).build());
//            // children
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.children.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.children.toString())).targetTypes(targetOrganizationFindTypes).build());
//            // find
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.find.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.find.toString())).targetTypes(targetOrganizationFindTypes).build());
//            // 对应的属性
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.attribute).title("可选择属性").targetTypes(queryAllAttribute(SecurityExpressType.organization)).build());
//
//        }
//        return list;
//    }
//
//    public List<SecurityTypeData> getTargetOrganizationFindTypes() {
//        List<SecurityTypeData> targetOrganizationTypes = new ArrayList<>();
//
// targetOrganizationTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//
// targetOrganizationTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employeeGroup.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employeeGroup.toString())).build());
//
// targetOrganizationTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetOrganizationTypes.add(SecurityTypeData.builder().key(SecurityExpressType.role.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.role.toString())).build());
//
// targetOrganizationTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// targetOrganizationTypes.add(SecurityTypeData.builder().key(SecurityExpressType.job.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.job.toString())).build());
//        return targetOrganizationTypes;
//    }
//
//    public List<ScopeTypeResult> getJobCombination(SecurityExpressType type) {
//        List<ScopeTypeResult> list = new ArrayList<>();
//        if (SecurityExpressType.job.equals(type)) {
//            // parent
//            List<SecurityTypeData> targetJobParentTypes = new ArrayList<>();
//
// targetJobParentTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parent.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parent.toString())).targetTypes(targetJobParentTypes).build());
//            // parents
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parents.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parents.toString())).targetTypes(targetJobParentTypes).build());
//            // closest
//            List<SecurityTypeData> targetJobTypes = new ArrayList<>();
//
// targetJobTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetJobTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employeeGroup.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.closest.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.closest.toString())).targetTypes(targetJobTypes).build());
//            // children
//            List<SecurityTypeData> targetJobFindTypes = new ArrayList<>();
//
// targetJobFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.children.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.children.toString())).targetTypes(targetJobFindTypes).build());
//            // find
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.find.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.find.toString())).targetTypes(targetJobFindTypes).build());
//            // 对应的属性
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.attribute).title("可选择属性").targetTypes(queryAllAttribute(SecurityExpressType.job)).build());
//
//        }
//        return list;
//    }
//
//    public List<ScopeTypeResult> getPositionCombination(SecurityExpressType type) {
//        List<ScopeTypeResult> list = new ArrayList<>();
//        if (SecurityExpressType.position.equals(type)) {
//            // parent
//            List<SecurityTypeData> targetPositionTypes = new ArrayList<>();
//
// targetPositionTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetPositionTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parent.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parent.toString())).targetTypes(targetPositionTypes).build());
//            // parents
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parents.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parents.toString())).targetTypes(targetPositionTypes).build());
//            // closest
//            List<SecurityTypeData> targetPositionClosestTypes = new ArrayList<>();
//
// targetPositionClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetPositionClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//
// targetPositionClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.closest.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.closest.toString())).targetTypes(targetPositionClosestTypes).build());
//            // children
//            List<SecurityTypeData> targetPositionFindTypes = new ArrayList<>();
//
// targetPositionFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.children.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.children.toString())).targetTypes(targetPositionFindTypes).build());
//            // find
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.find.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.find.toString())).targetTypes(targetPositionFindTypes).build());
//            // 对应的属性
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.attribute).title("可选择属性").targetTypes(queryAllAttribute(SecurityExpressType.position)).build());
//
//        }
//        return list;
//    }
//
//    public List<ScopeTypeResult> getEmployeeGroupCombination(SecurityExpressType type){
//        List<ScopeTypeResult> list = new ArrayList<>();
//        if (SecurityExpressType.employeeGroup.equals(type)) {
//            // parent
//            List<SecurityTypeData> targetEmployeeGroupTypes = new ArrayList<>();
//
// targetEmployeeGroupTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parent.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parent.toString())).targetTypes(targetEmployeeGroupTypes).build());
//            // parents
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parents.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parents.toString())).targetTypes(targetEmployeeGroupTypes).build());
//            // closest
//            List<SecurityTypeData> targetEmployeeGroupClosestTypes = new ArrayList<>();
//
// targetEmployeeGroupClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetEmployeeGroupClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.closest.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.closest.toString())).targetTypes(targetEmployeeGroupClosestTypes).build());
//            // children
//            List<SecurityTypeData> targetEmployeeGroupFindTypes = new ArrayList<>();
//
// targetEmployeeGroupFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.children.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.children.toString())).targetTypes(targetEmployeeGroupFindTypes).build());
//            // find
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.find.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.find.toString())).targetTypes(targetEmployeeGroupFindTypes).build());
//            // 对应的属性
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.attribute).title("可选择属性").targetTypes(queryAllAttribute(SecurityExpressType.employeeGroup)).build());
//
//        }
//        return list;
//    }
//
//    public List<ScopeTypeResult> getRoleCombination(SecurityExpressType type){
//        List<ScopeTypeResult> list = new ArrayList<>();
//        if (SecurityExpressType.role.equals(type)) {
//            // parent
//            List<SecurityTypeData> targetRoleTypes = new ArrayList<>();
//
// targetRoleTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetRoleTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parent.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parent.toString())).targetTypes(targetRoleTypes).build());
//            // parents
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.parents.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.parents.toString())).targetTypes(targetRoleTypes).build());
//            // closest
//            List<SecurityTypeData> targetRoleClosestTypes = new ArrayList<>();
//
// targetRoleClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.organization.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.organization.toString())).build());
//
// targetRoleClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.department.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.department.toString())).build());
//
// targetRoleClosestTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.closest.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.closest.toString())).targetTypes(targetRoleClosestTypes).build());
//            // children
//            List<SecurityTypeData> targetRoleFindTypes = new ArrayList<>();
//
// targetRoleFindTypes.add(SecurityTypeData.builder().key(SecurityExpressType.employee.name()).value(SecurityExpressType.getSecurityTypeValue(SecurityExpressType.employee.toString())).build());
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.children.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.children.toString())).targetTypes(targetRoleFindTypes).build());
//            // find
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.function).key(ExpressionTypeEnum.find.toString()).title(ExpressionTypeEnum.getExpressionTypeValue(ExpressionTypeEnum.find.toString())).targetTypes(targetRoleFindTypes).build());
//            // 对应的属性
//
// list.add(ScopeTypeResult.builder().type(ResultTypeScope.attribute).title("可选择属性").targetTypes(queryAllAttribute(SecurityExpressType.role)).build());
//
//        }
//        return list;
//    }
//
//    /**
//     * 根据目标类型查询对应的属性
//     * @param type
//     * @return
//     */
//    public List<SecurityTypeData> queryAllAttribute(SecurityExpressType type) {
//
//        Map<String, String> queryTypeToAttributeMap =
// typeAttribute.getQueryTypeToAttributeMap(type);
//        List<SecurityTypeData> list = new ArrayList<>();
//        for (Map.Entry<String, String> m : queryTypeToAttributeMap.entrySet()) {
//            list.add(SecurityTypeData.builder().key(m.getKey()).value(m.getValue()).build());
//        }
//        return list;
//    }
//
// }
