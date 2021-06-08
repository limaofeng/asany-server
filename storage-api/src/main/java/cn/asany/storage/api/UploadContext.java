package cn.asany.storage.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

/**
 * 上传上下文
 *
 * @author limaofeng
 */
@Builder
public class UploadContext {
    private UploadService uploadService;
    @Getter
    @Setter
    private boolean uploaded;
    @Getter
    private File file;
    @Getter
    private FileObject object;
    @Getter
    private UploadOptions options;
    @Getter
    private String location;
    @Getter
    private Storage storage;
    @Getter
    private String storageId;

    public FileObject upload(File file, String space) throws IOException {
        return this.uploadService.upload(new UploadFileObject(file), UploadOptions.builder().space(space).build());
    }
}
