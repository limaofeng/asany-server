package cn.asany.security.core.domain;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 资源类型
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
@Table(name = "AUTH_RESOURCE_TYPE")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ResourceType extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 1024)
  private String description;
  /** 资源名称 */
  @Column(name = "RESOURCE_NAME", length = 50)
  private String resourceName;
  /** 资源的ARN格式 */
  @Column(name = "ARN", length = 250, unique = true)
  private String arn;
  /** 服务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SERVICE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_RESOURCE_TYPE_SERVICE"))
  private AuthorizedService service;
}
