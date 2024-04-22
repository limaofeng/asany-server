package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.Tenantable;

/**
 * 角色
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_ROLE")
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
})
public class Role extends BaseBusEntity implements Tenantable {

  @Transient
  public static final Role USER = Role.builder().id(1L).name("USER").description("普通用户").build();

  @Transient
  public static final Role ADMIN = Role.builder().id(2L).name("ADMIN").description("管理员").build();

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 角色名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 角色类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private RoleType type;

  /** 信任的实体 */
  @Embedded TrustedEntity trustedEntity;

  /** 角色使用范围 */
  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  private List<RoleScope> scopes;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24)
  private String tenantId;
}
