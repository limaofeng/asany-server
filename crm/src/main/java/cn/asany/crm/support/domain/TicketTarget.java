package cn.asany.crm.support.domain;

import cn.asany.base.common.TicketTargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TicketTarget {
  /** 目标ID */
  @Column(name = "TARGET_ID")
  private Long id;

  /** 目标类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TARGET_TYPE", length = 20)
  private TicketTargetType type;

  /** 目标名称 */
  @Column(name = "TARGET_NAME", length = 50)
  private String name;
}
