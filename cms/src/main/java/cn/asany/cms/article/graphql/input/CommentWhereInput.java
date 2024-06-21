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
package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.Comment;
import cn.asany.cms.article.service.CommentService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 评论筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentWhereInput extends WhereInput<CommentWhereInput, Comment> {

  @JsonProperty(value = "forComment_isEmpty")
  public void setForCommentIsEmpty(Boolean isEmpty) {
    if (isEmpty) {
      this.filter.isNull("forComment.id");
    }
  }

  @JsonProperty(value = "forComment")
  public void setForComment(Long forComment) {
    if (Long.valueOf(0).equals(forComment)) {
      this.filter.isNull("forComment.id");
    } else {
      this.filter.equal("forComment.id", forComment);
    }
  }

  @JsonProperty(value = "id_startsWith")
  public void setIdStartsWith(Long id) {
    Comment comment = SpringBeanUtils.getBeanByType(CommentService.class).get(id);
    this.filter.contains("path", comment.getPath() + "%");
  }
}
