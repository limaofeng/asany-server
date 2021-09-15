package cn.asany.cms.article.dto;

import cn.asany.cms.article.bean.ArticleChannel;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CmsSetup {
  private List<ArticleChannel> channels;
}
