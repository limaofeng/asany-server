package cn.asany.cardhop.contacts.graphql.resolver;

import cn.asany.cardhop.contacts.domain.ContactGroup;
import cn.asany.cardhop.contacts.utils.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 联系人分组
 *
 * @author limaofeng
 */
@Component
public class ContactGroupGraphQLResolver implements GraphQLResolver<ContactGroup> {
  public String id(ContactGroup group) {
    return IdUtils.toKey(group.getBook().getId(), group.getNamespace(), group.getId());
  }

  public String parentId(ContactGroup group) {
    String[] pathIds = StringUtil.tokenizeToStringArray(group.getPath(), "/");
    if (pathIds.length <= 1) {
      return null;
    }
    return IdUtils.toKey(
        group.getBook().getId(), group.getNamespace(), Long.parseLong(pathIds[pathIds.length - 2]));
  }
}
