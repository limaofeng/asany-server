package cn.asany.pim.core.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@Table(name = "PIM_WARRANTY_POLICY")
@EqualsAndHashCode(callSuper = true)
public class WarrantyCard extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", length = 50)
  private String name;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "ISSUE_DATE")
  private Date issueDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRY_DATE")
  private Date expiryDate;

  @ManyToOne
  @JoinColumn(name = "DEVICE_ID", foreignKey = @ForeignKey(name = "FK_WARRANTY_CARD_DEVICE_ID"))
  private Device device;

  @ManyToOne
  @JoinColumn(
      name = "WARRANTY_POLICY_ID",
      foreignKey = @ForeignKey(name = "FK_WARRANTY_CARD_WARRANTY_POLICY_ID"))
  private WarrantyPolicy policy;
}
