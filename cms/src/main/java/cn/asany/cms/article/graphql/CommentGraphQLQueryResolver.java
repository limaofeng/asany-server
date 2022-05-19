package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.enums.CommentTargetType;
import cn.asany.cms.article.graphql.input.CommentFilter;
import cn.asany.cms.article.graphql.type.CommentConnection;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CommentGraphQLQueryResolver implements GraphQLQueryResolver {

  private final CommentService commentService;

  public CommentGraphQLQueryResolver(CommentService commentService) {
    this.commentService = commentService;
  }

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
    Pageable pageable = PageRequest.of(page, pageSize, orderBy.toSort());
    return Kit.connection(
        this.commentService.findPage(pageable, builder.build()), CommentConnection.class);
  }
}
