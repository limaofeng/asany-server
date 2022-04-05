package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.MultipartUpload;
import cn.asany.storage.data.bean.MultipartUploadChunk;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import java.io.File;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.stereotype.Component;

/**
 * 分片上传
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class MultipartStoragePlugin implements StoragePlugin {

  public static String ID = "multi-part";

  private final MultipartUploadService multipartUploadService;

  public MultipartStoragePlugin(
      FileService fileService, MultipartUploadService multipartUploadService) {
    this.multipartUploadService = multipartUploadService;
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return context.isMultipartUpload();
  }

  @SneakyThrows
  @Override
  public FileObject upload(UploadContext context, Invocation invocation) {
    UploadOptions options = context.getOptions();
    UploadFileObject uploadFile = context.getFile();
    Storage storage = context.getStorage();

    File file = uploadFile.getFile();

    String multipartUploadId = options.getUploadId();
    String fileHash = options.getHash();

    String fileName = uploadFile.getName();
    String contentType = uploadFile.getMimeType();

    int partNumber =
        Integer.parseInt(
            RegexpUtil.parseGroup(WebUtil.getExtension(fileName, true), "part(\\d+)$", 1));

    if (log.isDebugEnabled()) {
      log.debug(
          "上传文件参数:{fileName:"
              + fileName
              + ",contentType:"
              + contentType
              + ",dir:"
              + options.getSpace()
              + "}");
    }

    MultipartUpload multipartUpload =
        this.multipartUploadService.get(IdUtils.parseUploadId(multipartUploadId));

    List<MultipartUploadChunk> chunks = multipartUpload.getChunks();

    MultipartUploadChunk upladed = ObjectUtil.find(chunks, "index", partNumber);
    if (upladed != null) {
      FileObject fileObject = upladed.toFileObject();
      return checksum(upladed.toFileObject(), multipartUpload, storage);
    }

    String uploadId = multipartUpload.getUploadId();
    String path = multipartUpload.getPath();
    long partSize = file.length();

    String etag = storage.multipartUpload().uploadPart(path, uploadId, partNumber, file, partSize);

    MultipartUploadChunk chunk =
        multipartUploadService.appendPart(
            multipartUpload.getId(), path, fileHash, partNumber, etag, partSize);

    multipartUpload.setUploadedParts(chunks.size() + 1);

    this.multipartUploadService.updateMultipartUpload(multipartUpload);

    return checksum(chunk.toFileObject(), multipartUpload, storage);
  }

  private FileObject checksum(FileObject part, MultipartUpload multipartUpload, Storage storage) {
    if (Objects.equals(multipartUpload.getChunkLength(), multipartUpload.getUploadedParts())) {
      log.debug("全部分片已经上传:" + multipartUpload);
    }
    return part;
  }
}
