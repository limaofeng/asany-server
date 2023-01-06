package cn.asany.website.data.domain;

import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.nuwa.app.domain.Application;
import cn.asany.organization.core.domain.Organization;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 网站 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "WEBSITE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Website extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;
  /** 网站 LOGO */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "LOGO", precision = 500)
  private FileObject logo;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;
  /** 网站绑定的栏目 */
  @ManyToOne(targetEntity = ArticleCategory.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_CHANNEL_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_WEBSITE_ARTICLE_CHANNEL_ID"))
  @ToString.Exclude
  private ArticleCategory channel;
  /** 对应的应用 */
  @ManyToOne(targetEntity = Application.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "APPLICATION_ID", foreignKey = @ForeignKey(name = "FK_WEBSITE_APPLICATION_ID"))
  @ToString.Exclude
  private Application application;
  /** 所属组织 */
  @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_WEBSITE_ORGANIZATION_ID"))
  @ToString.Exclude
  private Organization organization;
  /** 存储器 */
  @Column(name = "STORAGE_ID", length = 500, nullable = false)
  private String storage;
}
