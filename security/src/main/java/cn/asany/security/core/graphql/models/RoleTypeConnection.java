package cn.asany.security.core.graphql.models;

import cn.asany.security.core.bean.RoleType;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

import java.util.List;

/**
 * @author: guoyong
 * @description: 角色分类接口
 * @create: 2020/6/9 15:47
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RoleTypeConnection extends BaseConnection<RoleTypeConnection.RoleTypeEdge> {

    private List<RoleTypeEdge> edges;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleTypeEdge implements Edge<RoleType> {
        private String cursor;
        private RoleType node;
    }
}