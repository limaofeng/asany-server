package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.enums.ContentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 内容表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:36
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_CONTENT")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Content extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "content_gen")
  @TableGenerator(
      name = "content_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_content:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 数据类型： json, html, markdown,file */
  @Column(name = "TYPE", length = 10)
  private ContentType type;
  /** 正文内容 */
  @Lob
  @Column(name = "CONTENT")
  private String text;
}
