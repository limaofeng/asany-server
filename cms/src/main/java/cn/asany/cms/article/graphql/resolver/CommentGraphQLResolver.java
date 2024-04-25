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
package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.Comment;
import cn.asany.cms.article.graphql.enums.CommentStarType;
import cn.asany.cms.article.graphql.input.CommentWhereInput;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class CommentGraphQLResolver implements GraphQLResolver<Comment> {

  @Autowired private CommentService commentService;

  public String user(Comment comment) {
    return comment.getUid();
  }

  public List<Comment> comments(Comment comment, CommentWhereInput where) {
    return commentService.findAll(
        ObjectUtil.defaultValue(where, new CommentWhereInput())
            .toFilter()
            .startsWith("path", comment.getPath())
            .notEqual("id", comment.getId()));
  }

  public Starrable starrable(Comment comment, CommentStarType starType) {
    //        .id(comment.getId().toString() + "/" + starType.getValue())
    //                .galaxy(comment.getId().toString())
    //                .starType(starType.getValue())
    return Starrable.builder().build();
  }
}
