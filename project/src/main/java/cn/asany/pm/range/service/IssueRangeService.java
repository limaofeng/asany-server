package cn.asany.pm.range.service;

import cn.asany.pm.range.bean.IssueRange;
import cn.asany.pm.range.dao.IssueRangeDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class IssueRangeService {
  @Autowired private IssueRangeDao issueRangeDao;

  public List<IssueRange> findAll() {
    return issueRangeDao.findAll();
  }

  public IssueRange findById(Long id) {
    return issueRangeDao.findById(id).orElse(null);
  }
}
