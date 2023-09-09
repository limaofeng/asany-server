package cn.asany.website.landing.domain;

import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 海报 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "WEBSITE_LANDING_POSTER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LandingPoster extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 门店名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;
  /** 海报背景图片 */
  @Type(type = "file")
  @Column(name = "BACKGROUND", precision = 500)
  private FileObject background;
  /** 海报背景音乐 */
  @Type(type = "file")
  @Column(name = "MUSIC", precision = 500)
  private FileObject music;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    LandingPoster poster = (LandingPoster) o;
    return id != null && Objects.equals(id, poster.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
