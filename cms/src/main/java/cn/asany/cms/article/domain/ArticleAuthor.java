package cn.asany.cms.article.domain;

import cn.asany.organization.employee.domain.Employee;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
