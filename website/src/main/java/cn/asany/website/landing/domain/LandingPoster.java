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

/** 海报 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "WEBSITE_LANDING_POSTER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LandingPoster extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 门店名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;

  /** 海报背景图片 */
  @Type(FileUserType.class)
  @Column(name = "BACKGROUND", precision = 500)
  private FileObject background;

  /** 海报背景音乐 */
  @Type(FileUserType.class)
  @Column(name = "MUSIC", precision = 500)
  private FileObject music;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    LandingPoster poster = (LandingPoster) o;
    return id != null && Objects.equals(id, poster.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
