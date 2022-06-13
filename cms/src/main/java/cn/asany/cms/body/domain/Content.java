package cn.asany.cms.body.domain;

import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.article.domain.enums.ArticleBodyType;
import cn.asany.cms.body.domain.enums.ContentType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.Objects;

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
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private ContentType type;

  /** 内容 */
  @Column(name = "`TEXT`", columnDefinition = "mediumtext")
  private String text;

  @Override
  public ArticleBodyType type() {
    return ArticleBodyType.valueOf(TYPE_KEY);
  }

  @Override
  public String generateSummary() {
    return null;
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
