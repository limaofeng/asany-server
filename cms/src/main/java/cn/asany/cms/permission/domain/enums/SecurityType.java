package cn.asany.cms.permission.domain.enums;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
public enum SecurityType {
  /** 组织 */
  organization,
  /** 部门 */
  department,
  /** 职务 */
  job,
  /** 职位 */
  position,
  /** 员工 */
  employee,
  /** 群组 */
  employeeGroup,
  /** 角色 */
  role,
  /** 登录用户 */
  user,

  /** 本部门 */
  department_self,
}
