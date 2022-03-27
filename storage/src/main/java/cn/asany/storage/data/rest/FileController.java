package cn.asany.storage.data.rest;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.core.FileUploadService;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.MultipartUploadChunk;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.utils.UploadUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @author limaofeng
 */
@RestController
@RequestMapping("/files")
public class FileController {

  private final FileService fileService;
  private final FileUploadService fileUploadService;
  private final UploadService uploadService;
  private final MultipartUploadService filePartService;

  @Autowired
  public FileController(
      FileService fileService,
      FileUploadService fileUploadService,
      MultipartUploadService filePartService,
      UploadService uploadService) {
    this.fileService = fileService;
    this.fileUploadService = fileUploadService;
    this.filePartService = filePartService;
    this.uploadService = uploadService;
  }

  /**
   * 上传文件<br>
   * 单独的文件上传接口，返回 FileDetail 对象
   *
   * @param part 要上传的文件
   * @param options 上传的目录标识
   * @return {String} 返回文件信息
   * @throws IOException 文件上传异常
   */
  @RequestMapping(method = RequestMethod.POST)
  public FileDetail upload(
      @RequestParam(value = "file", required = false) MultipartFile part, UploadOptions options)
      throws IOException {
    FileObject object = UploadUtils.partToObject(part);
    return (FileDetail) uploadService.upload(object, options);
  }

  /**
   * 分段上传查询
   *
   * @param hash hash
   * @return Map<Object>
   */
  @RequestMapping(value = "/{hash}/pass", method = RequestMethod.GET)
  public Map<String, Object> pass(@PathVariable("hash") String hash) {
    List<MultipartUploadChunk> parts = filePartService.find(hash);
    Map<String, Object> data = new HashMap<>();
    MultipartUploadChunk part = ObjectUtil.remove(parts, "index", 0);
    if (part != null) {
      data.put("fileDetail", fileService.findByPath(part.getPath()));
    }
    data.put("parts", parts);
    return data;
  }
}
