package cn.asany.cardhop.contacts.domain;

import cn.asany.base.common.domain.Email;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CARDHOP_CONTACT_EMAIL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "contact"})
public class ContactEmail extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  @TableGenerator
  private Long id;

  @Column(name = "IS_PRIMARY", nullable = false)
  private Boolean primary;

  @Column(name = "LABEL", length = 30)
  private String label;

  @Embedded private Email email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CONTACT_ID",
      foreignKey = @ForeignKey(name = "FK_CARDHOP_CONTACT_EMAIL_CID"),
      nullable = false,
      updatable = false)
  private Contact contact;
}
