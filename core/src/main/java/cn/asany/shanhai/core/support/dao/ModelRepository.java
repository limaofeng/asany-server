package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.bean.Model;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.util.Assert;

import java.util.*;

public class ModelRepository {
    protected Model model;
    protected String entityName;
    protected ModelResultTransformer resultTransformer;
    private OgnlUtil ognlUtil = OgnlUtil.getInstance();

    public ModelRepository(Model model) {
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

    public void delete(Long id) {
        Object entity = findById(id);
        if (entity == null) {
            return;
        }
        getSession().delete(entityName, entity);
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
        // TODO: criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.setResultTransformer(this.resultTransformer);
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

    public List<Object> findAll() {
        Criteria criteria = distinct(createCriteria(new Criterion[0]));
        return criteria.list();
    }

    public Object update(Long id, Object input) {
        ognlUtil.setValue("id", input, id);
        Object source = findById(id);
        if (source == null) {
            return null;
        }
        for (Map.Entry<String, Object> entry : ((Map<String, Object>) input).entrySet()) {
            ognlUtil.setValue(entry.getKey(), source, entry.getValue());
        }
        getSession().update(entityName, source);
        return source;
    }
}
