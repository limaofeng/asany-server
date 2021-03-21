package cn.asany.shanhai.core.support.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelJpaRepository {
    protected ModelSessionFactory sessionFactory;
    protected String entityName;

    public ModelJpaRepository(String entityName, ModelSessionFactory sessionFactory) {
        this.entityName = entityName;
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public List findBy(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        return find(Restrictions.eq(propertyName, value));
    }

    public List find(Criterion... criterions) {
        return distinct(createCriteria(criterions)).list();
    }

    protected Criteria distinct(Criteria criteria) {
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    protected Criteria createCriteria(Criterion[] criterions, String... orderBys) {
        Criteria criteria = getSession().createCriteria(this.entityName);
        Set<String> alias = new HashSet<String>();
        for (Criterion c : criterions) {
//            changePropertyName(criteria, alias, c);
            criteria.add(c);
        }
//        for (String orderBy : orderBys) {
//            createAlias(criteria, alias, orderBy);
//        }
        criteria.setCacheable(true);
        return criteria;
    }

}
