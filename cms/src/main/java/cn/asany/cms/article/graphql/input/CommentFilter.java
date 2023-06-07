package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.Comment;
import cn.asany.cms.article.service.CommentService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.graphql.inputs.QueryFilter;

public class CommentFilter extends QueryFilter<CommentFilter, Comment> {

  @JsonProperty(value = "forComment_isEmpty")
  public void setForCommentIsEmpty(Boolean isEmpty) {
    if (isEmpty) {
      this.builder.isNull("forComment.id");
    }
  }

  @JsonProperty(value = "forComment")
  public void setForComment(Long forComment) {
    if (Long.valueOf(0).equals(forComment)) {
      this.builder.isNull("forComment.id");
    } else {
      this.builder.equal("forComment.id", forComment);
    }
  }

  @JsonProperty(value = "id_startsWith")
  public void setIdStartsWith(Long id) {
    Comment comment = SpringBeanUtils.getBeanByType(CommentService.class).get(id);
    this.builder.contains("path", comment.getPath() + "%");
  }

}
