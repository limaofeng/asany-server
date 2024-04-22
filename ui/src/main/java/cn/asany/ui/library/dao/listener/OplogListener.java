package cn.asany.ui.library.dao.listener;

import cn.asany.ui.library.domain.enums.Operation;
import cn.asany.ui.library.service.OplogService;
import jakarta.persistence.*;
import org.jfantasy.framework.spring.SpringBeanUtils;

public class OplogListener {

  private OplogService _oplogService;

  private OplogService getOplogService() {
    if (this._oplogService == null) {
      this._oplogService = SpringBeanUtils.getBeanByType(OplogService.class);
    }
    return this._oplogService;
  }

  @PostPersist
  public void postPersist(Object source) {
    OplogService oplogService = getOplogService();
    oplogService.log(Operation.INSERT, source);
  }

  @PostUpdate
  public void postUpdate(Object source) {
    OplogService oplogService = getOplogService();
    oplogService.log(Operation.UPDATE, source);
  }

  @PostRemove
  public void postRemove(Object source) {
    OplogService oplogService = getOplogService();
    oplogService.log(Operation.DELETE, source);
  }
}
