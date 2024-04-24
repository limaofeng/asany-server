package cn.asany.crm.support.domain;

import cn.asany.crm.support.domain.enums.TicketPriority;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Data
@Entity
@Table(name = "CRM_TICKET_TYPE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketType extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 默认优先级 */
  @Enumerated(EnumType.STRING)
  @Column(name = "DEFAULT_PRIORITY", length = 20)
  private TicketPriority defaultPriority;

  /** 编号模板 */
  @Column(name = "NUMBERING_TEMPLATE", length = 200)
  private String numberingTemplate;
}
