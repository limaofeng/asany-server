package cn.asany.shanhai.core.domain;

import cn.asany.shanhai.core.domain.converter.MatchTypeConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.MatchType;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

/**
 * 字段元数据
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SH_MODEL_FIELD_METADATA")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelFieldMetadata implements Serializable {
  @Id
  @Column(name = "FIELD_ID", nullable = false, updatable = false)
  @GenericGenerator(
      name = "ModelFieldMetadataPkGenerator",
      strategy = "foreign",
      parameters = {@Parameter(name = "property", value = "field")})
  @GeneratedValue(generator = "ModelFieldMetadataPkGenerator")
  private Long id;
  /** 数据库中的列表名称 */
  @Column(name = "DATABASE_COLUMN_NAME", length = 100)
  private String databaseColumnName;
  /** 是否唯一 */
  @Column(name = "IS_UNIQUE", length = 1)
  private Boolean unique;
  /** 可插入 */
  @Builder.Default
  @Column(name = "INSERTABLE", length = 1)
  private Boolean insertable = true;
  /** 可修改 */
  @Builder.Default
  @Column(name = "UPDATABLE", length = 1)
  private Boolean updatable = true;
  /** 可排序 */
  @Builder.Default
  @Column(name = "SORTABLE", length = 1)
  private Boolean sortable = true;

  /** 字段支持的筛选方式 */
  @Column(name = "FILTERS", columnDefinition = "JSON")
  @Convert(converter = MatchTypeConverter.class)
  private MatchType[] filters;

  @OneToOne(fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  @ToString.Exclude
  private ModelField field;

  private transient String javaTypeClassName;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ModelFieldMetadata that = (ModelFieldMetadata) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
