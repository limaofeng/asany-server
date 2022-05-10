package cn.asany.organization.core.bean;

import cn.asany.organization.core.bean.enums.OrganizationInviteStatus;
import cn.asany.security.core.bean.Role;
import cn.asany.security.core.bean.User;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 组织邀请信息 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "ORG_ORGANIZATION_INVITE",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"USER_ID", "ORGANIZATION_ID"},
            name = "UK_ORGANIZATION_INVITE_USER"))
public class OrganizationInvite extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private OrganizationInviteStatus status;
  /** 邀请成为的角色 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ROLE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_INVITE_ROLE"))
  @ToString.Exclude
  private Role role;
  /** 用户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_INVITE_USER"))
  @ToString.Exclude
  private User user;
  /** 团队ID */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_INVITE_ORG"))
  @ToString.Exclude
  private Organization organization;
}
