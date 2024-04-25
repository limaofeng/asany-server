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
package cn.asany.website.landing.domain;

import cn.asany.base.common.domain.Address;
import cn.asany.base.common.domain.Geolocation;
import cn.asany.base.usertype.FileUserType;
import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "WEBSITE_LANDING_STORE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LandingStore extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 门店编码 */
  @Column(name = "CODE", length = 100, nullable = false)
  private String code;

  /** 门店名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;

  /** 门店位置 */
  @Embedded private Address address;

  /** 门店位置 (经纬坐标) */
  @Embedded private Geolocation location;

  /** 二维码 */
  @Type(FileUserType.class)
  @Column(name = "QR_CODE", precision = 500)
  private FileObject qrCode;

  /** 门店负责人 */
  @Column(name = "LEADER", precision = 100)
  private String leader;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    LandingStore store = (LandingStore) o;
    return id != null && Objects.equals(id, store.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
