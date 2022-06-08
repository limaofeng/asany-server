package cn.asany.cms.article.domain;

import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@RequiredArgsConstructor
@Entity
@Table(
    name = "CMS_ARTICLE_CHANNEL_ITEM",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_ARTICLE_CHANNEL_RELATIONSHIP",
            columnNames = {"CHANNEL_ID", "ARTICLE_ID"}))
public class ArticleChannelRelationship extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(targetEntity = ArticleChannel.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CHANNEL_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ARTICLE_CHANNEL_RELATIONSHIP_CID"))
  @ToString.Exclude
  private ArticleChannel channel;

  @ManyToOne(targetEntity = ArticleChannel.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ARTICLE_CHANNEL_RELATIONSHIP_AID"))
  @ToString.Exclude
  private Article article;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ArticleChannelRelationship that = (ArticleChannelRelationship) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
