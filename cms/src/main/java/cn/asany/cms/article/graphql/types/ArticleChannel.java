package cn.asany.cms.article.graphql.types;

import cn.asany.cms.article.bean.MetaData;
import cn.asany.cms.permission.bean.Permission;
import cn.asany.storage.api.FileObject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 文章频道
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-08-15 09:07
 */
@Data
public class ArticleChannel implements Serializable {

  private Long id;
  /** 编码 */
  private String url;
  /** 路径 */
  private String path;
  /** 名称 */
  private String name;
  /** 封面 */
  private FileObject cover;
  /** 描述 */
  private String description;
  /** 排序字段 */
  private Integer sort;
  /** SEO 优化字段 */
  private MetaData meta;
  /** 组织机构 */
  private Long organization;
  /** 上级栏目 */
  private ArticleChannel parent;
  /** 创建人 */
  private String creator;
  /** 创建时间 */
  private Date createdAt;
  /** 最后修改人 */
  private String modifier;
  /** 最后修改时间 */
  private Date updatedAt;
  /** 下级栏目 */
  private List<ArticleChannel> children;

  private List<Permission> permissions;
}
