package cn.asany.organization.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 组织状态
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019/11/13 11:36 上午
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(
    name = "ORG_EMPLOYEE_STATUS",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"ORGANIZATION_ID", "DIMENSION_ID", "CODE"},
            name = "UK_EMPLOYEE_STATUS_CODE"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeStatus extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID", length = 20)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "CODE", length = 50, nullable = false)
  private String code;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_STATUS_ORGANIZATION"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Organization organization;
  /** 所属组织纬度 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DIMENSION_ID",
      foreignKey = @ForeignKey(name = "FK_EMPLOYEE_STATUS_DIMENSION"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private OrganizationDimension dimension;
  /** 是否是默认值 */
  @Column(name = "is_default", length = 10)
  private Boolean isDefault;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    EmployeeStatus status = (EmployeeStatus) o;
    return id != null && Objects.equals(id, status.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
