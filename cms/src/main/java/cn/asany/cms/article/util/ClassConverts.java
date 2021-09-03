package cn.asany.cms.article.util;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.enums.ArticleType;
import cn.asany.cms.article.bean.enums.ContentType;
import cn.asany.cms.article.graphql.type.*;
import java.util.HashMap;
import java.util.Map;
import org.jfantasy.framework.util.common.ClassUtil;

public class ClassConverts {

  private static Map<String, Class<? extends IContent>> contentMap;

  static {
    contentMap = new HashMap();
    contentMap.put(ArticleType.picture + "|" + ContentType.json, PictureContent.class);
    contentMap.put(ArticleType.link + "|" + ContentType.link, LinkContent.class);
    contentMap.put(ArticleType.text + "|" + ContentType.html, HtmlContent.class);
    contentMap.put(ArticleType.video + "|" + ContentType.file, VideoContent.class);
  }

  /**
   * 功能：根据字段类型转换成对应的类型对象
   *
   * @return ModelField
   */
  public static IContent toContent(
      ArticleType articleType, ContentType contentType, Content source) {
    Class<? extends IContent> contentClass =
        contentMap.get(articleType.name() + "|" + contentType.name());
    IContent content =
        ClassUtil.newInstance(contentClass, new Class[] {Content.class}, new Object[] {source});
    content.setId(source.getId());
    content.setType(source.getType());
    return content;
  }
}
