package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.library.bean.Oplog;
import cn.asany.ui.library.graphql.input.OplogFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class IconFilter extends QueryFilter<OplogFilter, Oplog> {

    private Long library;
    private List<Long> library_in;

}
