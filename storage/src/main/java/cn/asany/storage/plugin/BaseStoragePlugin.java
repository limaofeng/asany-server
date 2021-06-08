package cn.asany.storage.plugin;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StoragePlugin;
import cn.asany.storage.api.UploadContext;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.utils.UploadUtils;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

@Component
public class BaseStoragePlugin implements StoragePlugin {

    @Autowired
    private FileService fileService;

    @Override
    public boolean supports(UploadContext context) {
        return true;
    }

    @Override
    @SneakyThrows
    public FileObject upload(UploadContext context) {
        String location = context.getLocation();
        Storage storage = context.getStorage();

        FileObject object = context.getObject();
        File file = context.getFile();

        String md5 = UploadUtils.md5(file);

        String mimeType = FileUtil.getMimeType(file);

        String filename = StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-", ""));
        // 获取虚拟目录
        String absolutePath = location + DateUtil.format("yyyyMMdd") + File.separator + filename + "." + WebUtil.getExtension(object.getName());
        storage.writeFile(absolutePath, file);
        FileDetail detail = fileService.saveFileDetail(absolutePath, object.getName(), ObjectUtil.defaultValue(mimeType, object.getContentType()), object.getSize(), md5, context.getStorageId(), "");
        context.setUploaded(true);
        return detail;
    }


}
