package cn.asany.cms.learn.domain;

import cn.asany.cms.learn.domain.databind.CourseDeserializer;
import cn.asany.cms.learn.domain.databind.LearnerDeserializer;
import cn.asany.cms.learn.domain.databind.LessonDeserializer;
import cn.asany.cms.learn.domain.enums.LessonScheduleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 学习记录
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_COURSE_LESSON_RECORD")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class LessonRecord extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 课程 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonDeserialize(using = CourseDeserializer.class)
  @JoinColumn(
      name = "COURSE_ID",
      foreignKey = @ForeignKey(name = "FK_COURSE_LESSON_RECORD_ID"),
      updatable = false,
      nullable = false)
  private Course course;

  /** 章节 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonDeserialize(using = LessonDeserializer.class)
  @JoinColumn(
      name = "LESSON_ID",
      foreignKey = @ForeignKey(name = "FK_LESSON_LESSON_RECORD_ID"),
      updatable = false,
      nullable = false)
  private Lesson lesson;

  /** 学习人 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonDeserialize(using = LearnerDeserializer.class)
  @JoinColumn(
      name = "learner",
      foreignKey = @ForeignKey(name = "FK_LEARNER_LESSON_RECORD_ID"),
      updatable = false,
      nullable = false)
  private Learner learner;

  /** 章节学习进度 */
  @Column(name = "LESSON_LEARNING_PROGRESS")
  private int lessonLearningProgress;

  @Enumerated(EnumType.STRING)
  @Column(name = "LESSON_SCHEDULE", length = 20)
  private LessonScheduleType lessonScheduleType;
}
