package cn.asany.pm.field.bean;

import cn.asany.pm.field.bean.enums.FieldCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
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
@Table(name = "ISSUE_FIELD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Field extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  @Column(name = "LABEL", length = 50, nullable = false)
  private String label;

  @Enumerated(EnumType.STRING)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_ISSUE_FIELD_TYPE_ID"))
  @ToString.Exclude
  private FieldType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", length = 20, nullable = false)
  private FieldCategory category;

  @Column(name = "RENDERER", length = 20)
  private String renderer;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Field field = (Field) o;
    return id != null && Objects.equals(id, field.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
