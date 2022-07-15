package cn.asany.base.common;

/**
 * 权限类型
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-06-12 09:12
 */
public enum SecurityType {

  /** 部门 */
  department,
  /** 组织 */
  organization,
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
  ;
}
