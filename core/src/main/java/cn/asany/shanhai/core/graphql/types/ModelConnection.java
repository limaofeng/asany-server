package cn.asany.shanhai.core.graphql.types;

import cn.asany.shanhai.core.bean.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

import java.util.List;

/**
 * Model 分页对象
 *
 * @author limaofeng
 */
@Data
public class ModelConnection extends BaseConnection<ModelConnection.ModelEdge> {
    private List<ModelEdge> edges;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelEdge implements Edge<Model> {
        private String cursor;
        private Model node;
    }
}
