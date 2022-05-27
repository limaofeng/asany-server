package cn.asany.security.core.dao;

import cn.asany.security.core.domain.GrantPermission;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-07-17 10:34
 */
@Repository
public interface GrantPermissionDao extends JpaRepository<GrantPermission, Long> {

  //  /**
  //   * 查询权限行数
  //   *
  //   * @param permissinId 权限ID
  //   * @param securityType 实体类型
  //   * @param value 实体值
  //   * @return count
  //   */
  //  int countGrantPermissionByPermissionIdAndSecurityTypeAndValue(
  //      String permissinId, SecurityType securityType, String value);

  //  /**
  //   * 删除权限类型对应的授权关系，重新授权
  //   *
  //   * @param permissinId 权限ID
  //   * @param securityType 授权类型
  //   */
  //  void deleteGrantPermissionByPermissionIdAndSecurityType(
  //      String permissinId, SecurityType securityType);

  //  /**
  //   * 删除权限对应的授权关系
  //   *
  //   * @param permissinId 权限ID
  //   */
  //  void deleteGrantPermissionByPermissionId(String permissinId);

  //  /**
  //   * 根据类型和值取权限列表
  //   *
  //   * @param securityType 类型
  //   * @param value 值
  //   * @return List
  //   */
  //  List<GrantPermission> findGrantPermissionsBySecurityTypeAndValue(
  //      SecurityType securityType, String value);

  //  /**
  //   * 删除类型对应的权限
  //   *
  //   * @param securityType securityType类型
  //   * @param value 值
  //   */
  //  void deleteGrantPermissionsBySecurityTypeAndValue(SecurityType securityType, String value);

  //  /**
  //   * 根据权限取授权实体信息
  //   *
  //   * @param permissionId 权限ID
  //   * @return List
  //   */
  //  List<GrantPermission> findGrantPermissionsByPermissionId(String permissionId);
}
