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
package cn.asany.pm.issue.core.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectsConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 任务评论
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "IssueComment")
@Table(name = "PM_ISSUE_COMMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 任务id */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ISSUE_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_ANNOTATE_ISSUE"))
  @ToString.Exclude
  private Issue issue;

  /** 用户ID */
  @Column(name = "UID", length = 50)
  private Long uid;

  /** 注释内容 */
  @Column(name = "CONTENT", length = 200)
  private String content;

  /** 注释时间 */
  @Column(name = "CONTENT_DATE", length = 200)
  private Date contentDate;

  @Convert(converter = FileObjectsConverter.class)
  @Column(name = "ATTACHMENTS", columnDefinition = "json")
  private List<FileObject> attachments;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Comment that = (Comment) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
