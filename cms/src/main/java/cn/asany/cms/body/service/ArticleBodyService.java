package cn.asany.cms.body.service;

import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.article.domain.enums.ArticleBodyType;
import cn.asany.cms.body.domain.Content;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyService {

  public ArticleBody convert(String body, String storeTemplate) {
    if (ArticleBodyType.valueOf((storeTemplate)) == ArticleBodyType.classic) {
      return JSON.deserialize(body, Content.class);
    }
    throw new IllegalStateException("Unexpected value: " + body);
  }
}
