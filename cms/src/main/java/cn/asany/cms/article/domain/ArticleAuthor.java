package cn.asany.cms.article.domain;

import cn.asany.organization.employee.domain.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 文章作者
 *
 * @author limaofeng
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_ARTICLE_AUTHOR")
public class ArticleAuthor extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ARTICLE_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_AUTHOR_AID"))
  private Article article;

  /** 对应员工 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_AUTHOR_EID"))
  private Employee employee;
}
