package cn.asany.organization.core.service;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.dao.OrganizationDao;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-04-24 17:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    public Organization save(Organization department) {
        return this.organizationDao.save(department);
    }

    public Organization get(String id) {
        Optional<Organization> byId = organizationDao.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
       // return this.organizationDao.findById(id).orElse(null);
    }

    public Organization update(String id, boolean merge, Organization department) {
        department.setId(id);
        return this.organizationDao.update(department, merge);
    }

    public void delete(String id) {
        this.organizationDao.deleteById(id);
    }

    public List<Organization> findAll(List<PropertyFilter> filters) {

        return this.organizationDao.findAll(filters, new OrderBy("updatedAt", OrderBy.Direction.DESC).toSort());
    }
}
