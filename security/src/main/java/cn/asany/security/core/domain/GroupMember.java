package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.GranteeType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.Tenantable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
    name = "AUTH_GROUP_MEMBER",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_AUTH_GROUP_MEMBER",
          columnNames = {"GROUP_ID", "TYPE", "VALUE"})
    })
public class GroupMember extends BaseBusEntity implements Tenantable {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 成员类型 */
  @Column(name = "TYPE", length = 24)
  private GranteeType type;

  /** 成员ID */
  @Column(name = "VALUE", length = 24)
  private String value;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24, nullable = false, updatable = false)
  private String tenantId;

  /** 用户组 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "GROUP_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_GROUP_MEMBER_UID"))
  private Group group;
}
