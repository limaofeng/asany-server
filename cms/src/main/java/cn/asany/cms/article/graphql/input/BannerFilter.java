package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.Banner;
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
