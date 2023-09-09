package cn.asany.cms.learn.domain;

import cn.asany.cms.learn.domain.databind.CourseDeserializer;
import cn.asany.cms.learn.domain.enums.LearnerType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

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
