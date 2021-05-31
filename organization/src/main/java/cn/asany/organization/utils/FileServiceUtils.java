package cn.asany.organization.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.jfantasy.storage.FileObject;

/**
 * 请求文件服务工具类
 */
public class FileServiceUtils {


    public static FileObject getFile(String fileIds) {
        String[] filedArray = fileIds.split(",");
        for (String fileId : filedArray) {
            try {
                HttpResponse<FileObject> response = Unirest.get("http://dj.prod.thuni-h.com/strage" + "/files/" + fileId).asObject(FileObject.class);
                return response.getBody();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
