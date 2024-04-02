package cn.asany.system.domain;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "SYS_SHORT_LINK_META_DATA",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME", "SHORT_LINK_ID"})})
public class ShortLinkMetaData extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  @Column(name = "VALUE", length = 2048)
  private String value;

  @ManyToOne
  @JoinColumn(
      name = "SHORT_LINK_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_SHORT_LINK_META_DATA_SHORT_LINK_ID"))
  private ShortLink shortLink;
}
