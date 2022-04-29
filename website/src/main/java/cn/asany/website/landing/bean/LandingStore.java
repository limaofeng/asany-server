package cn.asany.website.landing.bean;

import cn.asany.base.common.bean.Address;
import cn.asany.base.common.bean.Geolocation;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "WEBSITE_LANDING_STORE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LandingStore extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 门店编码 */
  @Column(name = "CODE", length = 100, nullable = false)
  private String code;
  /** 门店名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;
  /** 门店位置 */
  @Embedded private Address address;
  /** 门店位置 (经纬坐标) */
  @Embedded private Geolocation location;
  /** 二维码 */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "QR_CODE", precision = 500)
  private FileObject qrCode;
  /** 门店负责人 */
  @Column(name = "LEADER", precision = 100)
  private String leader;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    LandingStore store = (LandingStore) o;
    return id != null && Objects.equals(id, store.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
