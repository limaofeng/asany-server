package cn.asany.pm.security.bean;

import cn.asany.pm.security.bean.enums.SecurityType;
import cn.asany.pm.security.bean.permissionbind.PermissionDeserializer;
import cn.asany.pm.security.bean.permissionbind.PermissionSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 将权限分配给某一类人
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "IssueGrantPermission")
@Table(name = "GRANT_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class GrantPermission extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 权限 */
  @JsonProperty("permission")
  @JsonSerialize(using = PermissionSerializer.class)
  @JsonDeserialize(using = PermissionDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_GRANT_PERMISSION_PID"))
  @ToString.Exclude
  private Permission permission;

  /** 权限方案 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SID", foreignKey = @ForeignKey(name = "FK_GRANT_PERMISSION_SID"))
  @ToString.Exclude
  private PermissionScheme scheme;

  /** 操作id */
  //    @JoinColumn(name = "TID", foreignKey = @ForeignKey(name = "FK_GRANT_PERMISSION_TID"))
  //    @ManyToOne(fetch = FetchType.LAZY)
  //    private IssueWorkflowStepTransition tran;

  /** 人类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SECURITY_TYPE", length = 20, nullable = false)
  private SecurityType securityType;

  /** 对应的用户或者组 */
  @Column(name = "VALUE", length = 20)
  private String value;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    GrantPermission that = (GrantPermission) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
