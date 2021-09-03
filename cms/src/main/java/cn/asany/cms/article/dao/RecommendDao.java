package cn.asany.cms.article.dao;

import cn.asany.cms.article.bean.Recommend;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 接口
 *
 * @author ChenWenJie
 * @date 2020/10/22 11:16 上午
 */
@Repository
public interface RecommendDao extends JpaRepository<Recommend, Long> {}
