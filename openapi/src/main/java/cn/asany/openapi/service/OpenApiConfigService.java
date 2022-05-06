package cn.asany.openapi.service;

import cn.asany.openapi.bean.OpenApiConfig;
import cn.asany.openapi.bean.enums.OpenApiType;
import cn.asany.openapi.dao.OpenApiConfigDao;
import java.util.List;

import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.exception.AppidNotFoundException;
import cn.asany.weixin.framework.session.WeixinApp;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

/**
 * OpenApi 配置
 *
 * @author limaofeng
 */
@Service
public class OpenApiConfigService implements WeixinAppService {

  private final OpenApiConfigDao openApiConfigDao;

  public OpenApiConfigService(OpenApiConfigDao openApiConfigDao) {
    this.openApiConfigDao = openApiConfigDao;
  }

  public OpenApiConfig getAmapDefault() {
    List<OpenApiConfig> configs =
        openApiConfigDao.findAll(PropertyFilter.builder().equal("type", OpenApiType.AMAP).build());
    return configs.isEmpty() ? null : configs.get(0);
  }

  @Override
  public WeixinApp loadAccountByAppid(String appid) throws AppidNotFoundException {


    return null;
  }

  @Override
  public List<WeixinApp> getAll() {
    return null;
  }
}
