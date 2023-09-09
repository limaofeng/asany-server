package cn.asany.pm.field.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
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
@Table(name = "ISSUE_FIELD_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldType extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 24)
  private String id;

  /** 类型名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500, nullable = false)
  private String description;

  /** 示例值 */
  @Column(name = "EXAMPLE_VALUE", length = 500, nullable = false)
  private String example;

  /** 值类型 */
  @Column(name = "VALUE_TYPE", length = 20, nullable = false)
  private String valueType;

  /** 引用类型 */
  @Column(name = "REFERENCE_TYPE", length = 20, nullable = false)
  private String reference;

  /** 是否为集合类型 */
  @Column(name = "IS_LIST", length = 1, nullable = false)
  private boolean list;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldType fieldType = (FieldType) o;
    return id != null && Objects.equals(id, fieldType.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
