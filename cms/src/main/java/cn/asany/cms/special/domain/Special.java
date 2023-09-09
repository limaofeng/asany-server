package cn.asany.cms.special.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import javax.tools.FileObject;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 专栏／专题 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_SPECIAL")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "subscribers", "articles"})
public class Special extends BaseBusEntity {
  /** 专题编码 */
  @Id
  @Column(name = "ID", length = 50, nullable = false, updatable = false)
  private String id;

  /** 专题名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "SUMMARY", length = 1000)
  private String summary;

  /** 封面 */
  @Column(name = "COVER", length = 500)
  @Type(type = "file")
  private FileObject cover;

  /** 期刊号 */
  @Column(name = "ISSN", length = 50)
  private String issn;

  /** 发布日期 */
  @Column(name = "PUBLISH_DATE")
  private Date publishDate;

  /** 专栏简介 */
  @Lob
  @Column(name = "INTRODUCTION")
  private String introduction;

  /** 订阅人数 */
  @Column(name = "SUBSCRIBER_COUNT")
  private Long subscriberCount;

  /** 订阅者 */
  @OneToMany(
      mappedBy = "special",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<Subscriber> subscribers;

  /** 专栏文章 */
  @OneToMany(
      mappedBy = "special",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<SpecialArticle> articles;
}
