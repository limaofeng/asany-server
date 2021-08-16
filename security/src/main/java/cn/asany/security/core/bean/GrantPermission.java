package cn.asany.security.core.bean;

import cn.asany.base.common.SecurityType;
import cn.asany.security.core.bean.databind.PermissionDeserializer;
import cn.asany.security.core.bean.databind.PermissionSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 授予权限
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-06-11 20:10
 */
@Data
@EqualsAndHashCode(
    callSuper = false,
    of = {"securityType", "value", "resource", "permission"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_GRANT_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GrantPermission extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "auth_grant_permission_gen")
  @TableGenerator(
      name = "auth_grant_permission_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "auth_grant_permission:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 授权类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SECURITY_TYPE", length = 20, nullable = false)
  private SecurityType securityType;
  /** 授权 */
  @Column(name = "VALUE", length = 25, nullable = false)
  private String value;
  /** 资源 */
  @Column(name = "RESOURCE", length = 25)
  private String resource;
  /** 权限 */
  @JsonProperty("permission")
  @JsonSerialize(using = PermissionSerializer.class)
  @JsonDeserialize(using = PermissionDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PERMISSION",
      foreignKey = @ForeignKey(name = "FK_SECURE_GRANT_PERMISSION_PID"),
      nullable = false)
  private Permission permission;
  /** 权限方案 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SCHEME", foreignKey = @ForeignKey(name = "FK_SECURE_GRANT_PERMISSION_SID"))
  private PermissionScheme scheme;
}
