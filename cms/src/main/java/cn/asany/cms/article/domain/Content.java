package cn.asany.cms.article.domain;

import cn.asany.cms.article.domain.enums.ArticleContentType;
import java.io.Serializable;

/**
 * 内容表
 *
 * @author 李茂峰
 */
public interface Content extends Serializable {

  Long getId();

  void setId(Long id);

  ArticleContentType getType();
}
