package cn.asany.security.core.domain;

import cn.asany.security.core.domain.converter.ConditionConverter;
import cn.asany.security.core.domain.enums.PermissionPolicyEffect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.Tenantable;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

/**
 * 权限策略声明
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION_STATEMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PermissionStatement implements Serializable, Tenantable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "EFFECT", length = 20)
  private PermissionPolicyEffect effect;

  @Column(name = "ACTION", columnDefinition = "JSON")
  @Convert(converter = StringArrayConverter.class)
  private String[] action;

  @Column(name = "RESOURCE", columnDefinition = "JSON")
  @Convert(converter = StringArrayConverter.class)
  private String[] resource;

  @Column(name = "CONDITION", columnDefinition = "JSON")
  @Convert(converter = ConditionConverter.class)
  private List<PermissionCondition> condition;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PERMISSION_POLICY",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_STATEMENT_POLICY_ID"),
      nullable = false)
  private PermissionPolicy policy;

  @Column(name = "TENANT_ID", length = 24, updatable = false)
  private String tenantId;

  public PermissionStatement(PermissionPolicyEffect effect, String action, String resource) {
    this.effect = effect;
    this.action = new String[] {action};
    this.resource = new String[] {resource};
  }

  public void setAction(String action) {
    this.action = new String[] {action};
  }

  public void setResource(String resource) {
    this.resource = new String[] {resource};
  }

  public void setAction(String[] action) {
    this.action = action;
  }

  public void setResource(String[] resource) {
    this.resource = resource;
  }
}
