package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.FilePart;
import cn.asany.storage.data.service.FilePartService;
import cn.asany.storage.data.service.FileService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.spring.SpELUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分片上传
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class FragmentStoragePlugin implements StoragePlugin {

  @Autowired private FileService fileService;
  @Autowired private FilePartService filePartService;

  @Override
  public boolean supports(UploadContext context) {
    return false;
  }

  @SneakyThrows
  @Override
  public FileObject upload(UploadContext context) {
    UploadOptions options = context.getOptions();
    FileObject object = context.getObject();
    File file = context.getFile();
    Storage storage = context.getStorage();
    String location = context.getLocation();

    String fileName = object.getName();
    String contentType = object.getMimeType();
    // 判断是否为分段上传
    boolean isPart = options.isPart();
    // 生成分段上传的文件名
    if (isPart && "blob".equalsIgnoreCase(fileName)) {
      fileName = options.getPartName();
    }

    if (log.isDebugEnabled()) {
      log.debug(
          "上传文件参数:{fileName:"
              + contentType
              + ",contentType:"
              + fileName
              + ",dir:"
              + options.getSpace()
              + ",isPart:"
              + isPart
              + "}");
    }

    // 获取文件上传目录的配置信息
    Optional<FilePart> optionalFilePart =
        filePartService.findByPartFileHash(options.getEntireFileHash(), options.getPartFileHash());
    if (!optionalFilePart.isPresent()) {
      // 上传分片数据
      FileDetail fileDetail = (FileDetail) context.upload(file, "fragment");
      filePartService.save(
          fileDetail.getPath(),
          fileDetail.getStorage().getId(),
          options.getEntireFileHash(),
          options.getPartFileHash(),
          options.getTotal(),
          options.getIndex());
    }
    FilePart filePart = new FilePart();
    FileDetail fileDetail = fileService.getOneByPath(filePart.getPath());
    // 查询上传的片段
    List<FilePart> fileParts = filePartService.find(options.getEntireFileHash());
    FilePart part = ObjectUtil.remove(fileParts, "index", 0);
    FileDetail entireFileDetail =
        this.uploadPart(part, filePart, fileParts, contentType, options, storage);
    if (entireFileDetail != null) {
      fileDetail = entireFileDetail;
    }
    return fileDetail;
  }

  private FileDetail uploadPart(
      FilePart part,
      FilePart filePart,
      List<FilePart> fileParts,
      String contentType,
      UploadOptions uploadOptions,
      Storage storage)
      throws IOException {
    FileDetail fileDetail = null;
    if (part == null) {
      List<FilePart> joinFileParts = new ArrayList<>();
      ObjectUtil.join(joinFileParts, fileParts, "index");

      if (joinFileParts.size() == uploadOptions.getTotal()) {
        // 临时文件
        File tmp = FileUtil.tmp();
        // 合并 Part 文件
        try (FileOutputStream out = new FileOutputStream(tmp)) {
          for (FilePart filesPart : joinFileParts) {
            InputStream in = storage.readFile(filesPart.getPath());
            StreamUtil.copy(in, out);
            StreamUtil.closeQuietly(in);
            storage.removeFile(filesPart.getPath());
            ObjectUtil.remove(
                fileParts,
                SpELUtil.getExpression(
                    " absolutePath == #value.getAbsolutePath() and fileManagerId == #value.getFileManagerId() "),
                filePart);
          }
        }

        // 保存合并后的新文件
        fileDetail = null; // this.upload(tmp, contentType, uploadOptions.getEntireFileName(),
        // uploadOptions.getEntireFileDir());

        // 删除临时文件
        FileUtil.delFile(tmp);

        // 删除 Part 文件
        for (FilePart filesPart : fileParts) {
          storage.removeFile(filesPart.getPath());
        }

        // 在File_PART 表冗余一条数据 片段为 0
        filePartService.save(
            fileDetail.getPath(),
            fileDetail.getStorage().getId(),
            uploadOptions.getEntireFileHash(),
            uploadOptions.getEntireFileHash(),
            uploadOptions.getTotal(),
            0);
      }
    } else {
      // 删除 Part 文件
      for (FilePart filesPart : fileParts) {
        storage.removeFile(filesPart.getPath());
      }
    }
    return fileDetail;
  }
}
