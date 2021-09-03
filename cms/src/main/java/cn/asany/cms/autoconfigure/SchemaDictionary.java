package cn.asany.cms.autoconfigure;

import cn.asany.cms.article.graphql.type.HtmlContent;
import cn.asany.cms.article.graphql.type.LinkContent;
import cn.asany.cms.article.graphql.type.PictureContent;
import cn.asany.cms.article.graphql.type.VideoContent;
import graphql.kickstart.tools.SchemaParserDictionary;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;

public class SchemaDictionary implements SchemaParserDictionaryBuilder {
  @Override
  public void build(SchemaParserDictionary dictionary) {
    dictionary.add("HtmlContent", HtmlContent.class);
    dictionary.add("PictureContent", PictureContent.class);
    dictionary.add("VideoContent", VideoContent.class);
    dictionary.add("LinkContent", LinkContent.class);
  }
}
