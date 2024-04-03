package cn.asany.nuwa.app.domain;

import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NUWA_APPLICATION_MODULE")
public class ApplicationModule extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, length = 50)
  private String id;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @ElementCollection
  @CollectionTable(
      name = "NUWA_APPLICATION_MODULE_PARAMETERS",
      joinColumns = @JoinColumn(name = "MODULE_ID"))
  @Column(name = "NAME", length = 20)
  private Set<String> parameterNames;
}
