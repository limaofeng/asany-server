package cn.asany.organization.core.domain;

import cn.asany.organization.core.domain.enums.DepartmentLinkType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: 部门关联表
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_DEPARTMENT_LINK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DepartmentLink extends BaseBusEntity {
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 内部部门 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DEPARTMENT_ID", foreignKey = @ForeignKey(name = "FK_DEPARTMENT_LINK_EID"))
  private Department department;

  /** 链接类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 10)
  private DepartmentLinkType type;

  /** 外部ID */
  @Column(name = "LINK_ID", length = 32)
  private String linkId;
}
