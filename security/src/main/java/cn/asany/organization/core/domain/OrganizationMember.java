package cn.asany.organization.core.domain;

import cn.asany.security.core.domain.Role;
import cn.asany.security.core.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 组织成员 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "ORG_ORGANIZATION_MEMBER",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"USER_ID", "ORGANIZATION_ID"},
            name = "UK_ORGANIZATION_MEMBER_OUID"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children", "employees"})
public class OrganizationMember extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 角色 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ROLE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_MEMBER_ROLE"))
  @ToString.Exclude
  private Role role;
  /** 用户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_MEMBER_USER"))
  @ToString.Exclude
  private User user;
  /** 团队ID */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_MEMBER_ORG"))
  @ToString.Exclude
  private Organization organization;
}
