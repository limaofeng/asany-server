package cn.asany.pm.project.domain;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.pm.project.domain.enums.ProjectMemberType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 项目成员
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_PROJECT_MEMBER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectMember extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称（只在 ANONYMOUS 时有效） */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 成员类型 */
  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private ProjectMemberType type;
  /** 关联的员工 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EMPLOYEE_ID",
      foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_EMAIL_EID"),
      updatable = false)
  private Employee employee;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ProjectMember member = (ProjectMember) o;
    return id != null && Objects.equals(id, member.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
