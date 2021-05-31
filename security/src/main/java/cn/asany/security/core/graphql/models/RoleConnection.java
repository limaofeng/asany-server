package cn.asany.security.core.graphql.models;

import cn.asany.security.core.bean.Role;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

import java.util.List;

/**
 * @author: guoyong
 * @description: 角色查询接口
 * @create: 2020/6/9 15:47
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RoleConnection extends BaseConnection<RoleConnection.RoleEdge> {

    private List<RoleEdge> edges;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleEdge implements Edge<Role> {
        private String cursor;
        private Role node;
    }
}