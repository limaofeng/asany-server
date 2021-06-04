package cn.asany.storage.data.bean.converter;

import cn.asany.storage.data.bean.FileDetail;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;

public class FileDetailConverter implements AttributeConverter<FileDetail, String> {

    @Override
    public String convertToDatabaseColumn(FileDetail attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public FileDetail convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, FileDetail.class);
    }
}
