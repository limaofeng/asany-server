package cn.asany.security.oauth.graphql.inputs;

import cn.asany.security.oauth.bean.Application;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 应用过滤对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationFilter extends QueryFilter<ApplicationFilter, Application> {
}
