package cn.asany.email.user.domain;

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
  @Column(name = "USER_ID", nullable = false, updatable = false, precision = 22)
  @GenericGenerator(
      name = "MailSettingsPkGenerator",
      strategy = "foreign",
      parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "user")})
  @GeneratedValue(generator = "MailSettingsPkGenerator")
  private String id;

  @OneToOne(fetch = FetchType.LAZY, targetEntity = MailUser.class, mappedBy = "settings")
  @ToString.Exclude
  private MailUser user;

  @ElementCollection
  @CollectionTable(
      name = "JAMES_MAIL_SETTINGS_MAILBOXES",
      foreignKey = @ForeignKey(name = "FK_MAIL_SETTINGS_MAILBOXES"),
      joinColumns = @JoinColumn(name = "USER_ID"))
  @Column(name = "MAILBOX")
  private Set<String> mailboxes;
}
