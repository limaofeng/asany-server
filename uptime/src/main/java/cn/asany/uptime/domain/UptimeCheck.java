/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.uptime.domain;

import cn.asany.uptime.domain.enums.UptimeCheckType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
