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

import cn.asany.cms.learn.domain.databind.CourseDeserializer;
import cn.asany.cms.learn.domain.enums.LearnerType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 学习者
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_COURSE_LEARNER")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "special"})
public class Learner extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "cms_course_learner_gen")
  @TableGenerator(
      name = "cms_course_learner_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_course_learner:id",
      valueColumnName = "gen_value")
  private Long id;

  /** 学习者类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false, updatable = false)
  private LearnerType type;

  /** 课程 */
  @ManyToOne
  @JsonDeserialize(using = CourseDeserializer.class)
  @JoinColumn(
      name = "COURSE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "CMS_SPECIAL_SUBSCRIBER_ID"))
  private Course course;

  /** 订阅人关联的用户 */
  @Column(
      name = "employee" /*, foreignKey = @ForeignKey(name = "FK_LEARNER_EMPLOYEE_EMP_ID")*/,
      updatable = false,
      nullable = false)
  private Long employee;

  /** 课程的完成进度 */
  @Column(name = "learning_progress")
  private int learningProgress;
}
