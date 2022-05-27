package cn.asany.security.core.domain;

import cn.asany.security.core.domain.databind.PermissionDeserializer;
import cn.asany.security.core.domain.databind.PermissionSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 授予权限
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-06-11 20:10
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
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
  //  /** 授权类型 */
  //  @Enumerated(EnumType.STRING)
  //  @Column(name = "TYPE", length = 20, nullable = false)
  //  private GrantPermissionType type;
  /** 被授权者 */
  @Column(name = "VALUE", length = 25, nullable = false)
  private GrantPermissionValue value;
  /** 权限 */
  @JsonProperty("permission")
  @JsonSerialize(using = PermissionSerializer.class)
  @JsonDeserialize(using = PermissionDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PERMISSION",
      foreignKey = @ForeignKey(name = "FK_SECURE_GRANT_PERMISSION_PID"),
      nullable = false)
  @ToString.Exclude
  private Permission permission;
  /** 授权过期时间 不设置为永久 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRES_AT")
  private Date expiresAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    GrantPermission that = (GrantPermission) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
