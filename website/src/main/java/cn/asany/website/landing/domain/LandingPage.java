package cn.asany.website.landing.domain;

import cn.asany.base.common.domain.Metadata;
import cn.asany.website.landing.domain.enums.LandingPageStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NamedEntityGraph(
    name = "Graph.LandingPage.FetchDetails",
    attributeNodes = {
      @NamedAttributeNode(value = "poster", subgraph = "SubGraph.LandingPoster"),
      @NamedAttributeNode(value = "stores", subgraph = "SubGraph.LandingStore")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.LandingPoster",
          attributeNodes = {
            @NamedAttributeNode(value = "music"),
            @NamedAttributeNode(value = "background")
          }),
      @NamedSubgraph(
          name = "SubGraph.LandingStore",
          attributeNodes = {
            @NamedAttributeNode(value = "qrCode"),
            @NamedAttributeNode(value = "address"),
            @NamedAttributeNode(value = "location"),
          })
    })
@Entity
@Table(name = "WEBSITE_LANDING_PAGE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LandingPage extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private LandingPageStatus status;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "STARTS")
  private Date start;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "ENDS")
  private Date end;

  /** 海报 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "POSTER_ID", foreignKey = @ForeignKey(name = "FK_WEBSITE_LANDING_PAGE_POSTER"))
  @ToString.Exclude
  private LandingPoster poster;

  @ManyToMany(fetch = FetchType.LAZY)
  @OrderBy("id ASC")
  @JoinTable(
      name = "WEBSITE_LANDING_PAGE_STORE",
      joinColumns = {
        @JoinColumn(
            name = "PAGE_ID",
            foreignKey = @ForeignKey(name = "FK_WEBSITE_LANDING_PAGE_STORE_PID"))
      },
      inverseJoinColumns = {@JoinColumn(name = "STORE_ID")},
      foreignKey = @ForeignKey(name = "FK_WEBSITE_LANDING_PAGE_STORE_SID"))
  @ToString.Exclude
  private List<LandingStore> stores;

  /** 元数据 */
  @Embedded private Metadata metadata;
}
