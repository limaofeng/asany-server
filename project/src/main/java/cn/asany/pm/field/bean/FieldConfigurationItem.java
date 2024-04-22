package cn.asany.pm.field.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 字段项配置
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
@Table(name = "ISSUE_FIELD_CONFIGURATION_ITEM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfigurationItem extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "FIELD_ID",
      foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_ITEM_FIELD_ID"))
  @ToString.Exclude
  private Field field;

  @Column(name = "REQUIRED")
  private Boolean required;

  @Column(name = "RENDERER", length = 32)
  private String renderer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CONFIG_ID",
      foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_ITEM_CONFIG_ID"))
  @ToString.Exclude
  private FieldConfiguration fieldConfiguration;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldConfigurationItem that = (FieldConfigurationItem) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
