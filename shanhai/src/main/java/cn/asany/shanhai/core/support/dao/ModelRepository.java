package cn.asany.shanhai.core.support.dao;

import cn.asany.shanhai.core.domain.Model;
import java.lang.reflect.Array;
import java.util.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilter.MatchType;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.util.Assert;

public class ModelRepository {
  private final Class<?> entityClass;
  protected final Model model;
  protected final String entityName;
  protected ModelResultTransformer resultTransformer;
  private final OgnlUtil ognlUtil = OgnlUtil.getInstance();

  public ModelRepository(Model model, Class<?> entityClass) {
    this.model = model;
    this.entityClass = entityClass;
    this.entityName = model.getCode();
    this.resultTransformer = new ModelResultTransformer(entityClass, model.getFields());
  }

  protected SessionFactory getSessionFactory() {
    return ManualTransactionManager.getCurrentSessionFactory();
  }

  protected Session getSession() {
    return getSessionFactory().getCurrentSession();
  }

  public <T> T findById(Long id) {
    Criteria criteria = createCriteria(Restrictions.eq("id", id));
    criteria.setResultTransformer(this.resultTransformer);
    return (T) criteria.uniqueResult();
  }

  public <T> T delete(Long id) {
    T entity = findById(id);
    if (entity == null) {
      return null;
    }
    getSession().delete(entityName, entity);
    return entity;
  }

  public <T> T delete(T entity) {
    getSession().delete(entityName, entity);
    return entity;
  }

  public <T> T save(T input) {
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
    criteria.setResultTransformer(this.resultTransformer);
    return criteria;
  }

  //  protected Criteria createCriteria(Criterion[] criterions, String... orderBys) {
  //    Criteria criteria = getSession().createCriteria(this.entityName);
  //    Set<String> alias = new HashSet<String>();
  //    for (Criterion c : criterions) {
  //      //            changePropertyName(criteria, alias, c);
  //      criteria.add(c);
  //    }
  //    //        for (String orderBy : orderBys) {
  //    //            createAlias(criteria, alias, orderBy);
  //    //        }
  //    criteria.setCacheable(true);
  //    criteria.setResultTransformer(this.resultTransformer);
  //    return criteria;
  //  }

  public <T> List<T> findAll() {
    Criteria criteria = distinct(createCriteria());
    return criteria.list();
  }

  public <T> List<T> findAll(List<PropertyFilter> filters) {
    return find(buildPropertyFilterCriterion(filters));
  }

  public <T> List<T> findAll(List<PropertyFilter> filters, int offset, int limit, OrderBy orderBy) {
    Criteria c = distinct(createCriteria(buildPropertyFilterCriterion(filters)));
    c.setFirstResult(offset);
    c.setMaxResults(limit);
    setOrderBy(c, orderBy);
    return c.list();
  }

  public <T> Page<T> findPage(Page<T> page, List<PropertyFilter> filters) {
    Criterion[] criterions = buildPropertyFilterCriterion(filters);
    return findPage(page, criterions);
  }

  /**
   * @param page 翻页对象
   * @param criterions 查询条件
   * @return pager
   */
  public <T> Page<T> findPage(Page<T> page, Criterion... criterions) {
    Criteria c = distinct(createCriteria(criterions));
    page.setTotalCount(countCriteriaResult(c));
    setPageParameter(c, page);
    page.reset(c.list());
    return page;
  }

  protected <T> Criteria setPageParameter(Criteria c, Page<T> page) {
    c.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
    c.setMaxResults(page.getPageSize());
    return setOrderBy(c, page.getOrderBy());
  }

  protected Criteria setOrderBy(Criteria c, OrderBy orderBy) {
    if (orderBy.isOrderBySeted()) {
      for (OrderBy order : orderBy.getOrders()) {
        if (order.getDirection() == OrderBy.Direction.ASC) {
          c.addOrder(Order.asc(order.getProperty()));
        } else {
          c.addOrder(Order.desc(order.getProperty()));
        }
      }
    }
    return c;
  }

  protected int countCriteriaResult(Criteria c) {
    if (!(c instanceof CriteriaImpl)) {
      throw new IgnoreException(" Criteria 不能 cast CriteriaImpl");
    }
    CriteriaImpl impl = CriteriaImpl.class.cast(c);
    Projection projection = impl.getProjection();
    ResultTransformer transformer = impl.getResultTransformer();
    List<CriteriaImpl.OrderEntry> orderEntries = null;
    try {
      orderEntries =
          (List<CriteriaImpl.OrderEntry>) ReflectionUtils.getFieldValue(impl, "orderEntries");
      ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList<CriteriaImpl.OrderEntry>());
    } catch (Exception e) {
      throw new IgnoreException("不可能抛出的异常:" + e.getMessage(), e);
    }
    int totalCount =
        Integer.valueOf(c.setProjection(Projections.rowCount()).uniqueResult().toString());
    c.setProjection(projection);
    if (projection == null) {
      c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    }
    if (transformer != null) {
      c.setResultTransformer(transformer);
    }
    try {
      ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
    } catch (Exception e) {
      throw new IgnoreException("不可能抛出的异常:" + e.getMessage(), e);
    }
    return totalCount;
  }

  public <T> T update(Long id, T input, boolean merge) {
    ognlUtil.setValue("id", input, id);
    T source = findById(id);
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
