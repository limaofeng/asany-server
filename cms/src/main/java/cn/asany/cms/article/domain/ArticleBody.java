package cn.asany.cms.article.domain;

import java.io.Serializable;

/**
 * 内容表
 *
 * @author 李茂峰
 */
public interface ArticleBody extends Serializable {

  Long getId();

  void setId(Long id);

  String generateSummary();

  /**
   * 返回 Body Type
   *
   * @return string
   */
  String bodyType();
}
