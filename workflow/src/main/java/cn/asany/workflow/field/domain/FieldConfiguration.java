package cn.asany.workflow.field.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 字段配置
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD_CONFIGURATION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfiguration extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "field_configuration_gen")
  @TableGenerator(
      name = "field_configuration_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "field_configuration:id",
      valueColumnName = "gen_value")
  private Long id;

  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  @OneToMany(
      mappedBy = "fieldConfiguration",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<FieldConfigurationItem> fields;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldConfiguration that = (FieldConfiguration) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
