package cn.asany.pm.field.bean;

import cn.asany.pm.issue.core.domain.Issue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 字段值
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
@Table(name = "ISSUE_FIELD_VALUE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldValue extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 对应的问题 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ISSUE_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_FIELD_VALUE_ISSUE"))
  @ToString.Exclude
  private Issue issue;

  /** 对应的字段 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FIELD_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_FIELD_VALUE_FIELD"))
  @ToString.Exclude
  private Field field;

  /** 存储的值 */
  @Column(name = "VALUE", length = 250)
  private String value;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldValue that = (FieldValue) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
