package cn.asany.security.core.graphql.models;

import cn.asany.security.core.bean.PermissionType;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

import java.util.List;

/**
 * @author: guoyong
 * @description: 权限分类接口
 * @create: 2020/6/9 15:47
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PermissionTypeConnection extends BaseConnection<PermissionTypeConnection.PermissionTypeEdge> {

    private List<PermissionTypeEdge> edges;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PermissionTypeEdge implements Edge<PermissionType> {
        private String cursor;
        private PermissionType node;
    }
}