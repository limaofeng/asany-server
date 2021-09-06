package cn.asany.cms.article.bean;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文本内容
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_CONTENT_HTML")
public class HtmlContent extends BaseBusEntity implements Content {
  public static final String TYPE_KEY = "HTML";

  @Id
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 内容 */
  @Column(name = "CONTENT", columnDefinition = "mediumtext")
  private String text;
}
