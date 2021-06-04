package cn.asany.storage.data.listener;

import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.FileManagerFactory;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileManagerEventListener extends AbstractChangedListener<StorageConfig> {

    private static final long serialVersionUID = 1082020263270806626L;

    @Autowired
    private FileManagerFactory factory;

    public FileManagerEventListener() {
        super(EventType.POST_INSERT, EventType.POST_UPDATE, EventType.POST_DELETE);
    }

    @Override
    public void onPostDelete(StorageConfig config, PostDeleteEvent event) {
        factory.remove(config);
    }

    @Override
    public void onPostUpdate(StorageConfig config, PostUpdateEvent event) {
        if (modify(event, "configParamStore")) {
//            factory.register(config.getId(), config.getType(), config.getConfigParams());
        }
    }

    @Override
    public void onPostInsert(StorageConfig config, PostInsertEvent event) {
//        factory.register(config.getId(), config.getType(), config.getConfigParams());
    }

}
