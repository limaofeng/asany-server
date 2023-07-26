package cn.asany.shanhai.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * 参数
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(
    callSuper = false,
    of = {"id", "name"})
@ToString(of = "id")
@Entity
@Table(name = "SH_MODEL_ENDPOINT_ARGUMENT")
public class ModelEndpointArgument implements Serializable {
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
  /** 关联的具体字段类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_ENDPOINT_ARGUMENT_TID"),
      nullable = false)
  private Model realType;
  /** 是否必填 */
  @Builder.Default
  @Column(name = "IS_REQUIRED")
  private Boolean required = false;

  @Builder.Default
  @Column(name = "IS_LIST", length = 1, updatable = false)
  private Boolean list = false;
  /** 排序 */
  @Column(name = "SORT", nullable = false)
  private Integer index;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 默认值 */
  @Column(name = "DEFAULT_VALUE", length = 200)
  private String defaultValue;
  /** 接口 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ENDPOINT_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_ENDPOINT_ARGUMENT_EID"),
      nullable = false)
  private ModelEndpoint endpoint;

  public static class ModelEndpointArgumentBuilder {
    public ModelEndpointArgumentBuilder type(String type) {
      this.type = type;
      this.realType = Model.builder().code(type).build();
      return this;
    }

    public ModelEndpointArgumentBuilder type(Model type) {
      this.type = type.getCode();
      this.realType = type;
      return this;
    }
  }
}
