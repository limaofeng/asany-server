package cn.asany.shanhai.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * 接口返回类型
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(
    callSuper = false,
    of = {"id", "type"})
@ToString(of = "id")
@Entity
@Table(name = "SH_MODEL_ENDPOINT_RETURN_TYPE")
public class ModelEndpointReturnType implements Serializable {
  @Id
  @Column(name = "ENDPOINT_ID", nullable = false, updatable = false, precision = 22, scale = 0)
  @GenericGenerator(
      name = "ModelEndpointReturnTypePkGenerator",
      strategy = "foreign",
      parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "endpoint")})
  @GeneratedValue(generator = "ModelEndpointReturnTypePkGenerator")
  private Long id;
  /** 是否必填 */
  @Builder.Default
  @Column(name = "IS_REQUIRED", length = 1)
  private Boolean required = false;
  /** 存储值为列表，而不是单个值 */
  @Builder.Default
  @Column(name = "IS_LIST", length = 1)
  private Boolean list = false;
  /** 类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_ENDPOINT_RETURN_TYPE_TID"),
      nullable = false)
  private Model type;
  /** 接口 */
  @OneToOne(fetch = FetchType.LAZY, targetEntity = ModelEndpoint.class, mappedBy = "returnType")
  private ModelEndpoint endpoint;
}
