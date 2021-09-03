package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Comment;
import cn.asany.cms.article.bean.enums.CommentTargetType;
import cn.asany.cms.article.graphql.inputs.CommentFilter;
import cn.asany.cms.article.graphql.types.CommentConnection;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private CommentService commentService;

  public CommentConnection comments(
      CommentTargetType targetType,
      String targetId,
      CommentFilter filter,
      int page,
      int pageSize,
      OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new CommentFilter()).getBuilder();
    builder.equal("targetType", targetType);
    builder.equal("targetId", targetId);
    if (orderBy == null) {
      orderBy = OrderBy.desc("createdAt");
    }
    Pager<Comment> pager = new Pager<>(page, pageSize, orderBy);
    return Kit.connection(
        this.commentService.findPager(pager, builder.build()), CommentConnection.class);
  }
}
