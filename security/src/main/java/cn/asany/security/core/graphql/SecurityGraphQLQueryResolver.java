package cn.asany.security.core.graphql;

import cn.asany.security.core.bean.*;
import cn.asany.security.core.graphql.enums.ResultTypeScope;
import cn.asany.security.core.graphql.inputs.ContextInput;
import cn.asany.security.core.graphql.inputs.ScopeTypeAttribute;
import cn.asany.security.core.graphql.inputs.ScopeTypeResult;
import cn.asany.security.core.graphql.models.*;
import cn.asany.security.core.graphql.types.SecurityScopeObject;
import cn.asany.security.core.service.PermissionService;
import cn.asany.security.core.service.RoleScopeService;
import cn.asany.security.core.service.RoleService;
import cn.asany.security.core.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-04-01 15:17
 */
@Component
public class SecurityGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private RoleService roleService;
  @Autowired private UserService userService;
  //    @Autowired
  //    private SecurityService securityService;
  @Autowired private PermissionService permissionService;

  //    @Autowired
  //    private SendConfigureDao starTypeDao;
  //    @Autowired
  //    private CommonService commonService;
  //    @Autowired
  //    private AllCombinations allCombinations;
  @Autowired private ScopeTypeAttribute typeAttribute;
  //    @Autowired
  //    private GetObjectByID byID;

  @Autowired private RoleScopeService roleScopeService;

  public List<Role> roles(String organization, String scope) {
    return new ArrayList<>(); // this.roleService.getAllByOrg(organization,RoleScope.builder().id(scope).build());
  }

  /** 查询所有用户,带条件查询 */
  public UserConnection users(UserFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<User> pager = new Pager<>(page, pageSize, orderBy);
    PropertyFilterBuilder builder = ObjectUtil.defaultValue(filter, new UserFilter()).getBuilder();
    return Kit.connection(userService.findPager(pager, builder.build()), UserConnection.class);
  }

  /** 查询单个用户 */
  public User user(Long id) {
    return userService.findById(id);
  }

  public List<SecurityScopeObject> securityScopes(
      String organizationId, ContextInput contextInput, List<String> ids) {
    //        if (contextInput == null) {
    //            return this.securityService.getSecurityScopeObjects(organizationId, false,
    // ids.stream().map(id -> SecurityScope.newInstance(id)).collect(Collectors.toList()),
    // null,null);
    //        } else {
    //            return this.securityService.getSecurityScopeObjects(organizationId,
    // contextInput.getParsing(), ids.stream().map(id ->
    // SecurityScope.newInstance(id)).collect(Collectors.toList()), contextInput.getViewer(),null);
    //        }
    return new ArrayList<>();
  }

  /**
   * 采用jQuery格式表达式
   *
   * @params params
   * @return
   */
  //    public List<ScopeData> queryScopes(List<String> params, ScopeContext context) throws
  // NoSuchMethodException {
  //        // 将字符串转换成表达式
  //        List<ScopeData> all = new ArrayList<>();
  //        for (String param: params) {
  //            ScopeData scopeData = ScopeData.builder().build();
  //            if (context.getAnalysis()){
  //                // 预解析逻辑处理
  //                List<AnalyticParameter> analyticParameter = getAnalyticParameter(param);
  //                scopeData.setAnalyticParameters(analyticParameter);
  //                scopeData.setExpression(param);
  //                all.add(scopeData);
  //            } else {
  //                CurrentState data = null;
  //                Object expression = commonService.invokeMethod(param, context);
  //                List<AnalyticParameter> analyticParameter = getAnalyticParameter(param);
  //                scopeData.setAnalyticParameters(analyticParameter);
  //                scopeData.setExpression(param);
  //                if (expression instanceof UseMethod.ExpressMethod) {
  //                    data = ((UseMethod.ExpressMethod) expression).current;
  //                    scopeData.setType(data.getType());
  //                    scopeData.setData(data.getData());
  //                    all.add(scopeData);
  //                }
  //            }
  //        }
  //        return all;
  //    }

  //    public List<AnalyticParameter> getAnalyticParameter(String param){
  //        List<AnalyticParameter> all = new ArrayList<>();
  //        // String[] split = param.split(".");
  //        List<String> strs = Arrays.asList(param.split("\\."));
  //        for (int i = 0; i < strs.size(); i++){
  //            // 获取小括号中的数据
  //            ExpressionObject expressionObject = getExpressionObject(strs.get(i));
  //            // 获取目标函数返回对象
  //            ExpressionObject expressionObjectUp = null;
  //            // 上一个属性
  //            if (i-1>=0) {
  //                expressionObjectUp = getExpressionObject(strs.get(i-1));
  //            }
  //            // 获取小括号前的函数名
  //            String s = strs.get(i).split("\\(")[0];
  //
  //            // 根据当前类型、函数、目标类型，判断该表达式是否正确
  //            boolean right = allCombinations.isRight(expressionObjectUp != null ?
  // expressionObjectUp.getType() : null, s, expressionObject.getType());
  //            // 函数中文名
  //            String expressionTypeValue = ExpressionTypeEnum.getExpressionTypeValue(s);
  //            List<ExpressionAttribute> attributes = new ArrayList<>();
  //            if (expressionObject.getId() != null) {
  //                String objectByID = byID.getObjectByID(expressionObject.getType(),
  // expressionObject.getId());
  //
  // attributes.add(ExpressionAttribute.builder().key("id").opts("=").value(expressionObject.getId()).title("主键").name(objectByID).build());
  //            }
  //            if (expressionObject.getAttributes() != null &&
  // expressionObject.getAttributes().size() > 0) {
  //                for (ExpressionAttribute atr: expressionObject.getAttributes()) {
  //                    SecurityExpressType type = expressionObject.getType();
  //                    Map<String, String> queryTypeToAttributeMap =
  // typeAttribute.getQueryTypeToAttributeMap(type);
  //                    // 通过key  获取value
  //                    String title = queryTypeToAttributeMap.get(atr.getKey());
  //
  // attributes.add(ExpressionAttribute.builder().key(atr.getKey()).value(atr.getValue()).opts(atr.getOpts()).title(title).build());
  //                }
  //            }
  //            if (expressionTypeValue != null) {
  //                ExpressionDisplace expressionDisplace = null;
  //                if (expressionObject.getDisplace() != null &&
  // expressionObject.getDisplace().getName() != null) {
  //                    expressionDisplace =
  // ExpressionDisplace.builder().name(expressionObject.getDisplace().getName()).index(expressionObject.getDisplace().getIndex()).title(FilterEnum.getFilterEnumValue(expressionObject.getDisplace().getName())).build();
  //                }
  //
  //
  // all.add(AnalyticParameter.builder().functionName(s).code(right).displace(expressionDisplace).title(expressionTypeValue).targetType(expressionObject.getType()).targetTitle(SecurityExpressType.getSecurityTypeValue(expressionObject.getType().toString())).attribute(attributes).build());
  //            }
  //        }
  //        return all;
  //    }

  //    public ExpressionObject getExpressionObject(String data) {
  //        String displaceData = CommentParameter.getDisplaceData(data);
  //        String doubleQuote = CommentParameter.getDoubleQuote(displaceData);
  //        ExpressionObject expressionObject = CommentParameter.parameterParse(doubleQuote);
  //        return expressionObject;
  //    }

  //    public List<SecurityScopeObject> scopesAnalysis(List<String> ids){
  //        return securityService.scopesAnalysis(ids);
  //    }

  public List<RoleScope> roleScopes(BusinessFilter filter) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new BusinessFilter()).getBuilder();
    return roleScopeService.findAll(builder.build());
  }

  public RoleScope roleScope(String id) {
    return roleScopeService.get(id);
  }

  /** 查询角色分类 */
  public RoleTypeConnection roleTypes(
      RoleTypeFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<RoleType> pager = new Pager<>(page, pageSize, orderBy);
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new RoleTypeFilter()).getBuilder();
    return Kit.connection(
        roleService.findTypePager(pager, builder.build()), RoleTypeConnection.class);
  }

  /** 查询角色 */
  public RoleConnection findRoles(RoleFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<Role> pager = new Pager<>(page, pageSize, orderBy);
    PropertyFilterBuilder builder = ObjectUtil.defaultValue(filter, new RoleFilter()).getBuilder();
    return Kit.connection(roleService.findPager(pager, builder.build()), RoleConnection.class);
  }

  /** 查询权限分类 */
  public PermissionTypeConnection permissionTypes(
      PermissionTypeFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<PermissionType> pager = new Pager<>(page, pageSize, orderBy);
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new PermissionTypeFilter()).getBuilder();
    return Kit.connection(
        permissionService.findTypePager(pager, builder.build()), PermissionTypeConnection.class);
  }

  /** 查询权限分类 */
  public PermissionConnection permissions(
      PermissionFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<Permission> pager = new Pager<>(page, pageSize, orderBy);
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new PermissionFilter()).getBuilder();
    return Kit.connection(
        permissionService.findPager(pager, builder.build()), PermissionConnection.class);
  }

  /** 查询用户是否有指定的权限 */
  public String userPermissionsStatus(long userId, String permissions) {
    return userService.checkUserPermissions(userId, permissions);
  }

  /**
   * 查询权限详情
   *
   * @param id
   * @return
   */
  public List<Permission> permission(String id, String resourceId) {
    if (ObjectUtils.isEmpty(resourceId)) {
      return new ArrayList<Permission>();
    }
    return permissionService.permission(id, resourceId);
  }

  /**
   * 根据目标类型查询对应的属性
   *
   * @param type
   * @return
   */
  //    public List<ScopeTypeResult> queryTypeToAttribute(SecurityExpressType type) {
  //        Map<String, String> queryTypeToAttributeMap =
  // typeAttribute.getQueryTypeToAttributeMap(type);
  //        if (queryTypeToAttributeMap != null) {
  //            return getTypeAttribute(queryTypeToAttributeMap);
  //        }
  //        return null;
  //    }

  /**
   * 查询类型
   *
   * @param
   * @return
   */
  //    public List<ScopeTypeResult> queryScopeTypes(String expression) {
  //        // 解析表达式
  //        List<String> strs = Arrays.asList(expression.split("\\."));
  //        ExpressionObject expressionObject = getExpressionObject(strs.get(strs.size() - 1));
  //        SecurityExpressType type = expressionObject.getType();
  //        if (type != null) {
  //            switch (type) {
  //                case employee:
  //                    return allCombinations.getEmployeeCombination(type);
  //                case department:
  //                    return allCombinations.getDepartmentCombination(type);
  //                case job:
  //                    return allCombinations.getJobCombination(type);
  //                case position:
  //                    return allCombinations.getPositionCombination(type);
  //                case employeeGroup:
  //                    return allCombinations.getEmployeeGroupCombination(type);
  //                case organization:
  //                    return allCombinations.getOrganizationCombination(type);
  //                case role:
  //                    return allCombinations.getRoleCombination(type);
  //            }
  //        }
  //        return null;
  //    }

  // 遍历map，返回list
  public List<ScopeTypeResult> getTypeAttribute(Map<String, String> map) {
    List<ScopeTypeResult> list = new ArrayList<>();
    for (Map.Entry<String, String> m : map.entrySet()) {
      list.add(
          ScopeTypeResult.builder()
              .key(m.getKey())
              .title(m.getValue())
              .type(ResultTypeScope.attribute)
              .build());
    }
    return list;
  }
}
