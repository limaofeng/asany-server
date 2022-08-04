package cn.asany.cms.learn.domain;

import cn.asany.cms.learn.domain.enums.LessonType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 章节(废弃)
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_COURSE_SECTION")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CourseSection {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "cms_course_section_gen")
  @TableGenerator(
      name = "cms_course_section_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_course_section:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 章节内容 */
  @Column(name = "content")
  private String content;

  /** 章节类型 */
  @Column(name = "lesson_type")
  private LessonType lessonType;

  /** 章节关联的内容（每小节） */
  @OneToMany(
      mappedBy = "courseSection",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Lesson> lessons;

  /** 章节所属关联的课程 */
  @ManyToOne
  @JoinColumn(
      name = "COURSE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "CMS_COURSE_COURSE_SECTION_CID"))
  private Course course;
}
