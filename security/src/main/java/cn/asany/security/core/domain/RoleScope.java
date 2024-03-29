package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 角色使用范围 使用场景： 1. 部门经理角色，可能不存在所有的组织架构中。只在部分范围内使用。所以通过 RoleScope 来限制授权范围 2.
 * 会议管理中，每场会议都有主席角色，但并不是每场会议都存在主席
 *
 * @author limaofeng@msn.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_SCOPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoleScope extends BaseBusEntity implements Serializable {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 角色使用范围类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE_ID",
      foreignKey = @ForeignKey(name = "FK_ROLE_SCOPE_TYPE"),
      updatable = false)
  @ToString.Exclude
  private RoleScopeType type;

  /** 角色使用范围值 */
  @Column(name = "VALUE", length = 32)
  private String value;

  /** 角色 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ROLE_ID",
      foreignKey = @ForeignKey(name = "FK_ROLE_SCOPE_ROLE_ID"),
      updatable = false)
  @ToString.Exclude
  private Role role;
}
