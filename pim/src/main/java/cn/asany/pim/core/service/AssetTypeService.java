package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.AssetTypeDao;
import org.springframework.stereotype.Service;

@Service
public class AssetTypeService {
  private final AssetTypeDao assetTypeDao;

  public AssetTypeService(AssetTypeDao assetTypeDao) {
    this.assetTypeDao = assetTypeDao;
  }
}
