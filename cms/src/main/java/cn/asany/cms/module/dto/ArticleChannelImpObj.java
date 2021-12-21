package cn.asany.cms.module.dto;

import java.util.List;
import lombok.Data;

/**
 * ArticleChannel DTO
 *
 * @author limaofeng
 */
@Data
public class ArticleChannelImpObj {
  private String icon;
  private String name;
  private String description;
  private String slug;
  private List<ArticleChannelImpObj> children;

  private List<ArticleImpObj> posts;
}
