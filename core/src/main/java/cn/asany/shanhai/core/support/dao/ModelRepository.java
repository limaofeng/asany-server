package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.bean.Model;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.DistinctResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.*;

public class ModelRepository {
    protected Model model;
    protected String entityName;

    public ModelRepository(Model model, ModelSessionFactory sessionFactory) {
        this.model = model;
        this.entityName = model.getCode();
    }

    protected SessionFactory getSessionFactory() {
        return ManualTransactionManager.getCurrentSessionFactory();
    }


    protected Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    public Object findById(Long id) {
        Criteria criteria = createCriteria(Restrictions.eq("id", id));
        criteria.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                return tuple[tuple.length - 1];
            }

            @Override
            public List transformList(List list) {
                List<Object> result = new ArrayList<Object>(list.size());
                for (Object entity : list) {
                    Timestamp createdAt = OgnlUtil.getInstance().getValue("createdAt", entity);
                    OgnlUtil.getInstance().setValue("createdAt", entity, new Date(createdAt.getTime()));
                    result.add(entity);
                }
                return result;
            }
        });
        return criteria.uniqueResult();
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
