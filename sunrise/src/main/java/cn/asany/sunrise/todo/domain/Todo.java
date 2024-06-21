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
package cn.asany.sunrise.todo.domain;

import cn.asany.sunrise.todo.domain.enums.TodoState;
import cn.asany.sunrise.todo.domain.toys.When;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 待办
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SUNRISE_TODO")
public class Todo extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 标题 */
  @Column(name = "TITLE", length = 200)
  private String title;

  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 10, nullable = false)
  private TodoState status;

  /** 在完成 */
  @Embedded private When when;

  /** 截止时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "DEADLINE")
  private Date deadline;

  /** 完成时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "DUE_DATE")
  private Date dueDate;

  /** 地点 */
  @Column(name = "LOCATION", length = 100)
  private String location;

  /** 说明 */
  @Column(name = "NOTES", columnDefinition = "TEXT")
  private String notes;

  /** tags */
  @ElementCollection
  @CollectionTable(
      name = "SUNRISE_TODO_TAGS",
      foreignKey = @ForeignKey(name = "FK_SUNRISE_TODO_TAGS_TAG"),
      joinColumns = @JoinColumn(name = "TODO_ID"))
  @Column(name = "TAG")
  private List<String> tags;

  //  private List<Checklist> checklists;

  /** 链接 */
  @Column(name = "URL", length = 120)
  private String url;
}
