package cn.asany.cms.learn.domain;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
import cn.asany.cms.article.domain.Article;
import cn.asany.cms.learn.domain.enums.LessonType;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

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
