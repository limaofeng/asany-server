package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.AssetStatusDao;
import org.springframework.stereotype.Service;

@Service
public class AssetStatusService {

  private final AssetStatusDao assetStatusDao;

  public AssetStatusService(AssetStatusDao assetStatusDao) {
    this.assetStatusDao = assetStatusDao;
  }
}
