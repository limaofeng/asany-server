package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.bean.Model;
import java.lang.reflect.Array;
import java.util.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilter.MatchType;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.util.Assert;

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

  public List<Object> findAll(List<PropertyFilter> filters) {
    return find(buildPropertyFilterCriterion(filters));
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

  protected Criterion[] buildPropertyFilterCriterion(List<PropertyFilter> filters) {
    List<Criterion> criterionList = new ArrayList<>();
    for (PropertyFilter filter : filters) {
      Object propertyValue = getPropertyValue(filter);
      Criterion criterion =
          buildPropertyFilterCriterion(
              filter.getPropertyName(), propertyValue, filter.getMatchType());
      if (criterion == null) {
        continue;
      }
      criterionList.add(criterion);
    }
    return criterionList.toArray(new Criterion[criterionList.size()]);
  }

  private Criterion conjunction(MatchType matchType, Criterion[] criteria) {
    if (criteria.length == 1) {
      return criteria[0];
    }
    return matchType == MatchType.AND ? Restrictions.and(criteria) : Restrictions.or(criteria);
  }

  private Object getPropertyValue(PropertyFilter filter) {
    return filter.getPropertyValue();
  }

  protected Criterion buildPropertyFilterCriterion(
      String propertyName, Object propertyValue, MatchType matchType) {
    if (matchType == MatchType.OR || matchType == MatchType.AND) {
      return conjunction(
          matchType,
          ((List<List<PropertyFilter>>) propertyValue)
              .stream()
                  .map(item -> conjunction(MatchType.AND, buildPropertyFilterCriterion(item)))
                  .toArray(Criterion[]::new));
    }
    Assert.hasText(propertyName, "propertyName不能为空");
    Criterion criterion = null;
    try {
      if (MatchType.EQ.equals(matchType)) {
        criterion = Restrictions.eq(propertyName, propertyValue);
      } else if (MatchType.CONTAINS.equals(matchType)) {
        criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
      } else if (MatchType.STARTS_WITH.equals(matchType)) {
        criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.START);
      } else if (MatchType.ENDS_WITH.equals(matchType)) {
        criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.END);
      } else if (MatchType.LTE.equals(matchType)) {
        criterion = Restrictions.le(propertyName, propertyValue);
      } else if (MatchType.LT.equals(matchType)) {
        criterion = Restrictions.lt(propertyName, propertyValue);
      } else if (MatchType.GTE.equals(matchType)) {
        criterion = Restrictions.ge(propertyName, propertyValue);
      } else if (MatchType.GT.equals(matchType)) {
        criterion = Restrictions.gt(propertyName, propertyValue);
      } else if (MatchType.IN.equals(matchType)) {
        if (Array.getLength(propertyValue) == 0) {
          return null;
        }
        criterion = Restrictions.in(propertyName, (Object[]) propertyValue);
      } else if (MatchType.NOT_IN.equals(matchType)) {
        if (Array.getLength(propertyValue) == 0) {
          return null;
        }
        criterion = Restrictions.not(Restrictions.in(propertyName, (Object[]) propertyValue));
      } else if (MatchType.NOT_EQUAL.equals(matchType)) {
        criterion = Restrictions.ne(propertyName, propertyValue);
      } else if (MatchType.NULL.equals(matchType)) {
        criterion = Restrictions.isNull(propertyName);
      } else if (MatchType.NOT_NULL.equals(matchType)) {
        criterion = Restrictions.isNotNull(propertyName);
      } else if (MatchType.EMPTY.equals(matchType)) {
        criterion = Restrictions.isEmpty(propertyName);
      } else if (MatchType.NOT_EMPTY.equals(matchType)) {
        criterion = Restrictions.isNotEmpty(propertyName);
      } else if (MatchType.BETWEEN.equals(matchType)) {
        criterion =
            Restrictions.between(
                propertyName, Array.get(propertyValue, 0), Array.get(propertyValue, 1));
      }
    } catch (Exception e) {
      throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
    }
    return criterion;
  }
}
