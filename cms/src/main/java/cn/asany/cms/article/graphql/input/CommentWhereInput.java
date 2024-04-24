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
