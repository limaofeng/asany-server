package cn.asany.cms.article.graphql.inputs;

import cn.asany.cms.article.bean.MetaData;
import cn.asany.cms.article.bean.enums.ArticleCategory;
import cn.asany.cms.article.bean.enums.ArticleStatus;
import cn.asany.cms.article.bean.enums.ArticleType;
import cn.asany.storage.api.FileObject;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 文章新增对象
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-06-26 17:09
 */
@Data
public class ArticleInput {
  private String title;
  private ArticleType type;
  private String url;
  private ArticleStatus status;
  private ContentInput content;
  private String summary;
  private FileObject cover;
  private List<Long> channels;
  private List<Long> tags;
  private List<Long> recommend;
  private String author;
  private String organization;
  private Date publishedAt;
  private List<FileObject> attachments;
  private ArticleCategory category;
  private List<PermissionInput> permissions;
  private Boolean validity;
  private Date validityStartDate;
  private Date validityEndDate;
  private MetaData meta;
}
