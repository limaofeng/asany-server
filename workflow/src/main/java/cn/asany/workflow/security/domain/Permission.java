package cn.asany.workflow.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng@msn.com @ClassName: Permission @Description: 权限bean(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "IssuePermission")
@Table(name = "FSM_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Permission extends BaseBusEntity {
  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  private Long id;

  /** 编码 */
  @Column(name = "CODE", length = 20)
  private String code;

  /** 页面名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 页面描述 */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /** 权限 */
  @OneToMany(
      mappedBy = "permission",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<GrantPermission> permissions;
}
