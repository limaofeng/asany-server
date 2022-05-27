package cn.asany.base.common.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Geolocation {
  /** 经度 */
  @Column(name = "GEO_LONGITUDE", precision = 11, scale = 6)
  private BigDecimal longitude;
  /** 纬度 */
  @Column(name = "GEO_LATITUDE", precision = 11, scale = 6)
  private BigDecimal latitude;
}
