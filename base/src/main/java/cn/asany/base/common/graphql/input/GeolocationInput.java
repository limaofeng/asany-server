package cn.asany.base.common.graphql.input;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class GeolocationInput {
  private BigDecimal longitude;
  private BigDecimal latitude;
}
