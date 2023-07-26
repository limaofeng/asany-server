package cn.asany.security.core.domain;

import cn.asany.security.core.domain.databind.PermissionDeserializer;
import cn.asany.security.core.domain.databind.PermissionSerializer;
import cn.asany.security.core.domain.enums.GranteeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 授予权限
 *
 * @author limaofeng
 * @version V1.0
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_GRANT_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GrantPermission extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 表示被授权的对象类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "GRANTEE_TYPE", length = 25, nullable = false)
  private GranteeType granteeType;
  /** 指定被授权的用户或角色的ID */
  @Column(name = "GRANTEE_ID", length = 25, nullable = false)
  private String granteeId;
  /** 权限 */
  @JsonProperty("permission")
  @JsonSerialize(using = PermissionSerializer.class)
  @JsonDeserialize(using = PermissionDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PERMISSION_POLICY",
      foreignKey = @ForeignKey(name = "FK_GRANT_PERMISSION_POLICY"),
      nullable = false)
  @ToString.Exclude
  private PermissionPolicy permissionPolicy;
}
