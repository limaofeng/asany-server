package cn.asany.cms.circle.service;

import cn.asany.cms.circle.dao.CircleMemberDao;
import cn.asany.cms.circle.domain.CircleMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CircleMemberService {
  @Autowired private CircleMemberDao circleMemberDao;

  public void delete(Long userId, String categoryId) {
    circleMemberDao.deleteById(0L);
  }

  //    public ArticleCategory[] circles(Long userId) {
  //        return ObjectUtil.toFieldArray(circleMemberDao.find(Restrictions.eq("userId", userId)),
  // "category", ArticleCategory.class);
  //    }

  @Transactional
  public CircleMember save(CircleMember circleMember) {
    //        CircleMember oldCircleMember =
    // this.circleMemberDao.findUnique(Restrictions.eq("userId", circleMember.getUserId()),
    // Restrictions.eq("category.id", circleMember.getCategory().getId()));
    //        if (oldCircleMember != null) {
    //            throw new ValidationException("您已经加入该圈子，请勿重复操作!");
    //        }
    //        return this.circleMemberDao.save(circleMember);
    return null;
  }
}
