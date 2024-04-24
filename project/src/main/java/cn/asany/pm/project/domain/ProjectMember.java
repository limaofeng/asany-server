package cn.asany.pm.project.domain;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.pm.project.domain.enums.ProjectMemberType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

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
  @TableGenerator
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
      foreignKey = @ForeignKey(name = "FK_PROJECT_MEMBER_EMPLOYEE_ID"),
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
