package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.PermissionPolicyEffect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
@Table(name = "AUTH_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permission implements Serializable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "EFFECT", length = 20)
  private PermissionPolicyEffect effect;

  @Column(name = "ACTION", length = 100)
  private String action;

  @Column(name = "RESOURCE", length = 200)
  @Convert(converter = StringArrayConverter.class)
  private String[] resources;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PERMISSION_POLICY",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_POLICY_ID"),
      nullable = false)
  private PermissionPolicy policy;
}
