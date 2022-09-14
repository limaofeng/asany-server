package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.ModuleDao;
import cn.asany.shanhai.core.domain.Module;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

  private final ModuleDao moduleDao;

  public ModuleService(ModuleDao moduleDao) {
    this.moduleDao = moduleDao;
  }

  public Page<Module> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.moduleDao.findPage(pageable, filters);
  }

  public Optional<Module> findById(Long id) {
    return this.moduleDao.findById(id);
  }
}
