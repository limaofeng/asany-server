package cn.asany.shanhai.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(
    callSuper = false,
    of = {"id", "name"})
@ToString(of = "id")
@Entity
@Table(name = "SH_MODEL_FIELD_ARGUMENT")
public class ModelFieldArgument implements Serializable {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;
  /** 类型 */
  @Column(name = "TYPE", length = 50, nullable = false)
  private String type;
  /** 类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_ARGUMENT_TID"),
      nullable = false)
  private Model realType;
  /** 是否必填 */
  @Builder.Default
  @Column(name = "IS_REQUIRED")
  private Boolean required = false;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 默认值 */
  @Column(name = "DEFAULT_VALUE", length = 50)
  private String defaultValue;

  /** 接口 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "FIELD_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_ARGUMENT_EID"),
      nullable = false)
  private ModelField field;

  public static class ModelFieldArgumentBuilder {

    public ModelFieldArgumentBuilder type(String type) {
      this.type = type;
      this.realType = Model.builder().code(type).build();
      return this;
    }

    public ModelFieldArgumentBuilder type(Model type) {
      this.type = type.getCode();
      this.realType = type;
      return this;
    }
  }
}
