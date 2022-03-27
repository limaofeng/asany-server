package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.MultipartUpload;
import cn.asany.storage.data.bean.MultipartUploadChunk;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.dto.SimpleFileObject;
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

  private final FileService fileService;
  private final MultipartUploadService multipartUploadService;

  public MultipartStoragePlugin(
      FileService fileService, MultipartUploadService multipartUploadService) {
    this.fileService = fileService;
    this.multipartUploadService = multipartUploadService;
  }

  @Override
  public boolean supports(UploadContext context) {
    return context.getOptions().isMultipartUpload();
  }

  @SneakyThrows
  @Override
  public FileObject upload(UploadContext context, Invocation invocation) {
    UploadOptions options = context.getOptions();
    FileObject object = context.getObject();
    File file = context.getFile();
    Storage storage = context.getStorage();
    String location = context.getLocation();
    String multipartUploadId = options.getUploadId();
    String fileHash = options.getHash();

    String fileName = object.getName();
    String contentType = object.getMimeType();

    int partNumber =
        Integer.parseInt(RegexpUtil.parseGroup(WebUtil.getExtension(fileName), "part(\\d+)$", 1));

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
      return upladed.toFileObject();
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

    if (Objects.equals(multipartUpload.getChunkLength(), multipartUpload.getUploadedParts())) {
      System.out.println("全部片段已经上传, 开始合并文件");
      return new SimpleFileObject();
    } else {
      return chunk.toFileObject();
    }

    // 获取文件上传目录的配置信息
    //    Optional<MultipartUploadChunk> optionalFilePart =
    //        filePartService.findByPartFileHash(options.getEntireFileHash(),
    // options.getPartFileHash());

    //    if (!optionalFilePart.isPresent()) {
    // 上传分片数据
    //      filePartService.save(
    //          fileObject.getPath(),
    //          storage.getId(),
    //          options.getEntireFileHash(),
    //          options.getPartFileHash(),
    //          options.getTotalParts(),
    //          partNumber);
    //    }

    // 查询上传的片段
    //    List<MultipartUploadChunk> chunks = filePartService.find(options.getEntireFileHash());
    //
    //    if (chunks.size() == options.getTotalParts()) {
    //      return completeMultipartUpload(chunks, options);
    //    }

  }

  private FileObject completeMultipartUpload(
      List<MultipartUploadChunk> chunks, UploadOptions options) {

    //    FileDetail fileDetail = null;
    //    List<FilePart> joinFileParts = new ArrayList<>();
    //    ObjectUtil.join(joinFileParts, fileParts, "index");
    //
    //    if (joinFileParts.size() == uploadOptions.getTotalParts()) {
    //      // 临时文件
    //      File tmp = FileUtil.tmp();
    //      // 合并 Part 文件
    //      try (FileOutputStream out = new FileOutputStream(tmp)) {
    //        for (FilePart filesPart : joinFileParts) {
    //          InputStream in = storage.readFile(filesPart.getPath());
    //          StreamUtil.copy(in, out);
    //          StreamUtil.closeQuietly(in);
    //          storage.removeFile(filesPart.getPath());
    //          ObjectUtil.remove(
    //              fileParts,
    //              SpELUtil.getExpression(
    //                  " absolutePath == #value.getAbsolutePath() and fileManagerId ==
    // #value.getFileManagerId() "),
    //              filePart);
    //        }
    //      }
    //
    //      // 保存合并后的新文件
    //      fileDetail = null; // this.upload(tmp, contentType, uploadOptions.getEntireFileName(),
    //      // uploadOptions.getEntireFileDir());
    //
    //      // 删除临时文件
    //      FileUtil.delFile(tmp);
    //
    //      // 删除 Part 文件
    //      for (FilePart filesPart : fileParts) {
    //        storage.removeFile(filesPart.getPath());
    //      }
    //
    //      // 在File_PART 表冗余一条数据 片段为 0
    //      filePartService.save(
    //          fileDetail.getPath(),
    //          fileDetail.getStorageConfig().getId(),
    //          uploadOptions.getEntireFileHash(),
    //          uploadOptions.getEntireFileHash(),
    //          uploadOptions.getTotalParts(),
    //          0);
    //    }
    //    // 删除 Part 文件
    //    for (FilePart filesPart : fileParts) {
    //      storage.removeFile(filesPart.getPath());
    //    }
    //    return fileDetail.toFileObject();
    //  }
    return new SimpleFileObject();
  }
}
