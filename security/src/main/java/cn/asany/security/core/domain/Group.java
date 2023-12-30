package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
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
          columnNames = {"NAME", "TENANT_ID"})
    })
@JsonIgnoreProperties({
  "hibernate_lazy_initializer",
  "handler",
})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group extends BaseBusEntity implements Tenantable {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 显示名称 */
  @Column(name = "DISPLAY_NAME", length = 50)
  private String displayName;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;
  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24, nullable = false)
  private String tenantId;
  /** 组内成员 */
  @OneToMany(
      mappedBy = "group",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<GroupMember> members;
}