package cn.asany.security.oauth.graphql.type;

import cn.asany.security.oauth.bean.OAuthApplication;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

import java.util.List;

/**
 * 应用分页对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ApplicationConnection extends BaseConnection<ApplicationConnection.ApplicationEdge> {

    private List<ApplicationEdge> edges;

    @Data
    public static class ApplicationEdge implements Edge<OAuthApplication> {
        private String cursor;
        private OAuthApplication node;

    }
}