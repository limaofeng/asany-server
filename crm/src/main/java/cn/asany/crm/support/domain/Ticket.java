package cn.asany.crm.support.domain;

import cn.asany.base.common.domain.ContactInformation;
import cn.asany.crm.core.domain.Customer;
import cn.asany.crm.core.domain.CustomerStore;
import cn.asany.crm.support.domain.enums.TicketPriority;
import cn.asany.crm.support.domain.enums.TicketStatus;
import cn.asany.security.core.domain.User;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@Table(name = "CRM_TICKET")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ticket extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编号 */
  @Column(name = "NO", length = 50)
  private String no;
  /** 问题描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 问题状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20)
  private TicketStatus status;
  /** 优先级 */
  @Enumerated(EnumType.STRING)
  @Column(name = "PRIORITY", length = 20)
  private TicketPriority priority;
  /** 分配给 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ASSIGNEE_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User assignee;
  /** 客户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "FK_TICKET_CUSTOMER"))
  private Customer customer;
  /** 门店 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STORE_ID", foreignKey = @ForeignKey(name = "FK_TICKET_CUSTOMER_STORE"))
  private CustomerStore store;
  /** 报修人联系方式 */
  @Embedded private ContactInformation contactInfo;
  /** 催办标志位，默认为false */
  @Column(name = "URGENT")
  @Builder.Default
  private Boolean urgent = Boolean.FALSE;
  /** 评价 */
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "TICKET_ID")
  private TicketRating rating;
}
