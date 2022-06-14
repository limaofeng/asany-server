package cn.asany.cms.body.domain;

import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.body.domain.enums.ContentType;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.htmlcleaner.TagNode;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.htmlcleaner.HtmlCleanerUtil;

/**
 * 文本内容
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "CMS_CONTENT")
public class Content extends BaseBusEntity implements ArticleBody {

  public static final String TYPE_KEY = "classic";

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private ContentType type;

  /** 内容 */
  @Column(name = "`TEXT`", columnDefinition = "mediumtext")
  private String text;

  @Override
  public String generateSummary() {
    if (type == ContentType.HTML) {
      TagNode node = HtmlCleanerUtil.htmlCleaner(text);
      String contentString = node.getText().toString().trim().replace("\n", "");
      return StringUtil.ellipsis(contentString, 190, "");
    }
    return StringUtil.ellipsis(text, 20, "");
  }

  @Override
  public String bodyType() {
    return TYPE_KEY;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Content content = (Content) o;
    return id != null && Objects.equals(id, content.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
