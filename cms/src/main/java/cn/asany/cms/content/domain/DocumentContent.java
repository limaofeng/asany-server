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
package cn.asany.cms.content.domain;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.domain.enums.DocumentType;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "CMS_DOCUMENT_CONTENT")
public class DocumentContent extends BaseBusEntity implements ArticleContent {

  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 文档的URL，用于下载或在线查看 */
  @Column(name = "URL", length = 250)
  private String url;

  /** 文档的存储对象 */
  @Column(name = "DOCUMENT_STORE", columnDefinition = "JSON")
  @Convert(converter = FileObjectConverter.class)
  private FileObject document;

  /** 文档大小 单位：字节 */
  private Long size;

  /** 文档类型，如"PDF", "DOCX", "XLSX"等 */
  @Enumerated(EnumType.STRING)
  @Column(name = "DOCUMENT_TYPE", length = 10)
  private DocumentType type;

  /** 文档标题 */
  @Column(name = "TITLE", length = 50)
  private String title;

  /** 文档描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  @Override
  public String generateSummary() {
    return null;
  }

  @Override
  public ContentType getContentType() {
    return ContentType.DOCUMENT;
  }
}
