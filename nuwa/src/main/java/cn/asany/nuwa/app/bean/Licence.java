package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.LicenceStatus;
import cn.asany.nuwa.app.bean.enums.LicenceType;
import cn.asany.organization.core.bean.Organization;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 应用许可
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "NUWA_APPLICATION_LICENCE",
    uniqueConstraints = {@UniqueConstraint(name = "UK_LICENCE_KEY", columnNames = "LICENCE_KEY")})
public class Licence extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 许可证 Key */
  @Column(name = "LICENCE_KEY", length = 1024)
  private String key;
  /** 许可证类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 50)
  private LicenceType type;
  /** 许可证状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 50)
  private LicenceStatus status;
  /** 应用 */
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_LICENCE_APP"))
  private Application application;
  /** 持有人 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "OWNERSHIP",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_LICENCE_OWNERSHIP"))
  private Organization ownership;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Licence licence = (Licence) o;
    return id != null && Objects.equals(id, licence.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
