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
package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 角色授权
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_GRANT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoleGrant extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 32)
  private String id;

  /** 对应角色 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROLE_ID", foreignKey = @ForeignKey(name = "FK_ROLE_GRANT_ROLE_ID"))
  private Role role;

  /** 授权主体 */
  @Embedded private Grantee grantee;

  /** 授予日期 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "GRANT_DATE")
  private Date grantDate;

  /** 过期日期 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRY_DATE")
  private Date expiryDate;
}
