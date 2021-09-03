package cn.asany.cms.learn.bean;

import cn.asany.cms.learn.bean.databind.CourseDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 课程下具体的学习人（必修）
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_COURSE_LEARNER_SCOPE")
public class LearnerScope extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "cms_course_learner_scope_gen")
  @TableGenerator(
      name = "cms_course_learner_scope_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_course_learner_scope:id",
      valueColumnName = "gen_value")
  private Long id;

  /** 课程 */
  @ManyToOne
  @JsonDeserialize(using = CourseDeserializer.class)
  @JoinColumn(
      name = "COURSE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "CMS_COURSE_LEARNER_SCOPE_CID"))
  private Course course;

  /** 具体学习人 */
  @Column(name = "scope")
  private String scope;
}
