package cn.asany.shanhai.view.service;

import cn.asany.shanhai.view.dao.ModelViewDao;
import cn.asany.shanhai.view.domain.ModelView;
import cn.asany.shanhai.view.domain.enums.ModelViewType;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ModelViewService {

  private final ModelViewDao modelViewDao;

  public ModelViewService(ModelViewDao modelViewDao) {
    this.modelViewDao = modelViewDao;
  }

  public Optional<ModelView> getDefaultView(Long modelId, ModelViewType type) {
    return this.modelViewDao.findOne(
        PropertyFilter.builder()
            .equal("model.id", modelId)
            .equal("type", type)
            .equal("defaultView", true)
            .build());
  }

  public Optional<ModelView> findById(Long id) {
    return this.modelViewDao.findById(id);
  }

  public List<ModelView> findAll(
      List<PropertyFilter> filters, int offset, int limit, Sort orderBy) {
    return this.modelViewDao.findAll(filters, offset, limit, orderBy);
  }
}
