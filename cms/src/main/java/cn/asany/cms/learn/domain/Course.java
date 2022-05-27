package cn.asany.cms.learn.domain;

import cn.asany.cms.learn.domain.databind.LearnerScopeDeserializer;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 课程
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/8/28 4:39 下午
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_COURSE")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Course extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "cms_course_gen")
  @TableGenerator(
      name = "cms_course_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_course:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 封面 */
  @Column(name = "COVER", length = 500)
  @Convert(converter = FileObjectConverter.class)
  private FileObject cover;

  /** 发布人 */
  @Column(
      name = "EMPLOYEE" /*, foreignKey = @ForeignKey(name = "FK_COURSE_EMPLOYEE_EMP_ID")*/,
      updatable = false,
      nullable = false)
  private Long publishUser;

  /** 发布日期 */
  @Column(name = "PUBLISH_DATE", length = 200)
  private String publishDate;
  /** 学习范围 */
  @JsonDeserialize(using = LearnerScopeDeserializer.class)
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<LearnerScope> learnerScope;
  /** 简介 */
  @Lob
  @Column(name = "INTRODUCTION")
  private String introduction;

  /** 学习时长 */
  @Column(name = "DURATION")
  private float duration;

  /** 学习人 */
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Learner> learners;
  /** 章节 */
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<CourseSection> sections;
  /** 学习内容 */
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Lesson> lessons;

  /** 课程分类ID */
  @Column(name = "TYPE_ID")
  private String type;

  /** 是否置顶 */
  @Column(name = "top", columnDefinition = "TINYINT default 0", nullable = false)
  private Boolean top;

  /** 消息提醒类型 */
  @Column(name = "NOTIFICATION_TYPE")
  private String notificationType;

  /** 互动控制 */
  @Column(name = "CONTROL_TYPE")
  private String controlType;
}
