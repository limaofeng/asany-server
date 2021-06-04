package cn.asany.storage.data.rest;

import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.SpaceService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dirs")
public class DirectoryController {

    @Autowired
    private SpaceService spaceService;
    @Autowired
    private FileService fileService;

    @GetMapping
    public Pager<Space> search(Pager<Space> pager, List<PropertyFilter> filters) {
        if (!pager.isOrderBySetted()) {
//            pager.sort(BaseBusEntity.FIELDS_BY_CREATE_TIME, Pager.SORT_DESC);
        }
        return spaceService.findPager(pager,filters);
    }

    @GetMapping("/{id}/images")
    public Pager<FileDetail> images(@PathVariable("id") String id, Pager<FileDetail> pager, List<PropertyFilter> filters) {
        if (!pager.isOrderBySetted()) {
//            pager.sort(BaseBusEntity.FIELDS_BY_CREATE_TIME, Pager.SORT_DESC);
        }
        Space directory = spaceService.get(id);
        filters.add(new PropertyFilter("LIKES_contentType","image"));
        filters.add(new PropertyFilter("EQS_namespace", directory.getStorage().getId()));
        //filters.add(new PropertyFilter("EQS_folder.path", directory.getDirPath()));
        return fileService.findPager(pager,filters);
    }

}
