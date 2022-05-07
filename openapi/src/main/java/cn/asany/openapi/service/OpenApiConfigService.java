package cn.asany.openapi.service;

import cn.asany.openapi.bean.OpenApiConfig;
import cn.asany.openapi.bean.enums.OpenApiType;
import cn.asany.openapi.configs.WeixinConfig;
import cn.asany.openapi.dao.OpenApiConfigDao;
import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.exception.AppidNotFoundException;
import cn.asany.weixin.framework.session.WeixinApp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

  public OpenApiConfig getDefaultWeixin() {
    List<OpenApiConfig> configs =
        openApiConfigDao.findAll(
            PropertyFilter.builder().equal("type", OpenApiType.WEIXIN).build());
    return configs.isEmpty() ? null : configs.get(0);
  }

  @Override
  public WeixinApp loadAccountByAppid(String appid) throws AppidNotFoundException {
    Optional<OpenApiConfig> configOptional =
        this.openApiConfigDao.findOne(
            PropertyFilter.builder()
                .equal("type", OpenApiType.WEIXIN)
                .equal("appid", appid)
                .build());
    return configOptional
        .map(it -> it.toConfig(WeixinConfig.class))
        .orElseThrow(() -> new AppidNotFoundException(" appid [ " + appid + " ] 不存在 "));
  }

  @Override
  public List<WeixinApp> getAll() {
    return this.openApiConfigDao
        .findAll(PropertyFilter.builder().equal("type", OpenApiType.WEIXIN).build()).stream()
        .map(it -> it.toConfig(WeixinConfig.class))
        .collect(Collectors.toList());
  }
}
