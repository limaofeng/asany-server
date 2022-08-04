package cn.asany.pm.field.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
@Table(name = "ISSUE_FIELD_CONFIGURATION_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfigurationScheme extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
