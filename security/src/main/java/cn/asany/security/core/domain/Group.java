package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.Tenantable;

/**
 * 用户组
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "AUTH_GROUP",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_AUTH_GROUP",
          columnNames = {"ID", "TENANT_ID"})
    })
@IdClass(GroupPrimaryKey.class)
@JsonIgnoreProperties({
  "hibernate_lazy_initializer",
  "handler",
  "menus",
  "permissions",
  "roles",
  "permissions"
})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group extends BaseBusEntity implements Tenantable {
  @Id private String id;
  /** 租户ID */
  @Id private String tenantId;
  /** 用户组名称 */
  @Column(name = "NAME")
  private String name;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;
  /** 组内成员 */
  @OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY)
  private List<GroupMember> members;
}
