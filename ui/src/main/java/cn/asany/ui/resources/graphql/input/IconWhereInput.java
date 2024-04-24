package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.library.domain.Oplog;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 图标查询过滤
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IconWhereInput extends WhereInput<IconWhereInput, Oplog> {

  @JsonProperty("id_in")
  public void setIds(List<Long> ids) {
    this.filter.in("resourceId", ids);
  }

  public void setLibrary(Long library) {
    this.filter.equal("library.id", library);
  }

  public void setLibrary_in(List<Long> ids) {
    this.filter.in("library.id", ids);
  }
}
