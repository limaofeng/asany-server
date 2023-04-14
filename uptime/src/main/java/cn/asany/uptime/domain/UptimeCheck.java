package cn.asany.uptime.domain;

import cn.asany.uptime.domain.enums.UptimeCheckType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "UPTIME_CHECK")
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UptimeCheck extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false, length = 10)
  private UptimeCheckType type;

  @Column(name = "TARGET", nullable = false, length = 200)
  private String target;

  @ElementCollection
  @CollectionTable(
      name = "UPTIME_CHECK_REGION_REGIONS",
      foreignKey = @ForeignKey(name = "FK_UPTIME_CHECK_REGION"),
      joinColumns = @JoinColumn(name = "REGION_ID"))
  @Column(name = "REGION")
  private List<String> regions;

  @Column(name = "ENABLED", nullable = false, length = 1)
  private Boolean enabled;
}
