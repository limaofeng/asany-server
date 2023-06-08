package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.ModuleDao;
import cn.asany.shanhai.core.domain.Module;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

  private final ModuleDao moduleDao;

  public ModuleService(ModuleDao moduleDao) {
    this.moduleDao = moduleDao;
  }

  public Page<Module> findPage(Pageable pageable, PropertyFilter filter) {
    return this.moduleDao.findPage(pageable, filter);
  }

  public Optional<Module> findById(Long id) {
    return this.moduleDao.findById(id);
  }

  public List<Module> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.moduleDao.findAll(filter, offset, limit, sort);
  }

  public Module save(Module module) {
    return this.moduleDao.save(module);
  }

  public Module update(Long id, Module module, boolean merge) {
    module.setId(id);
    return this.moduleDao.update(module, merge);
  }

  public int delete(Long... ids) {
    for (Long id : ids) {
      this.moduleDao.deleteById(id);
    }
    return ids.length;
  }
}
