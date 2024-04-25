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
package cn.asany.cms.article.domain;

import cn.asany.base.usertype.FileUserType;
import cn.asany.cms.article.domain.enums.BackgroundType;
import cn.asany.storage.api.FileObject;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

/**
 * 横幅
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_BANNER")
@DynamicUpdate
public class Banner extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 标题 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 标题 */
  @Column(name = "TITLE", length = 200)
  private String title;

  /** 副标题 */
  @Column(name = "SUBTITLE", length = 200)
  private String subtitle;

  /** 描述 */
  @Column(name = "description", length = 500)
  private String description;

  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;

  /** 媒介 */
  @Column(name = "BACKGROUND_TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private BackgroundType backgroundType;

  /** 媒介 */
  @Column(name = "BACKGROUND", columnDefinition = "TINYTEXT")
  @Type(FileUserType.class)
  private FileObject background;

  /** 地址 */
  @Column(name = "URL", length = 100)
  private String url;

  /** 按钮文字 */
  @Column(name = "BUTTON_TEXT", length = 100)
  private String buttonText;
}
