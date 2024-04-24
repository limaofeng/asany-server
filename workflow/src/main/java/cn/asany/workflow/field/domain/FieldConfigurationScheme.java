package cn.asany.workflow.field.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 字段方案
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
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD_CONFIGURATION_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfigurationScheme extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "field_configuration_scheme_gen")
  @TableGenerator(
      name = "field_configuration_scheme_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "field_configuration_scheme:id",
      valueColumnName = "gen_value")
  private Long id;

  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  @OneToMany(
      mappedBy = "fieldConfigurationScheme",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<FieldConfigurationSchemeItem> fieldConfigurations;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldConfigurationScheme that = (FieldConfigurationScheme) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
