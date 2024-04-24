package cn.asany.organization.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "ORG_TEAM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 编码 */
  @Column(name = "CODE", length = 50)
  private String code;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_TEAM_ORG"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Organization organization;

  /** 团队成员 */
  @OneToMany(
      mappedBy = "team",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<TeamMember> members;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Team team = (Team) o;
    return id != null && Objects.equals(id, team.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
