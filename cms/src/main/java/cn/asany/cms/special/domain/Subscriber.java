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
package cn.asany.cms.special.domain;

import cn.asany.cms.special.domain.enums.SubscriberType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** 订阅记录 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "CMS_SUBSCRIBER",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_SUBSCRIBER_UNIQUE",
          columnNames = {"SPECIAL_ID", "TYPE", "VALUE"})
    })
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "special"})
public class Subscriber extends BaseBusEntity {
  /** 订阅人ID */
  @Id
  //  @Null(groups = RESTful.POST.class)
  @Column(name = "ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "subscriber_gen")
  @TableGenerator(
      name = "subscriber_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_subscriber:id",
      valueColumnName = "gen_value")
  private Long id;

  /** 订阅人名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  /** 订阅类型: member／team */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false, updatable = false)
  private SubscriberType type;

  /** 订阅专栏 */
  @ManyToOne
  @JoinColumn(
      name = "SPECIAL_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "CMS_SPECIAL_SUBSCRIBER"))
  private Special special;

  /** 订阅人关联的用户 */
  @Column(name = "VALUE", nullable = false, updatable = false, length = 32)
  private String value;

  /** 到期时间 */
  @Column(name = "EXPIRES", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date expires;
}
