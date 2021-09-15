package cn.asany.organization.relationship.bean;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.Job;
import cn.asany.organization.core.bean.Organization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 职位
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-22 下午04:00:48
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_POSITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Position extends BaseBusEntity {

  private static final long serialVersionUID = -7020427994563623645L;
  /** 职位编码 */
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 职位名称 */
  @Column(name = "NAME", nullable = false, length = 50)
  private String name;
  /** 职位描述信息 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;
  /** 对应的职务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "JOB_ID", foreignKey = @ForeignKey(name = "FK_POSITION_JID"))
  private Job job;
  /** 职位拥有的角色 */
  //    @JsonIgnore
  //    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
  //    @JoinTable(name = "AUTH_ROLE_POSITION", joinColumns = @JoinColumn(name = "POSITION_ID",
  // foreignKey = @ForeignKey(name = "FK_AUTH_ROLE_POSITION_PID")), inverseJoinColumns =
  // @JoinColumn(name = "ROLE_CODE"), foreignKey = @ForeignKey(name = "FK_ROLE_POSITION_RID"))
  //    private List<Role> roles;
  /** 所属部门 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DEPARTMENT_ID", foreignKey = @ForeignKey(name = "FK_POSITION_PID"))
  private Department department;
  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORG_POSITION_OID"),
      updatable = false,
      nullable = false)
  private Organization organization;
}
