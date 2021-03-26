package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.bean.Model;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelRepository {
    protected Model model;
    protected String entityName;
    protected ModelResultTransformer resultTransformer;

    public ModelRepository(Model model, ModelSessionFactory sessionFactory) {
        this.model = model;
        this.entityName = model.getCode();
        this.resultTransformer = new ModelResultTransformer(model.getFields());
    }

    protected SessionFactory getSessionFactory() {
        return ManualTransactionManager.getCurrentSessionFactory();
    }


    protected Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    public Object findById(Long id) {
        Criteria criteria = createCriteria(Restrictions.eq("id", id));
        criteria.setResultTransformer(this.resultTransformer);
        return criteria.uniqueResult();
    }

    public Object save(Object input) {
        Object pk = getSession().save(entityName, input);
        return findById((Long) pk);
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

    protected Criteria createCriteria(Criterion... criterions) {
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
