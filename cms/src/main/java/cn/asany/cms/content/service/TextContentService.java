package cn.asany.cms.content.service;

import cn.asany.cms.content.dao.TextContentDao;
import cn.asany.cms.content.domain.TextContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.domain.enums.TextContentType;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * 内容服务
 *
 * @author limaofeng
 */
@Service
public class TextContentService implements ArticleContentHandler<TextContent> {

  private final TextContentDao textContentDao;

  public TextContentService(TextContentDao textContentDao) {
    this.textContentDao = textContentDao;
  }

  @Override
  public boolean supports(ContentType type) {
    return type == ContentType.TEXT;
  }

  public TextContent save(TextContent articleBody) {
    return textContentDao.save(articleBody);
  }

  @Override
  public TextContent update(Long id, TextContent body) {
    body.setId(id);
    return textContentDao.update(body);
  }

  @Override
  public void delete(Long id) {
    this.textContentDao.deleteById(id);
  }

  @Override
  public TextContent parse(Map<String, Object> content) {
    Long id = (Long) content.get("id");
    TextContentType type = TextContentType.valueOf((String) content.get("type"));
    String text = (String) content.get("text");
    return TextContent.builder().id(id).type(type).text(text).build();
  }

  public TextContent update(TextContent content) {
    return textContentDao.update(content, true);
  }
}
