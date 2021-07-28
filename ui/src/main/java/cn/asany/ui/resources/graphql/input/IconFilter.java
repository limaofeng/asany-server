package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.library.bean.Oplog;
import cn.asany.ui.library.graphql.input.OplogFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class IconFilter extends QueryFilter<OplogFilter, Oplog> {

    @JsonProperty("id_in")
    public void setIds(List<Long> ids) {
        this.builder.in("resourceId", ids);
    }

    public void setLibrary(Long library) {
        this.builder.equal("library.id", library);
    }

    public void setLibrary_in(List<Long> ids) {
        this.builder.in("library.id", ids);
    }
}
