package cn.asany.storage.data.bean.databind;

import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;
import java.lang.reflect.Array;

public class FileDetailsDeserializer extends JsonDeserializer<FileDetail[]> {

    private static final Log LOG = LogFactory.getLog(FileDetailsDeserializer.class);

    private static FileService fileService;

    @Override
    public FileDetail[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        String files = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
        FileDetail[] fileDetails;
        if (files.startsWith("[") && files.endsWith("]")) {
            fileDetails = JSON.deserialize(files, new TypeReference<FileDetail[]>() {
            });
            if (fileDetails != null) {
                return fileDetails;
            }
        }
        if (StringUtil.isBlank(files)) {
            return new FileDetail[0];
        }
        String[] absolutePaths = files.split(",");
        if (absolutePaths.length == 0) {
            return new FileDetail[0];
        }
        fileDetails = new FileDetail[absolutePaths.length];
        for (int i = 0, len = absolutePaths.length; i < len; i++) {
            String[] arry = absolutePaths[i].split(":");
            FileDetail fileDetail = getFileService().get(arry[0]);
            if (fileDetail == null) {
                continue;
            }
//            try {
//                fileDetails[i] = (FileDetail)fileDetail.clone();
//            } catch (CloneNotSupportedException e) {
//                LOG.error(e.getMessage(),e);
//            }
        }
        return fileDetails;
    }

    private static FileService getFileService() {
        if(fileService == null){
            fileService = SpringContextUtil.getBeanByType(FileService.class);
        }
        return  fileService;
    }
}
