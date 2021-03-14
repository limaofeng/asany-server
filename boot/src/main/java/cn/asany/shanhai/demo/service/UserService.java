package cn.asany.shanhai.demo.service;

import cn.asany.shanhai.demo.bean.User;
import cn.asany.shanhai.demo.dao.UserDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author limaofeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 保存用户
     *
     * @param user 用户对象
     */
    public User save(User user) {
        return this.userDao.save(user);
    }

    public User update(Long id, boolean merge, User user) {
        user.setId(id);
        return this.userDao.update(user, merge);
    }

    public Pager<User> findPager(Pager<User> pager, List<PropertyFilter> filters) {
        return this.userDao.findPager(pager, filters);
    }

    public void delete(Long... ids) {
        this.userDao.deleteInBatch(Arrays.stream(ids).map(id -> User.builder().id(id).build()).collect(Collectors.toList()));
    }

    public Optional<User> get(Long id) {
        return this.userDao.findById(id);
    }
}