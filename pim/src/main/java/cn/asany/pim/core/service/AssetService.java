package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.AssetDao;
import org.springframework.stereotype.Service;

@Service
public class AssetService {
  private final AssetDao assetDao;

  public AssetService(AssetDao assetDao) {
    this.assetDao = assetDao;
  }
}
