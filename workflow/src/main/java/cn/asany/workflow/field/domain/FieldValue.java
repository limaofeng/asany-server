package cn.asany.workflow.field.domain;

import cn.asany.pm.issue.bean.Issue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.Objects;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD_VALUE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldValue extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "issue_field_value_gen")
  @TableGenerator(
      name = "issue_field_value_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "issue_field_value:id",
      valueColumnName = "gen_value")
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
