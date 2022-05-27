package cn.asany.shanhai.core.domain;

import cn.asany.shanhai.core.domain.enums.ModelDelegateType;
import cn.asany.shanhai.core.support.graphql.DelegateHandler;
import cn.asany.shanhai.gateway.domain.Service;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 实体接口委派
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(
    callSuper = false,
    of = {"id", "name", "type"})
@ToString(of = "id")
@Entity
@Table(name = "SH_MODEL_DELEGATE")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelDelegate extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 委托类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 50)
  private ModelDelegateType type;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 委托处理类 */
  @Column(name = "DELEGATE_CLASS_NAME", length = 200)
  private String delegateClassName;
  /** 规则 */
  @Transient private ModelEndpointDelegateRule rules;
  /** 服务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SERVICE_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_ENDPOINT_DELEGATE_SID"))
  private Service service;

  @Transient
  public DelegateHandler getDelegateHandler() {
    return null;
  }

  static class ModelEndpointDelegateRule {
    String query;
    Object reject;
    String args;
  }
}
