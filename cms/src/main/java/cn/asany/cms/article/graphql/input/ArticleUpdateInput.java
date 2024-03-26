package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.databind.ArticleBodySourceConverter;
import cn.asany.cms.article.domain.enums.ArticleStatus;
import cn.asany.cms.article.domain.enums.ArticleType;
import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 文章新增对象
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class ArticleUpdateInput {
  private String title;
  private ArticleType type;
  private String url;
  private ArticleStatus status;

  @JsonDeserialize(converter = ArticleBodySourceConverter.class)
  private String content;

  private String summary;
  private FileObject image;
  private List<Long> tags;
  private List<String> features;
  private String author;
  private String organization;
  private Date publishedAt;
  private List<FileObject> attachments;
  private List<PermissionInput> permissions;
  private List<String> access;
  private Long category;
  private Boolean validity;
  private Date validityStartDate;
  private Date validityEndDate;
  private List<ArticleMetafieldInput> metafields;
}
