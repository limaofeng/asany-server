package cn.asany.storage.data.rest;

import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.SpaceService;
import cn.asany.storage.data.service.StorageService;
import java.util.List;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file-managers")
public class FileManagerController {

  @Autowired private StorageService storageService;
  @Autowired private SpaceService spaceService;

  @RequestMapping(value = "/{id}/dirs")
  public Pager<Space> dirs(
      @PathVariable("id") String id, Pager<Space> pager, List<PropertyFilter> filters) {
    filters.add(new PropertyFilter("EQS_fileManager.id", id));
    return spaceService.findPager(pager, filters);
  }
}
