package cn.asany.shanhai.data.domain.toy;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfigParam implements Serializable {
  private String name;
  private String value;
}
