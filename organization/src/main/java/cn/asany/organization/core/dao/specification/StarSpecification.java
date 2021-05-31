package cn.asany.organization.core.dao.specification;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019/9/25 6:01 下午
 */
public class StarSpecification implements Specification {
    private Long stargazer;
    private String starType;

    public StarSpecification(Long stargazer, String starType) {
        this.stargazer = stargazer;
        this.starType = starType;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
        return new HQLPredicate(builder, this.stargazer, this.starType);
    }

    static class HQLPredicate extends AbstractSimplePredicate {
        private Long stargazer;
        private String starType;

        public HQLPredicate(CriteriaBuilder builder, Long stargazer, String starType) {
            super((CriteriaBuilderImpl) builder);
            this.stargazer = stargazer;
            this.starType = starType;
        }

        @Override
        public String render(boolean isNegated, RenderingContext renderingContext) {
            String hql = "SELECT galaxy FROM Star s WHERE s.stargazer = " + stargazer + " and s.starType = '" + starType + "'";
            return "generatedAlias0.id in (" + hql + ")";
        }

        /**
         * 功能：暂不支持该功能
         *
         * @param registry 数据参数
         */
        @Override
        public void registerParameters(ParameterRegistry registry) {

        }
    }
}

