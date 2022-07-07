package cn.asany.nuwa.app.domain;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NUWA_APPLICATION_DEPENDENCY")
public class ApplicationDependency extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "TYPE", length = 50)
  private String type;

  @Column(name = "VALUE", length = 50)
  private String value;

  @Column(name = "VERSION", length = 50)
  private String version;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_DEPENDENCY_APPID"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Application application;
}
