package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.enums.ArticleContentType;

/**
 * 内容表
 *
 * @author 李茂峰
 */
public interface Content {

  Long getId();

  void setId(Long id);

  ArticleContentType getType();
}
