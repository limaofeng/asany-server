package cn.asany.email.user.bean;

import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 邮件设置 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "JamesMailSettings")
@Table(name = "JAMES_MAIL_SETTINGS")
public class MailSettings extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_ID",
      foreignKey = @ForeignKey(name = "FK_MAIL_SETTINGS_USER_ID"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private MailUser user;

  @ElementCollection
  @CollectionTable(
      name = "JAMES_MAIL_SETTINGS_MAILBOXES",
      foreignKey = @ForeignKey(name = "FK_MAIL_SETTINGS_MAILBOXES"),
      joinColumns = @JoinColumn(name = "SETTINGS_ID"))
  @Column(name = "MAILBOX")
  private Set<String> mailboxes;
}
