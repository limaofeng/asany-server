package cn.asany.shanhai.data.bean.toy;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/1/14 12:24 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfigParam implements Serializable {
  private String name;
  private String value;
}
