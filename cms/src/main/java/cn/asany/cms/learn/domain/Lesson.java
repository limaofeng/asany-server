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
package cn.asany.cms.learn.domain;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.learn.domain.enums.LessonType;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 课程内容(章节里面的每一小节)
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_COURSE_LESSON")
public class Lesson extends BaseBusEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  /** 章节类型 */
  @Enumerated(value = EnumType.STRING)
  @Column(name = "lesson_type")
  private LessonType lessonType;

  /** 课程 */
  @ManyToOne
  @JoinColumn(
      name = "COURSE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "CMS_COURSE_LESSON_CID"))
  private Course course;

  /** 课程（章节） */
  @ManyToOne
  @JoinColumn(
      name = "COURSE_SECTION_ID",
      updatable = false,
      foreignKey = @ForeignKey(name = "CMS_COURSE_LESSON_SID"))
  private CourseSection courseSection;

  /** 对应的文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "CMS_COURSE_LESSON_AID"))
  private Article article;
}
