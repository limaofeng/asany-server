package cn.asany.security.core.graphql.inputs;

import lombok.Data;

import java.util.List;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-08-19 16:05
 */
@Data
public class PermissionInput {
    private String permission;
    private List<String> grants;
}
