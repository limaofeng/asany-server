package cn.asany.workflow.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng@msn.com @ClassName: PermissionToRole @Description: 将权限分配给某一类人(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "IssueGrantPermission")
@Table(name = "FSM_GRANT_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class GrantPermission extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  private Long id;

  /** 权限 */
  @JsonProperty("permission")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PERMISSION", foreignKey = @ForeignKey(name = "FK_GRANT_PERMISSION_PID"))
  private Permission permission;

  /** 权限方案 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SCHEME", foreignKey = @ForeignKey(name = "FK_GRANT_PERMISSION_SID"))
  private PermissionScheme scheme;

  /** 操作id */
  //    @JoinColumn(name = "TID", foreignKey = @ForeignKey(name = "FK_GRANT_PERMISSION_TID"))
  //    @ManyToOne(fetch = FetchType.LAZY)
  //    private IssueWorkflowStepTransition tran;

  /** 人类型 */
  //  @Enumerated(EnumType.STRING)
  //  @Column(name = "SECURITY_TYPE", length = 20, nullable = false)
  //  private SecurityType securityType;

  /** 对应的用户或者组 */
  @Column(name = "VALUE", length = 20)
  private String value;
}
