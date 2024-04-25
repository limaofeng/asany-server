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

import cn.asany.base.usertype.FileUserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import javax.tools.FileObject;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/** 专栏／专题 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_SPECIAL")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "subscribers", "articles"})
public class Special extends BaseBusEntity {
  /** 专题编码 */
  @Id
  @Column(name = "ID", length = 50, nullable = false, updatable = false)
  private String id;

  /** 专题名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "SUMMARY", length = 1000)
  private String summary;

  /** 封面 */
  @Column(name = "COVER", length = 500)
  @Type(FileUserType.class)
  private FileObject cover;

  /** 期刊号 */
  @Column(name = "ISSN", length = 50)
  private String issn;

  /** 发布日期 */
  @Column(name = "PUBLISH_DATE")
  private Date publishDate;

  /** 专栏简介 */
  @Lob
  @Column(name = "INTRODUCTION")
  private String introduction;

  /** 订阅人数 */
  @Column(name = "SUBSCRIBER_COUNT")
  private Long subscriberCount;

  /** 订阅者 */
  @OneToMany(
      mappedBy = "special",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Subscriber> subscribers;

  /** 专栏文章 */
  @OneToMany(
      mappedBy = "special",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<SpecialArticle> articles;
}
