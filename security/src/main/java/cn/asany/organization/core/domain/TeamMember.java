package cn.asany.organization.core.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 团队成员
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_TEAM_MEMBER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TeamMember extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 职位名称 */
  @Column(name = "TITLE", length = 50)
  private String title;
  /** 头像 */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "avatar", precision = 500)
  private FileObject avatar;
  /** 简介 */
  @Column(name = "INTRODUCTION", length = 500)
  private String introduction;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer sort;
  /** 所属团队 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TEAM_ID",
      foreignKey = @ForeignKey(name = "FK_TEAM_MEMBER_TEAM"),
      updatable = false,
      nullable = false)
  private Team team;
  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_TEAM_MEMBER_ORG"),
      updatable = false,
      nullable = false)
  private Organization organization;
}
