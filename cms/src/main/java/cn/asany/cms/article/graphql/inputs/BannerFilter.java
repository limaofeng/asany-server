package cn.asany.cms.article.graphql.inputs;

import cn.asany.cms.article.bean.Banner;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * Banner
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BannerFilter extends QueryFilter<BannerFilter, Banner> {}
