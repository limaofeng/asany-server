package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.bean.ArticleTag;
import cn.asany.cms.article.converter.ArticleChannelConverter;
import cn.asany.cms.article.converter.ArticleConverter;
import cn.asany.cms.article.graphql.input.ArticleChannelInput;
import cn.asany.cms.article.graphql.input.ArticleInput;
import cn.asany.cms.article.graphql.input.ArticleTagInput;
import cn.asany.cms.article.service.ArticleChannelService;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.article.service.ArticleTagService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-26 17:06
 */
@Component
public class ArticleGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private ArticleChannelConverter articleChannelConverter;
  @Autowired private ArticleConverter articleConverter;
  @Autowired private ArticleService articleService;
  @Autowired private ArticleTagService articleTagService;
  @Autowired private ArticleChannelService articleChannelService;
  @Autowired protected Environment environment;
  /**
   * 保存文章
   *
   * @param input
   * @return
   */
  public Article createArticle(ArticleInput input) {
    return articleService.save(articleConverter.toArticle(input), input.getPermissions());
  }

  /**
   * 修改文章
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  public Article updateArticle(Long id, Boolean merge, ArticleInput input) {
    return articleService.update(articleConverter.toArticle(input), merge, id);
  }

  /**
   * 删除文章
   *
   * @param id
   * @return
   */
  public Boolean removeArticle(Long id) {
    articleService.deleteArticle(id);
    return true;
  }

  public Boolean batchRemoveArticle(List<Long> ids) {
    Long[] longs = new Long[ids.size()];
    articleService.deleteArticle(ids.toArray(longs));
    return true;
  }
  /**
   * 发布文章
   *
   * @param id
   * @return
   */
  public Boolean publishArticle(Long id) {
    articleService.publish(id);
    return true;
  }

  /**
   * 新增栏目
   *
   * @param input
   * @return
   */
  public ArticleChannel createChannel(ArticleChannelInput input) {
    ArticleChannel channel = articleChannelConverter.toChannel(input);
    Long parent = input.getParent();
    if (parent != null) {
      ArticleChannel articleChannel = articleChannelService.findOne(parent);
      channel.setParent(articleChannel);
    }
    channel = articleChannelService.save(channel);
    // 保存权限
    //    if (input.getPermissions() != null) {
    //      channel1.setPermissions(
    //          securityGrpcInvoke.updateGrantPermissions(
    //              "ArticleChannel", channel1.getId(), input.getPermissions()));
    //    }
    return channel;
  }

  /**
   * 更新栏目
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  public ArticleChannel updateChannel(Long id, Boolean merge, ArticleChannelInput input) {
    ArticleChannel channel = articleChannelConverter.toChannel(input);
    channel = articleChannelService.update(id, merge, channel);
    // 保存权限
    //    if (input.getPermissions() != null) {
    //      channel1.setPermissions(
    //          securityGrpcInvoke.updateGrantPermissions(
    //              "ArticleChannel", channel1.getId(), input.getPermissions()));
    //    }
    return channel;
  }

  /**
   * 删除栏目
   *
   * @param id
   * @return
   */
  public Boolean removeChannel(Long id) {
    return articleTagService.delete(id);
  }

  /**
   * 添加标签
   *
   * @param input
   * @return
   */
  public ArticleTag createArticleTag(ArticleTagInput input) {
    ArticleTag articleTag = articleChannelConverter.toArticle(input);
    return articleTagService.save(articleTag);
  }
  /**
   * 更新标签
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  public ArticleTag updateArticleTag(Long id, Boolean merge, ArticleTagInput input) {
    ArticleTag channel = articleChannelConverter.toArticle(input);
    return articleTagService.update(id, merge, channel);
  }

  /**
   * 删除标签
   *
   * @param id
   * @return
   */
  public Boolean removeArticleTag(Long id) {
    return articleTagService.delete(id);
  }
}
