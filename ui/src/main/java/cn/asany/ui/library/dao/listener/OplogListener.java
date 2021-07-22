package cn.asany.ui.library.dao.listener;

import cn.asany.ui.library.service.OplogService;
import org.jfantasy.framework.spring.SpringContextUtil;

import javax.persistence.*;

public class OplogListener {

    private OplogService oplogService;

    private OplogService getOplogService() {
        if(this.oplogService == null) {
            this.oplogService = SpringContextUtil.getBeanByType(OplogService.class);
        }
        return this.oplogService;
    }

    @PrePersist
    public void prePersist(Object source) {
        System.out.println("@PrePersist：" + source);
    }

    @PostPersist
    public void postPersist(Object source) {
        System.out.println("@PostPersist：" + source);
    }

    @PreUpdate
    public void preUpdate(Object source) {
        OplogService oplogService = getOplogService();
//        oplogService.log();

        System.out.println("@PrePersist：" + source);
    }

    @PostUpdate
    public void postUpdate(Object source) {
        System.out.println("@PostPersist：" + source);
    }

    @PreRemove
    public void preRemove(Object source) {
        System.out.println("@PrePersist：" + source);
    }

    @PostRemove
    public void postRemove(Object source) {
        System.out.println("@PostPersist：" + source);
    }

}
