package cn.asany.crm.support.domain;

import cn.asany.crm.support.domain.enums.TicketStatus;
import cn.asany.security.core.domain.User;
import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CRM_TICKET_STATUS_LOG")
@EqualsAndHashCode(callSuper = true)
public class TicketStatusLog extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TICKET_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_TICKET_STATUS_LOG_TICKET"))
  private Ticket ticket;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20)
  private TicketStatus status;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LOG_TIME")
  private Date logTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "logged_by_user_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // 不需要外键约束
  private User loggedBy;

  @Column(name = "NOTE", length = 200)
  private String note;
}
