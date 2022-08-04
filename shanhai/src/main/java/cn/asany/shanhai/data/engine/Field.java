package cn.asany.shanhai.data.engine;

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
public class Field implements Serializable {
  private String id;
  private String name;
  private String type;
  private boolean primaryKey;

  public Field(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
