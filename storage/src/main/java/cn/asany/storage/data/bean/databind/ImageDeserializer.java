package cn.asany.storage.data.bean.databind;


import cn.asany.storage.data.bean.Image;
import cn.asany.storage.data.service.FileService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

public class ImageDeserializer extends JsonDeserializer<Image> {

    private static FileService fileService;

    @Override
    public Image deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        String[] arry = value.split(":");
        return new Image(getFileService().findByPath(arry[0]));
    }

    private static FileService getFileService() {
        if (fileService == null) {
            fileService = SpringContextUtil.getBeanByType(FileService.class);
        }
        return fileService;
    }

}
