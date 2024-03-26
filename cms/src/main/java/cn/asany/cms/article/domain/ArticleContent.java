package cn.asany.cms.article.domain;

import cn.asany.cms.content.domain.enums.ContentType;
import java.io.Serializable;

/**
 * 内容表
 *
 * @author 李茂峰
 */
public interface ArticleContent extends Serializable {

  Long getId();

  void setId(Long id);

  String generateSummary();

  ContentType getContentType();
}
