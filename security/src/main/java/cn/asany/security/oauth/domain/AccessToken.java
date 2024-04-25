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
package cn.asany.security.oauth.domain;

import cn.asany.security.core.domain.User;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.dao.hibernate.converter.StringSetConverter;
import net.asany.jfantasy.framework.security.auth.TokenType;
import org.hibernate.Hibernate;

/**
 * 访问令牌
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
    name = "AUTH_ACCESS_TOKEN",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "TOKEN", name = "UN_AUTH_ACCESS_TOKEN_UNIQUE")
    })
public class AccessToken extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 凭证类型 */
  @Column(name = "TOKEN_TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  /** 范围 */
  @Column(name = "SCOPES")
  @Convert(converter = StringSetConverter.class)
  private Set<String> scopes;

  /** 生成时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "ISSUED_AT")
  private Date issuedAt;

  /** 过期时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRES_AT")
  private Date expiresAt;

  /** Token */
  @Column(name = "TOKEN", length = 500)
  private String token;

  /** 刷新 Token */
  @Column(name = "REFRESH_TOKEN", length = 32)
  private String refreshToken;

  /** 最后使用时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_USED_TIME")
  private Date lastUsedTime;

  /** 应用 */
  @Column(name = "CLIENT_ID", length = 20, updatable = false, nullable = false)
  private String client;

  /** 密钥 */
  @Column(name = "CLIENT_SECRET", length = 40, updatable = false, nullable = false)
  private String clientSecret;

  /** 客户详细信息 */
  @Embedded private AccessTokenClientDetails clientDetails;

  /** 用户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_ID",
      foreignKey = @ForeignKey(name = "FK_ACCESS_TOKEN_USER"),
      updatable = false)
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    AccessToken that = (AccessToken) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
