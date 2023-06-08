package cn.asany.cms.article.graphql;

import cn.asany.cms.article.domain.enums.CommentTargetType;
import cn.asany.cms.article.graphql.input.CommentWhereInput;
import cn.asany.cms.article.graphql.type.CommentConnection;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
      CommentWhereInput where,
      int page,
      int pageSize,
      Sort orderBy) {
    PropertyFilter filter = ObjectUtil.defaultValue(where, new CommentWhereInput()).toFilter();
    filter.equal("targetType", targetType);
    filter.equal("targetId", targetId);
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(this.commentService.findPage(pageable, filter), CommentConnection.class);
  }
}
