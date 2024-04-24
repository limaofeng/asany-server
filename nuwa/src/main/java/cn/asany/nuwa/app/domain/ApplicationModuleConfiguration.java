package cn.asany.nuwa.app.domain;

import jakarta.persistence.*;
import java.util.Map;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "NUWA_APPLICATION_MODULE_CONFIG",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_APPLICATION_MODULE_CONFIG",
          columnNames = {"APPLICATION_ID", "MODULE_ID"})
    })
public class ApplicationModuleConfiguration extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MODULE_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_MODULE_CONFIG_MID"),
      updatable = false,
      nullable = false)
  private ApplicationModule module;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_MODULE_CONFIG_APPID"),
      updatable = false,
      nullable = false)
  private Application application;

  @ElementCollection(fetch = FetchType.LAZY)
  @MapKeyColumn(name = "PARAM_NAME", length = 20)
  @Column(name = "PARAM_VALUE", length = 100)
  @CollectionTable(
      name = "NUWA_APPLICATION_MODULE_CONFIG_VALUES",
      joinColumns =
          @JoinColumn(
              name = "CONFIG_ID",
              referencedColumnName = "ID",
              foreignKey = @ForeignKey(name = "FK_APPLICATION_MODULE_CONFIG_VALUES")))
  private Map<String, String> values;
}
