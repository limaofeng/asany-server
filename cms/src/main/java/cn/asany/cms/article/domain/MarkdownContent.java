package cn.asany.cms.article.domain;

import cn.asany.cms.article.domain.enums.ArticleContentType;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_CONTENT_MARKDOWN")
public class MarkdownContent extends BaseBusEntity implements Content {
  public static final String TYPE_KEY = "MARKDOWN";

  @Id
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Override
  public ArticleContentType getType() {
    return ArticleContentType.MARKDOWN;
  }
}
