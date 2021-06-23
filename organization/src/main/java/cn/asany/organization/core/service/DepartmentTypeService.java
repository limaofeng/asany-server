package cn.asany.organization.core.service;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.DepartmentType;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.dao.DepartmentDao;
import cn.asany.organization.core.dao.DepartmentTypeDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("departmentTypeServoce")
public class DepartmentTypeService {

    @Autowired
    private DepartmentTypeDao departmentTypeDao;
    @Autowired
    private DepartmentDao departmentDao;

    public DepartmentType createDepartmentType(String name, String code, Long organization, Boolean multiSectoral, Long andPost) {
        DepartmentType departmentType = new DepartmentType();
        Organization organization1 = new Organization();
        departmentType.setMultiSectoral(multiSectoral);
        if (andPost != null) {
            departmentType.setAndPost(andPost);
        }
        organization1.setId(organization);
        departmentType.setName(name);
        departmentType.setOrganization(organization1);
        departmentType.setCode(code);
        return departmentTypeDao.save(departmentType);
    }

    public DepartmentType selectDepartmentTypeById(Long id) {
        Optional<DepartmentType> departmentType = departmentTypeDao.findById(id);
        if (departmentType.isPresent()) {
            return departmentType.get();
        }
        return null;
    }

    public List<DepartmentType> selectDepartmentType() {
        return departmentTypeDao.findAll();
    }

    public List<DepartmentType> selectDepartmentTypeByOrganization(Long organizationId) {
        Organization organization = Organization.builder().id(organizationId).build();
        return departmentTypeDao.findByOrganization(organization);
    }

    public boolean removeDepartmentType(Long id){
        List<Department> all = departmentDao.findAll(
                Example.of(Department.builder()
                        .type(DepartmentType.builder().id(id).build())
                        .build()));
        if (CollectionUtils.isEmpty(all)){
            departmentTypeDao.deleteById(id);
            return true;
        }
       return false;
    }
}
