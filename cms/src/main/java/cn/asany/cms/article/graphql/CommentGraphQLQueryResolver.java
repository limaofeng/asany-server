/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.cms.article.graphql;

import cn.asany.cms.article.domain.enums.CommentTargetType;
import cn.asany.cms.article.graphql.input.CommentWhereInput;
import cn.asany.cms.article.graphql.type.CommentConnection;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
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
