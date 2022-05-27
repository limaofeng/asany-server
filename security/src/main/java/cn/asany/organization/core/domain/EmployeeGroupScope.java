package cn.asany.organization.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 用戶組分类
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_GROUP_SCOPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeGroupScope extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  private String id;

  @Column(name = "NAME")
  private String name;

  /** 用户组范围 */
  @OneToMany(mappedBy = "scope", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<EmployeeGroup> groups;

  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_GROUP_SCOPE_OID"),
      updatable = false,
      nullable = false)
  private Organization organization;
}
