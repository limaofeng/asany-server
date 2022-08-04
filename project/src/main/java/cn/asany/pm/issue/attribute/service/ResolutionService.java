package cn.asany.pm.issue.attribute.service;

import cn.asany.pm.issue.attribute.dao.ResolutionDao;
import cn.asany.pm.issue.attribute.domain.Resolution;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 解决结果
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class ResolutionService {

  private final ResolutionDao resolutionDao;

  public ResolutionService(ResolutionDao resolutionDao) {
    this.resolutionDao = resolutionDao;
  }

  public List<Resolution> findAll() {
    return resolutionDao.findAll();
  }
}
