package cn.asany.security.core.graphql.input;

import cn.asany.security.core.domain.RoleSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 业务空间过滤
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessWhereInput extends WhereInput<BusinessWhereInput, RoleSpace> {}
