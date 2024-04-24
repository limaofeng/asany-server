package cn.asany.organization.core.domain;

import cn.asany.base.usertype.FileUserType;
import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

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
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 职位名称 */
  @Column(name = "TITLE", length = 50)
  private String title;

  /** 头像 */
  @Type(FileUserType.class)
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
