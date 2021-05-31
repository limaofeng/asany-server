package cn.asany.security.oauth.graphql.types;

import lombok.Data;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

import java.util.List;

/**
 * 应用分页对象
 *
 * @author limaofeng
 */
@Data
public class ApplicationConnection extends BaseConnection<ApplicationConnection.ApplicationEdge> {

    private List<ApplicationEdge> edges;

    @Data
    public static class ApplicationEdge implements Edge<Application> {
        private String cursor;
        private Application node;

    }
}