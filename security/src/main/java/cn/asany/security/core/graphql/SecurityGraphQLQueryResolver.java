package cn.asany.security.core.graphql;

import cn.asany.security.core.domain.*;
import cn.asany.security.core.graphql.enums.ResultTypeScope;
import cn.asany.security.core.graphql.input.ContextInput;
import cn.asany.security.core.graphql.input.ScopeTypeAttribute;
import cn.asany.security.core.graphql.input.ScopeTypeResult;
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
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  /** ??????????????????,??????????????? */
  public UserConnection users(UserFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    PropertyFilterBuilder builder = ObjectUtil.defaultValue(filter, new UserFilter()).getBuilder();
    return Kit.connection(userService.findPage(pageable, builder.build()), UserConnection.class);
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
   * ??????jQuery???????????????
   *
   * @params params
   * @return
   */
  //    public List<ScopeData> queryScopes(List<String> params, ScopeContext context) throws
  // NoSuchMethodException {
  //        // ??????????????????????????????
  //        List<ScopeData> all = new ArrayList<>();
  //        for (String param: params) {
  //            ScopeData scopeData = ScopeData.builder().build();
  //            if (context.getAnalysis()){
  //                // ?????????????????????
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
  //            // ???????????????????????????
  //            ExpressionObject expressionObject = getExpressionObject(strs.get(i));
  //            // ??????????????????????????????
  //            ExpressionObject expressionObjectUp = null;
  //            // ???????????????
  //            if (i-1>=0) {
  //                expressionObjectUp = getExpressionObject(strs.get(i-1));
  //            }
  //            // ??????????????????????????????
  //            String s = strs.get(i).split("\\(")[0];
  //
  //            // ???????????????????????????????????????????????????????????????????????????
  //            boolean right = allCombinations.isRight(expressionObjectUp != null ?
  // expressionObjectUp.getType() : null, s, expressionObject.getType());
  //            // ???????????????
  //            String expressionTypeValue = ExpressionTypeEnum.getExpressionTypeValue(s);
  //            List<ExpressionAttribute> attributes = new ArrayList<>();
  //            if (expressionObject.getId() != null) {
  //                String objectByID = byID.getObjectByID(expressionObject.getType(),
  // expressionObject.getId());
  //
  // attributes.add(ExpressionAttribute.builder().key("id").opts("=").value(expressionObject.getId()).title("??????").name(objectByID).build());
  //            }
  //            if (expressionObject.getAttributes() != null &&
  // expressionObject.getAttributes().size() > 0) {
  //                for (ExpressionAttribute atr: expressionObject.getAttributes()) {
  //                    SecurityExpressType type = expressionObject.getType();
  //                    Map<String, String> queryTypeToAttributeMap =
  // typeAttribute.getQueryTypeToAttributeMap(type);
  //                    // ??????key  ??????value
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

  public List<RoleSpace> roleScopes(BusinessFilter filter) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new BusinessFilter()).getBuilder();
    return roleScopeService.findAll(builder.build());
  }

  public RoleSpace roleScope(String id) {
    return roleScopeService.get(id);
  }

  /** ???????????? */
  public RoleConnection findRoles(RoleFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    PropertyFilterBuilder builder = ObjectUtil.defaultValue(filter, new RoleFilter()).getBuilder();
    return Kit.connection(roleService.findPage(pageable, builder.build()), RoleConnection.class);
  }

  /** ?????????????????? */
  public PermissionTypeConnection permissionTypes(
      PermissionTypeFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new PermissionTypeFilter()).getBuilder();
    return Kit.connection(
        permissionService.findTypePage(pageable, builder.build()), PermissionTypeConnection.class);
  }

  /** ?????????????????? */
  public PermissionConnection permissions(
      PermissionFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new PermissionFilter()).getBuilder();
    return Kit.connection(
        permissionService.findPage(pageable, builder.build()), PermissionConnection.class);
  }

  /** ???????????????????????????????????? */
  public String userPermissionsStatus(long userId, String permissions) {
    return userService.checkUserPermissions(userId, permissions);
  }

  /**
   * ??????????????????
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
   * ???????????????????????????????????????
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
   * ????????????
   *
   * @param
   * @return
   */
  //    public List<ScopeTypeResult> queryScopeTypes(String expression) {
  //        // ???????????????
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

  // ??????map?????????list
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
