package cn.asany.cms.body.service;

import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.article.domain.enums.ArticleBodyType;
import cn.asany.cms.body.domain.Content;
import java.util.List;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyService {

  private final List<ArticleBodyHandler> handlers;

  public ArticleBodyService(List<ArticleBodyHandler> handlers) {
    this.handlers = handlers;
  }

  public ArticleBody convert(String body, String storeTemplate) {
    if (ArticleBodyType.valueOf((storeTemplate)) == ArticleBodyType.classic) {
      return JSON.deserialize(body, Content.class);
    }
    throw new IllegalStateException("Unexpected value: " + body);
  }

  public ArticleBodyHandler getBodyHandler(ArticleBodyType type) {
    return this.handlers.stream()
        .filter(item -> item.supports(type))
        .findFirst()
        .orElseThrow(() -> new ValidationException("100", type.name() + "没有对应的处理逻辑 "));
  }

  public ArticleBody save(ArticleBody body) {
    ArticleBodyHandler handler = getBodyHandler(ArticleBodyType.valueOf(body.bodyType()));
    return handler.save(body);
  }

  public ArticleBody update(Long id, ArticleBody body) {
    ArticleBodyHandler handler = getBodyHandler(ArticleBodyType.valueOf(body.bodyType()));
    return handler.update(id, body);
  }

  public void deleteById(Long id, ArticleBodyType bodyType) {
    ArticleBodyHandler handler = getBodyHandler(bodyType);
    handler.delete(id);
  }
}
