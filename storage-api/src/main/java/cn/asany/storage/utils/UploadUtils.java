package cn.asany.storage.utils;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.api.UploadFileObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPart;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件上传工具类
 *
 * @author limaofeng
 */
@Slf4j
public class UploadUtils {

    public static FileObject partToObject(Part part) {
        if (!(part instanceof ApplicationPart)) {
            throw new RuntimeException("Part 类型不匹配: " + part.getClass());
        }
        DiskFileItem fileItem = ClassUtil.getFieldValue(part, ApplicationPart.class, "fileItem");
        File tempFile = ClassUtil.getFieldValue(fileItem, DiskFileItem.class, "tempFile");
        return new UploadFileObject(part.getSubmittedFileName(), tempFile, FileObjectMetadata.builder()
            .contentLength(part.getSize())
            .contentType(part.getContentType())
            .build());
    }

    public static String md5(File file) {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            return DigestUtils.md5DigestAsHex(input);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IgnoreException(e);
        } finally {
            StreamUtil.closeQuietly(input);
        }
    }
}
